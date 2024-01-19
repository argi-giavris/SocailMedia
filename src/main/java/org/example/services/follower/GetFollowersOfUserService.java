package org.example.services.follower;

import org.example.models.Paging;
import org.example.repositories.FollowerRepository;
import org.example.repositories.UserRepository;
import org.example.utils.DbUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GetFollowersOfUserService {

    public static List<String> getFollowersOfUser(String username, Paging paging) throws SQLException {

        return DbUtils.inTransaction(connection -> {
            UserRepository userRepo = new UserRepository();
            Integer userId = userRepo.getUserIdByUsername(connection, username);

            List<Integer> followersIds = FollowerRepository.getFollowersUserIds(connection, userId, paging);

            if (followersIds.isEmpty()) {
                throw new RuntimeException("You have no followers");
            }

            List<String> followersNames = UserRepository.getListOfUsernamesById(connection, followersIds);

            return followersNames;
        });

    }
}
