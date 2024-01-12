package org.example.controllers.comment;

import io.javalin.http.Context;
import org.example.models.CommentView;
import org.example.services.CommentService;
import org.example.utils.JwtUtils;

import java.util.List;

public class ViewCommentsOfOwnPostController {

    public void viewCommentsOfOwnPost(Context ctx) {
        String username = JwtUtils.getUserUsernameFromJwt(ctx);
        try {
            List<CommentView> viewComments = CommentService.getCommentsOfOwnPost(username);
            ctx.status(201).json(viewComments);
        }catch (RuntimeException e) {
            ctx.status(500).json(e.getMessage());
        }
    }
}
