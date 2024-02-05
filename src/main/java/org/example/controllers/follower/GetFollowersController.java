package org.example.controllers.follower;

import io.javalin.http.Context;
import org.example.models.Paging;
import org.example.services.follower.GetFollowersOfUserService;
import org.example.services.follower.GetFollowingOfUserService;
import org.example.utils.JwtUtils;

import java.sql.SQLException;
import java.util.List;

public class GetFollowersController {

    public void viewFollowers(Context ctx) throws SQLException {
        String username = JwtUtils.getUserUsernameFromJwt(ctx);
        Paging paging = Paging.fromContext(ctx);

        List<String> followers = GetFollowersOfUserService.getFollowersOfUser(username, paging);
        ctx.status(201).json(followers);

    }

    public void viewFollowing(Context ctx) throws SQLException{
        String username = JwtUtils.getUserUsernameFromJwt(ctx);
        int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
        int pageSize = ctx.queryParamAsClass("pageSize", Integer.class).getOrDefault(20);
        Paging paging = new Paging(page, pageSize);

        List<String> following = GetFollowingOfUserService.getFollowingOfUser(username, paging);
        ctx.status(201).json(following);

    }
}
