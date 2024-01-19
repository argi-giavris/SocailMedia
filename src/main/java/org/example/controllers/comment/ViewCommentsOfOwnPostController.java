package org.example.controllers.comment;

import io.javalin.http.Context;
import org.example.models.CommentView;
import org.example.models.Paging;
import org.example.services.comment.ViewCommentsOfOwnPostService;
import org.example.utils.JwtUtils;

import java.sql.SQLException;
import java.util.List;

public class ViewCommentsOfOwnPostController {

    public void viewCommentsOfOwnPost(Context ctx) throws SQLException {
        String username = JwtUtils.getUserUsernameFromJwt(ctx);
        Paging paging = Paging.fromContext(ctx);

        List<CommentView> viewComments = ViewCommentsOfOwnPostService.getCommentsOfOwnPosts(username, paging);
        ctx.status(201).json(viewComments);

    }
}
