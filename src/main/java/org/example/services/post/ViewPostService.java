package org.example.services.post;

import org.example.models.Post;
import org.example.models.PostView;
import org.example.repositories.FollowerRepository;
import org.example.repositories.PostRepository;
import org.example.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;


public class ViewPostService {

    public static List<PostView> getFollowersViewPosts(String username) {
        UserRepository userRepo = new UserRepository();
        Integer userId = userRepo.getUserIdByUsername(username);

        List<Integer> followersIds = FollowerRepository.getFollowingUserIds(userId);
        if (followersIds.isEmpty()) {
            throw new RuntimeException("You are not following anyone");
        }

        List<Post> posts = PostRepository.getPostsByUserId(followersIds);

        if (posts.isEmpty()){
            throw new RuntimeException("Following Users have no posts yet");
        }

        List<PostView> postViews = new ArrayList<>();
        for (Post post : posts) {
            String postUsername = userRepo.getUsernameById(post.getUserId());
            postViews.add(new PostView(postUsername, post.getContent(), post.getTimestamp()));
        }

        return postViews;

    }
}
