package org.example.controllers.follower;

import io.javalin.http.Context;
import org.example.models.CommentView;
import org.example.services.CommentService;
import org.example.services.FollowService;
import org.example.utils.JwtUtils;

import java.util.List;

public class GetFollowersController {

    public void viewFollowers(Context ctx) {
        String username = JwtUtils.getUserUsernameFromJwt(ctx);
        try {
            List<String> followers = FollowService.getFollowersOfUser(username);
            ctx.status(201).json(followers);
        }catch (RuntimeException e) {
            ctx.status(500).json(e.getMessage());
        }
    }

    public void viewFollowing(Context ctx) {
        String username = JwtUtils.getUserUsernameFromJwt(ctx);
        try {
            List<String> following = FollowService.getFollowingOfUser(username);
            ctx.status(201).json(following);
        }catch (RuntimeException e) {
            ctx.status(500).json(e.getMessage());
        }
    }
}
