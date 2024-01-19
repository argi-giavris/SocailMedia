package org.example.controllers.user;

import org.example.models.User;
import org.example.services.user.UserService;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.Map;

public class UserLoginController {

    public void login(Context ctx) throws SQLException {

        User user = ctx.bodyAsClass(User.class);
        String token = UserService.loginUser(user);
        ctx.json(Map.of("token", token));
    }
}
