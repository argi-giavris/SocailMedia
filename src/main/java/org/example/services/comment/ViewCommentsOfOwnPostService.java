package org.example.services.comment;

import org.example.models.Comment;
import org.example.models.CommentView;
import org.example.models.Paging;
import org.example.repositories.CommentRepository;
import org.example.repositories.PostRepository;
import org.example.repositories.UserRepository;
import org.example.utils.DbUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ViewCommentsOfOwnPostService {

    public static List<CommentView> getCommentsOfOwnPosts(String username, Paging paging) {
        try {
            return DbUtils.inTransaction(connection -> {
                UserRepository userRepo = new UserRepository();
                Integer userId = userRepo.getUserIdByUsername(connection, username);

                if (PostRepository.getNumberOfPostsByUserId(connection, userId) < 1) {
                    throw new RuntimeException("You have no posts yet");
                }

                List<CommentView> commentViews = new ArrayList<>();

                List<Comment> comments = CommentRepository.getPaginatedCommentsOfUsersOwnPosts(connection, userId, paging);

                List<CommentView> postCommentViews = comments.stream()
                        .map(comment -> new CommentView(
                                comment.getPostId(),
                                UserRepository.getUsernameById(connection, comment.getUserId()),
                                comment.getContent(),
                                comment.getTimestamp()
                        ))
                        .toList();

                commentViews.addAll(postCommentViews);


                return commentViews;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
