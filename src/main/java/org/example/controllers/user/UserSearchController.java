package org.example.controllers.user;

import io.javalin.http.Context;
import org.example.services.FollowService;
import org.example.services.UserService;

import java.util.List;

public class UserSearchController {

    public void searchUser(Context ctx) {
        String searchTerm = ctx.queryParam("name");

        try {
            List<String> users = UserService.searchUsersWithSearchTerm(searchTerm);
            ctx.status(201).json(users);
        }catch (RuntimeException e) {
            ctx.status(500).json(e.getMessage());
        }
    }
}
