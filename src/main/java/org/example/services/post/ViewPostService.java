package org.example.services.post;

import org.example.models.Paging;
import org.example.models.Post;
import org.example.models.PostView;
import org.example.repositories.FollowerRepository;
import org.example.repositories.PostRepository;
import org.example.repositories.UserRepository;
import org.example.utils.DbUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ViewPostService {

    public static List<PostView> getFollowersViewPosts(String username, Paging paging) throws SQLException {

        return DbUtils.inTransaction(connection -> {
            Integer userId = UserRepository.getUserIdByUsername(connection, username);

            if (FollowerRepository.countOfFollowingUsers(connection, userId) == 0) {
                throw new RuntimeException("You are not following anyone");
            }

            List<Post> posts = PostRepository.getPostsOfFollowingUsers(connection, userId, paging);

            if (posts.isEmpty()) {
                throw new RuntimeException("Following Users have no posts yet");
            }

            List<PostView> postViews = new ArrayList<>();
            for (Post post : posts) {
                String postUsername = UserRepository.getUsernameById(connection, post.getUserId());
                postViews.add(new PostView(postUsername, post.getContent(), post.getTimestamp()));
            }

            return postViews;
        });


    }
}
