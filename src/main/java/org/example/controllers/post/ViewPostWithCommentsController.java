package org.example.controllers.post;

import io.javalin.http.Context;
import org.example.models.CommentView;
import org.example.models.PostCommentView;
import org.example.services.post.ViewPostsWithCommentsService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ViewPostWithCommentsController {

    public void viewPostWthComments(Context ctx) throws SQLException {
        Integer postId = Integer.valueOf(Objects.requireNonNull(ctx.queryParam("postId")));

        PostCommentView postCommentView = ViewPostsWithCommentsService.getPostCommentsByPostId(postId);
        ctx.status(201).json(postCommentView);

    }

//    public static class PostWithCommentsResponseDto {
//        String postContent;
//        LocalDateTime postCreatedAt;
//        List<CommentDto> postComments;
//    }
//
//    public static class CommentDto {
//        Integer id;
//        String username;
//        String content;
//        LocalDateTime createdAt;
//    }
}
