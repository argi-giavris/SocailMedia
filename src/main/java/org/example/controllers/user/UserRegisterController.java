package org.example.controllers.user;


import io.javalin.http.Context;
import org.example.models.User;
import org.example.services.user.UserService;

public class UserRegisterController {

    public void register(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        try {
            UserService.registerUser(user);
            ctx.status(201).json(user.getUsername());
        } catch (RuntimeException e) {
            ctx.status(500).json(e.getMessage());
        }
    }
}
