package org.example.controllers.user;


import io.javalin.http.Context;
import org.example.models.User;
import org.example.services.user.UserService;

import java.sql.SQLException;

public class UserRegisterController {

    public void register(Context ctx) throws SQLException {
        User user = ctx.bodyAsClass(User.class);

        UserService.registerUser(user);
        ctx.status(201).json(user.getUsername());
    }
}
