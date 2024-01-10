package org.example.controllers.comment;

import io.javalin.http.Context;
import org.example.dto.CommentContentAndPostDto;
import org.example.dto.UserUsernameAndRoleDto;
import org.example.services.CommentService;
import org.example.utils.JwtUtils;



public class CreateCommentController {

    public void comment(Context ctx) {
        CommentContentAndPostDto comment = ctx.bodyAsClass(CommentContentAndPostDto.class);
        UserUsernameAndRoleDto commentator = JwtUtils.getUserFromJwt(ctx);

        try {
            CommentService.createComment(comment, commentator);
            ctx.status(201).json(comment.getContent());
        } catch (RuntimeException e) {
            ctx.status(500).json(e.getMessage());
        }
    }
}
