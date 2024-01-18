package org.example.controllers.comment;

import io.javalin.http.Context;
import org.example.models.CommentView;
import org.example.services.comment.ViewCommentsOfPostsService;
import org.example.utils.JwtUtils;

import java.util.List;

public class ViewCommentsOfPostsController {

//    public void viewCommentsOfPosts(Context ctx) {
//        String username = JwtUtils.getUserUsernameFromJwt(ctx);
//        try {
//            List<CommentView> viewComments = ViewCommentsOfPostsService.getCommentsOfPosts(username);
//            ctx.status(201).json(viewComments);
//        }catch (RuntimeException e) {
//            ctx.status(500).json(e.getMessage());
//        }
//    }
}
