package org.example.controllers.post;

import io.javalin.http.Context;
import org.example.models.Paging;
import org.example.models.PostCommentView;
import org.example.services.post.ViewPostsWithCommentsService;
import org.example.utils.JwtUtils;

import java.sql.SQLException;
import java.util.List;

public class ViewUserPostsWithCommentsController {

    public void viewUserPostsWithComments(Context ctx) throws SQLException {
        String username = JwtUtils.getUserUsernameFromJwt(ctx);
        Paging paging = Paging.fromContext(ctx);

        List<PostCommentView> postCommentViews = ViewPostsWithCommentsService.getUserPostsWithComments(username, paging);
        ctx.status(201).json(postCommentViews);

    }
}
