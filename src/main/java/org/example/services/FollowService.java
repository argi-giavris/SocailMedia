package org.example.services;


import org.example.models.Follower;
import org.example.repositories.FollowerRepository;
import org.example.repositories.UserRepository;
import java.util.List;

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

    public static List<String> getFollowersOfUser(String username) {
        UserRepository userRepo = new UserRepository();
        Integer userId = userRepo.getUserIdByUsername(username);

        List<Integer> followersIds = FollowerRepository.getFollowersUserIds(userId);

        if (followersIds.isEmpty()) {
            throw new RuntimeException("You have no followers");
        }

        List<String> followersNames = UserRepository.getListOfUsernamesById(followersIds);

        return followersNames;
    }

    public static List<String> getFollowingOfUser(String username) {
        UserRepository userRepo = new UserRepository();
        Integer userId = userRepo.getUserIdByUsername(username);

        List<Integer> followingIds = FollowerRepository.getFollowingUserIds(userId);

        if (followingIds.isEmpty()) {
            throw new RuntimeException("You are not following any user");
        }

        List<String> followersNames = UserRepository.getListOfUsernamesById(followingIds);

        return followersNames;
    }
}
