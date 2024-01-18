package org.example.services.comment;

import org.example.dto.CommentContentAndPostDto;
import org.example.dto.UserUsernameAndRoleDto;
import org.example.models.*;
import org.example.repositories.CommentRepository;
import org.example.repositories.PostRepository;
import org.example.repositories.UserRepository;
import org.example.utils.DbUtils;
import org.example.utils.TimeConfig;

import java.sql.SQLException;
import java.time.LocalDateTime;


public class CreateCommentService {

    public static void createComment(CommentContentAndPostDto commentDto, UserUsernameAndRoleDto commentator) throws SQLException {

        DbUtils.inTransactionWithoutResult(connection -> {
            UserRepository userRepo = new UserRepository();
            CommentRepository commentRepo = new CommentRepository();
            PostRepository postRepo = new PostRepository();

            Integer userId = userRepo.getUserIdByUsername(connection, commentator.getUsername());

            if (!postRepo.postExistsById(connection, commentDto.getPostId())) {
                throw new RuntimeException("Post id does not exist");
            }

            Integer numberOfCommentsInPostFromUser = commentRepo.getNumberOfCommentsPerPost(connection, commentDto.getPostId(), userId);

            if (String.valueOf(commentator.getRole()).equals("FREE") && numberOfCommentsInPostFromUser > 4) {
                throw new RuntimeException("Free users can comment up to 5 times per post");
            }

            LocalDateTime now = TimeConfig.getTime();
            Comment comment = Comment.fromCommentContentAndPostDto(commentDto, userId, now);

            commentRepo.insertComment(connection, comment);
        });

    }


}
