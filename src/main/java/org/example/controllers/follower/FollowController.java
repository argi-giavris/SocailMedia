package org.example.controllers.follower;

import io.javalin.http.Context;
import org.example.dto.FollowUserIdDto;
import org.example.models.Follower;
import org.example.services.follower.FollowService;
import org.example.utils.JwtUtils;

public class FollowController {

    public void follow(Context ctx) {
        //json will have the userId that the logged user wants to follow
        FollowUserIdDto userIdToFollow = ctx.bodyAsClass(FollowUserIdDto.class);
        String username = JwtUtils.getUserUsernameFromJwt(ctx);

        try {
            FollowService.addFollower(userIdToFollow, username);
        } catch (RuntimeException e) {
            ctx.status(500).json(e.getMessage());
        }

    }

    public void unfollow(Context ctx) {
        //json will have the userId that the logged user wants to follow
        Follower loggedUser = ctx.bodyAsClass(Follower.class);
        String username = JwtUtils.getUserUsernameFromJwt(ctx);

        try {
            FollowService.removeFollower(loggedUser, username);
        } catch (RuntimeException e) {
            ctx.status(500).json(e.getMessage());
        }
    }
}
