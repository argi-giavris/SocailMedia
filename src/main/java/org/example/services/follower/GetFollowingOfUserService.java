package org.example.services.follower;

import org.example.models.Paging;
import org.example.repositories.FollowerRepository;
import org.example.repositories.UserRepository;
import org.example.utils.DbUtils;

import java.sql.SQLException;
import java.util.List;

public class GetFollowingOfUserService {

    public static List<String> getFollowingOfUser(String username, Paging paging) {
        try {
            return DbUtils.inTransaction(connection -> {
                UserRepository userRepo = new UserRepository();
                Integer userId = userRepo.getUserIdByUsername(connection, username);

                List<Integer> followingIds = FollowerRepository.getFollowingUserIds(connection, userId, paging);

                if (followingIds.isEmpty()) {
                    throw new RuntimeException("You are not following any user");
                }

                return UserRepository.getListOfUsernamesById(connection, followingIds);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
