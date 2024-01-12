package org.example.services;

import org.example.dto.CommentContentAndPostDto;
import org.example.dto.UserUsernameAndRoleDto;
import org.example.models.*;
import org.example.repositories.CommentRepository;
import org.example.repositories.PostRepository;
import org.example.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentService {

    public static void createComment(CommentContentAndPostDto commentDto, UserUsernameAndRoleDto commentator) {

        UserRepository userRepo = new UserRepository();
        Integer userId = userRepo.getUserIdByUsername(commentator.getUsername());

        CommentRepository commentRepo = new CommentRepository();
        PostRepository postRepo = new PostRepository();
        if (!postRepo.postExistsById(commentDto.getPostId())) {
            throw new RuntimeException("Post id does not exists");
        }
        Integer numberOfCommentsInPostFromUser = commentRepo.getNumberOfCommentsPerPost(commentDto.getPostId(), userId);

        if (String.valueOf(commentator.getRole()).equals("FREE") && numberOfCommentsInPostFromUser > 4) {
            throw new RuntimeException("Free users can comment up to 5 times per post");
        }

        LocalDateTime now = LocalDateTime.now();
        Comment comment = Comment.fromCommentContentAndPostDto(commentDto, userId, now);

        commentRepo.insertComment(comment);

    }

    public static List<CommentView> getCommentsOfOwnPost(String username) {
        UserRepository userRepo = new UserRepository();
        Integer userId = userRepo.getUserIdByUsername(username);

        List<Integer> postIds = PostRepository.getPostsIdByUserId(userId);

        if (postIds.isEmpty()) {
            throw new RuntimeException("You have no posts yet");
        }

        List<CommentView> commentViews = new ArrayList<>();

        for (Integer postId : postIds) {
            List<Comment> comments = CommentRepository.getAllCommentsByPostId(postId);

            List<CommentView> postCommentViews = comments.stream()
                    .map(comment -> new CommentView(
                            UserRepository.getUsernameById(comment.getUserId()),
                            comment.getContent(),
                            comment.getTimestamp()
                    ))
                    .toList();

            commentViews.addAll(postCommentViews);
        }
        return commentViews;
    }
}
