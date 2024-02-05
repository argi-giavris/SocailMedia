package org.example.controllers.follower;

import io.javalin.http.Context;
import org.example.dto.FollowUserIdDto;
import org.example.models.Follower;
import org.example.services.follower.FollowService;
import org.example.utils.JwtUtils;

import java.sql.SQLException;

public class FollowController {

    public void follow(Context ctx) throws SQLException {
        //json will have the userId that the logged user wants to follow
        FollowUserIdDto userIdToFollow = ctx.bodyAsClass(FollowUserIdDto.class);
        String username = JwtUtils.getUserUsernameFromJwt(ctx);

        FollowService.addFollower(userIdToFollow, username);
    }

    public void unfollow(Context ctx) throws SQLException {
        //json will have the userId that the logged user wants to unfollow
        Follower loggedUser = ctx.bodyAsClass(Follower.class);
        String username = JwtUtils.getUserUsernameFromJwt(ctx);

        FollowService.removeFollower(loggedUser, username);

    }
}
