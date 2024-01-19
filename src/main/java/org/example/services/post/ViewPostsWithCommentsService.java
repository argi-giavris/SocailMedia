package org.example.services.post;

import org.example.models.*;
import org.example.repositories.PostRepository;
import org.example.repositories.CommentRepository;
import org.example.repositories.UserRepository;
import org.example.utils.DbUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ViewPostsWithCommentsService {
    public static List<PostCommentView> getUserPostsWithComments(String username, Paging paging) throws SQLException {


        return DbUtils.inTransaction(connection -> {
            Integer userId = UserRepository.getUserIdByUsername(connection, username);

            List<Post> posts = PostRepository.getPostsByUserId(connection, Collections.singletonList(userId), paging);

            if (posts.isEmpty()) {
                throw new RuntimeException("You have no posts yet");
            }

            List<PostCommentView> postCommentViews = new ArrayList<>();
            Integer limitComments = 100;

            for (Post post : posts) {

                PostCommentView postCommentView = getPostCommentView(connection, post, limitComments);
                postCommentViews.add(postCommentView);
            }

            return postCommentViews;
        });


    }

    public static PostCommentView getPostCommentsByPostId(Integer postId) throws SQLException {

        return DbUtils.inTransaction(connection -> {

            if (!PostRepository.postExistsById(connection, postId)) {
                throw new RuntimeException("Post not found");
            }

            Post post = PostRepository.getPostByPostId(connection, postId);
            PostCommentView postCommentView = getPostCommentView(connection, post, 100);
            return postCommentView;
        });

    }

    private static PostCommentView getPostCommentView(Connection connection, Post post, Integer limit) {
        List<Comment> comments = CommentRepository.getLimitedCommentsByPostId(connection, post.getId(), limit);

        List<CommentView> commentViews = comments.stream()
                .map(comment -> new CommentView(
                        comment.getPostId(),
                        UserRepository.getUsernameById(connection, comment.getUserId()),
                        comment.getContent(), comment.getTimestamp()))
                .collect(Collectors.toList());


        PostCommentView postCommentView = new PostCommentView(
                post.getContent(),
                post.getTimestamp(),
                commentViews
        );
        return postCommentView;
    }
}
