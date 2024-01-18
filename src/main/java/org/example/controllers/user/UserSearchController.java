package org.example.controllers.user;

import io.javalin.http.Context;
import org.example.models.Paging;
import org.example.services.user.UserSearchService;
import org.example.services.user.UserService;

import java.util.List;

public class UserSearchController {

    public void searchUser(Context ctx) {
        String searchTerm = ctx.queryParam("name");
        Paging paging = Paging.fromContext(ctx);

        try {
            List<String> users = UserSearchService.searchUsersWithSearchTerm(searchTerm, paging);
            ctx.status(201).json(users);
        }catch (RuntimeException e) {
            ctx.status(500).json(e.getMessage());
        }
    }
}
