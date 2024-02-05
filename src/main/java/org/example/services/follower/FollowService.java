package org.example.services.follower;


import org.example.dto.FollowUserIdDto;
import org.example.models.Follower;
import org.example.repositories.FollowerRepository;
import org.example.repositories.UserRepository;
import org.example.utils.DbUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class FollowService {

    public static void addFollower(FollowUserIdDto dto, String username) throws SQLException {

        DbUtils.inTransactionWithoutResult(connection -> {
            Integer followerUserId = findLoggedUserId(username, connection);

            FollowerRepository followerRepo = new FollowerRepository();

            validateFollowers(dto, followerUserId, followerRepo);

            Follower follower = Follower.newFollowersRelationship(followerUserId, dto.getUserIdToFollow());
            FollowerRepository.addFollowerRepo(follower);
        });


    }

    private static void validateFollowers(FollowUserIdDto dto, Integer followerUserId, FollowerRepository followerRepo) {
        if (followerUserId.equals(dto.getUserIdToFollow())) {
            throw new RuntimeException("Cannot follow yourself");
        }

        if (followerRepo.followersRelationshipExists(followerUserId, dto.getUserIdToFollow())) {
            throw new RuntimeException("You are already following this user");
        }
    }

    public static void removeFollower(Follower loggedUser, String username) throws SQLException {


        DbUtils.inTransactionWithoutResult(connection -> {
            Integer followerUserId = findLoggedUserId(username, connection);


            FollowerRepository followerRepo = new FollowerRepository();
            if (!followerRepo.followersRelationshipExists(followerUserId, loggedUser.getUserIdToFollow())) {
                throw new RuntimeException("You are not following this user");
            }

            FollowerRepository.removeFollowerRepo(loggedUser);
        });

    }

    private static Integer findLoggedUserId(String username, Connection connection) {
        UserRepository userRepo = new UserRepository();

        return userRepo.getUserIdByUsername(connection, username);
    }


}
