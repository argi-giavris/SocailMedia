package org.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpResponseException;
import org.eclipse.jetty.http.HttpStatus;
import org.example.dto.UserUsernameAndRoleDto;
import org.example.models.Role;
import org.example.models.User;

import java.util.Date;
import java.util.Objects;

public class JwtUtils {

    public static String generateJwt(User user) {
        AppConfig config = ConfigLoader.loadConfig("config.yml");
        JwtConfig jwtConfig = config.getJwt();
        Algorithm algorithm = Algorithm.HMAC512(jwtConfig.getSecretKey());
        return JWT.create()
                .withClaim("username", user.getUsername())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getExpiresInMinutes())) // 1 hour expiration
                .sign(algorithm);
    }

    public static Handler ensureJwtAuth = ctx -> {
        AppConfig config = ConfigLoader.loadConfig("config.yml");
        JwtConfig jwtConfig = config.getJwt();
        String authHeader = ctx.header("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Algorithm algorithm = Algorithm.HMAC512(jwtConfig.getSecretKey());

                DecodedJWT jwt = JWT.require(algorithm)
                        .build()
                        .verify(token);
            } catch (JWTVerificationException exception){
                // Invalid signature/claims
                ctx.status(403).result("Invalid JWT token: " + exception.getMessage());

                throw new HttpResponseException(HttpStatus.FORBIDDEN_403, "Access Denied");
            }
        } else {
            ctx.status(401).result("Missing or invalid Authorization header");
        }
    };

    public static UserUsernameAndRoleDto getUserFromJwt(Context ctx) {
        try {

            String token = Objects.requireNonNull(ctx.header("Authorization")).substring(7);
            DecodedJWT jwt = JWT.decode(token);


            String username = jwt.getClaim("username").asString();
            Role role = Role.valueOf(jwt.getClaim("role").asString());

            return new UserUsernameAndRoleDto(username, role);
        } catch (Exception e) {
            throw new RuntimeException("Invalid parse of token");
        }
    }

    public static String getUserUsernameFromJwt(Context ctx) {
        try {

            String token = Objects.requireNonNull(ctx.header("Authorization")).substring(7);
            DecodedJWT jwt = JWT.decode(token);

            String username = jwt.getClaim("username").asString();


            return username;
        } catch (Exception e) {
            throw new RuntimeException("Invalid parse of token");
        }
    }
}
