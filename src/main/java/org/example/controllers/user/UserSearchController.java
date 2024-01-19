package org.example.controllers.user;

import io.javalin.http.Context;
import org.example.models.Paging;
import org.example.services.user.UserSearchService;
import org.example.services.user.UserService;

import java.sql.SQLException;
import java.util.List;

public class UserSearchController {

    public void searchUser(Context ctx) throws SQLException {
        String searchTerm = ctx.queryParam("name");
        Paging paging = Paging.fromContext(ctx);

        List<String> users = UserSearchService.searchUsersWithSearchTerm(searchTerm, paging);
        ctx.status(201).json(users);

    }
}
