package org.example.services.post;

import org.example.models.Comment;
import org.example.models.CommentView;
import org.example.models.Post;
import org.example.models.PostCommentView;
import org.example.repositories.PostRepository;
import org.example.repositories.CommentRepository;
import org.example.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ViewPostsWithCommentsService {
    public static List<PostCommentView> getUserPostsWithComments(String username) {
        UserRepository userRepo = new UserRepository();
        Integer userId = userRepo.getUserIdByUsername(username);

        List<Post> posts = PostRepository.getPostsByUserId(Collections.singletonList(userId));

        if (posts.isEmpty()){
            throw new RuntimeException("You have no posts yet");
        }

        List<PostCommentView> postCommentViews = new ArrayList<>();
        Integer limitComments = 100;

        for (Post post : posts) {

            List<Comment> comments = CommentRepository.getLimitedCommentsByPostId(post.getId(), limitComments);


            List<CommentView> commentViews = comments.stream()
                    .map(comment -> new CommentView(UserRepository.getUsernameById(comment.getUserId()),comment.getContent(), comment.getTimestamp()))
                    .collect(Collectors.toList());


            PostCommentView postCommentView = new PostCommentView(
                    post.getContent(),
                    post.getTimestamp(),
                    commentViews
            );

            postCommentViews.add(postCommentView);
        }

        return postCommentViews;

    }
}
