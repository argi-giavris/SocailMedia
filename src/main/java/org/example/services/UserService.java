package org.example.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.example.models.User;
import org.example.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.utils.JwtUtils;

import java.util.Date;



public class UserService {


    public static void registerUser(User user) throws RuntimeException{

        UserRepository userRepo = new UserRepository();
        if(!userRepo.emailHasValidFormat(user.getUsername())) {
            throw new RuntimeException("Email is not valid");
        }

        if (userRepo.userExistsByEmail(user.getUsername())) {
            throw new RuntimeException("User with current email already exists");
        }
        if (!userRepo.isValidPassword(user.getPassword())) {
            throw new RuntimeException("Invalid password. Password must meet the specified criteria.");
        }
        userRepo.registerUser(user);


    }

    public static String loginUser(User user) {
        User authenticatedUser = authenticateUser(user);
        if (authenticatedUser != null) {
            JwtUtils jwt = new JwtUtils();
            return jwt.generateJwt(authenticatedUser);
        } else {
            throw new RuntimeException("Authentication failed");
        }
    }

    private static User authenticateUser(User user) {

        UserRepository userRepo = new UserRepository();
        User storedUser = userRepo.getUserByUsername(user.getUsername());
        if (storedUser != null && userRepo.verifyPassword(user.getPassword(), storedUser.getPassword())) {
            return storedUser;
        }
        return null;
    }



}
