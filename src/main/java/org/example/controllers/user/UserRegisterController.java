package org.example.controllers.user;


import io.javalin.http.Context;
import org.example.models.User;
import org.example.services.UserService;

public class UserRegisterController {

    public void register(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        try {
            UserService.registerUser(user);
            ctx.status(201).json(user);
        } catch (RuntimeException e) {
            ctx.status(500).json(e.getMessage());
        }
    }
}
