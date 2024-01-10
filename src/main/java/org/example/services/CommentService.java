package org.example.services;

import org.example.dto.CommentContentAndPostDto;
import org.example.dto.UserUsernameAndRoleDto;
import org.example.models.Comment;
import org.example.models.User;
import org.example.repositories.CommentRepository;
import org.example.repositories.PostRepository;
import org.example.repositories.UserRepository;

import java.time.LocalDateTime;

public class CommentService {

    public static void createComment(CommentContentAndPostDto commentDto, UserUsernameAndRoleDto commentator) {

        UserRepository userRepo = new UserRepository();
        Integer userId = userRepo.getUserIdByUsername(commentator.getUsername());

        CommentRepository commentRepo = new CommentRepository();
        PostRepository postRepo = new PostRepository();
        if(!postRepo.postExistsById(commentDto.getPostId())){
            throw new RuntimeException("Post id does not exists");
        }
        Integer numberOfCommentsInPostFromUser = commentRepo.getNumberOfCommentsPerPost(commentDto.getPostId(), userId);

        if(String.valueOf(commentator.getRole()).equals("FREE") && numberOfCommentsInPostFromUser > 4) {
            throw new RuntimeException("Free users can comment up to 5 times per post");
        }

        LocalDateTime now = LocalDateTime.now();
        Comment comment = Comment.fromCommentContentAndPostDto(commentDto,userId,now);

        commentRepo.insertComment(comment);

    }
}
