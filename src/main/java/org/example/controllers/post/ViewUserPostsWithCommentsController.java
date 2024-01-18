package org.example.controllers.post;

import io.javalin.http.Context;
import org.example.models.Paging;
import org.example.models.PostCommentView;
import org.example.services.post.ViewPostsWithCommentsService;
import org.example.utils.JwtUtils;

import java.util.List;

public class ViewUserPostsWithCommentsController {

    public void viewUserPostsWithComments(Context ctx) {
        String username = JwtUtils.getUserUsernameFromJwt(ctx);
        Paging paging = Paging.fromContext(ctx);
        try {
            List<PostCommentView> postCommentViews = ViewPostsWithCommentsService.getUserPostsWithComments(username, paging);
            ctx.status(201).json(postCommentViews);
        } catch (RuntimeException e) {
            ctx.status(500).json(e.getMessage());
        }
    }
}
