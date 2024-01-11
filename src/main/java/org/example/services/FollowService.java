package org.example.services;


import org.example.models.Follower;
import org.example.repositories.FollowerRepository;
import org.example.repositories.UserRepository;

public class FollowService {

    public static void addFollower(Follower loggedUser, String username) {

        UserRepository userRepo = new UserRepository();
        Integer userId = userRepo.getUserIdByUsername(username);
        if(userId == loggedUser.getFollowingUserId()) {
            throw new RuntimeException("Cannot follow yourself");
        }
        loggedUser.setUserId(userId);

        FollowerRepository followerRepo = new FollowerRepository();
        if (followerRepo.followersRelationshipExists(loggedUser)){
            throw new RuntimeException("You are already following this user");
        }

        FollowerRepository.addFollowerRepo(loggedUser);


    }

    public static void removeFollower(Follower loggedUser, String username) {

        UserRepository userRepo = new UserRepository();
        Integer userId = userRepo.getUserIdByUsername(username);
        loggedUser.setUserId(userId);

        FollowerRepository followerRepo = new FollowerRepository();
        if (!followerRepo.followersRelationshipExists(loggedUser)){
            throw new RuntimeException("You are not following this user");
        }

        FollowerRepository.removeFollowerRepo(loggedUser);


    }
}
