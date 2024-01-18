package org.example.repositories;

import org.example.models.Follower;
import org.example.models.Paging;
import org.example.utils.DbUtils;

import java.net.ConnectException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FollowerRepository {

    public static void addFollowerRepo(Follower user) {

        String query = "Insert into followers (user_id, following_user_id) Values (?, ?)";

        try {
            int rowsAffected = DbUtils.inTransaction(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    statement.setInt(1, user.getFollowerUserId());
                    statement.setInt(2, user.getUserIdToFollow());
                    return statement.executeUpdate(); // Returns the number of rows affected
                }
            });

            if (rowsAffected > 0) {
                System.out.println("Insertion was successful.");
            } else {
                System.out.println("Insertion failed or no rows were affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeFollowerRepo(Follower user) {

        String query = "Delete from followers where user_id = ? and following_user_id = ?";

        try {
            int rowsAffected = DbUtils.inTransaction(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    statement.setInt(1, user.getFollowerUserId());
                    statement.setInt(2, user.getUserIdToFollow());
                    return statement.executeUpdate(); // Returns the number of rows affected
                }
            });

            if (rowsAffected > 0) {
                System.out.println("Delete was successful.");
            } else {
                System.out.println("Delete failed or no rows were affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> getFollowingUserIds(Connection connection, Integer userId, Paging paging) {
        String query = "Select following_user_id from followers where user_id = ? LIMIT ? OFFSET ?";

        List<Integer> followingIds = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, paging.getPageSize());
            statement.setInt(3, (paging.getPage() - 1) * paging.getPageSize());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    followingIds.add(resultSet.getInt("following_user_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return followingIds;

    }

    public static List<Integer> getFollowersUserIds(Connection connection, Integer userId, Paging paging) {
        String query = "Select user_id from followers where following_user_id = ? LIMIT ? OFFSET ?";

        List<Integer> followerIds = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, paging.getPageSize());
            statement.setInt(3, (paging.getPage() - 1) * paging.getPageSize());


            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    followerIds.add(resultSet.getInt("user_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return followerIds;
    }

    public static Integer countOfFollowingUsers(Connection connection, Integer userId) {
        String query = "Select COUNT(*) FROM followers WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public boolean followersRelationshipExists(Integer followerUserId, Integer userIdToFollow) {
        String query = "Select user_id from followers where user_id = ? and following_user_id = ?";

        try {
            return DbUtils.inTransaction(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, followerUserId);
                    statement.setInt(2, userIdToFollow);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        return resultSet.next();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
