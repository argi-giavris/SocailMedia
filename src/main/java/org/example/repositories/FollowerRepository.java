package org.example.repositories;

import org.example.models.Follower;
import org.example.utils.DbUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FollowerRepository {

    public boolean followersRelationshipExists(Follower loggedUser) {
        String query = "Select user_id from followers where user_id = ? and following_user_id = ?";

        try {
            return DbUtils.inTransaction(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, loggedUser.getUserId());
                    statement.setInt(2, loggedUser.getFollowingUserId());

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

    public static void addFollowerRepo(Follower user) {

        String query = "Insert into followers (user_id, following_user_id) Values (?, ?)";

        try {
            int rowsAffected = DbUtils.inTransaction(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    statement.setInt(1, user.getUserId());
                    statement.setInt(2, user.getFollowingUserId());
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
                    statement.setInt(1, user.getUserId());
                    statement.setInt(2, user.getFollowingUserId());
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
}
