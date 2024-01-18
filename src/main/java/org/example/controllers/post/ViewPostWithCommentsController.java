package org.example.controllers.post;

import io.javalin.http.Context;
import org.example.models.PostCommentView;
import org.example.services.post.ViewPostsWithCommentsService;

import java.util.List;

public class ViewPostWithCommentsController {

    public void viewPostWthComments(Context ctx) {
        Integer postId = Integer.valueOf(ctx.queryParam("postId"));
        try {
            PostCommentView postCommentView = ViewPostsWithCommentsService.getPostCommentsByPostId(postId);
            ctx.status(201).json(postCommentView);
        } catch (RuntimeException e) {
            ctx.status(500).json(e.getMessage());
        }
    }
}
