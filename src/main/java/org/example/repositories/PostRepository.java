package org.example.repositories;

import org.example.dto.UserUsernameAndRoleDto;
import org.example.models.Post;
import org.example.models.User;
import org.example.utils.DbUtils;

import java.sql.Timestamp;
import java.sql.*;

public class PostRepository {

    public static void insertPost(Post post) {
        String query = "INSERT INTO posts (userId, content, timestamp) VALUES (?,?,?) ";

        try {
            DbUtils.inTransactionWithoutResult(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    statement.setInt(1, post.getId());
                    statement.setString(2, post.getContent());
                    statement.setTimestamp(3, Timestamp.valueOf(post.getTimestamp()));
                    statement.executeUpdate();

                    try (ResultSet keys = statement.getGeneratedKeys()) {
                        if (keys.next()) {
                            post.setId(keys.getInt(1));
                        }
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean postExistsById(Integer postId) {
        String query = "Select postId from posts where postId = ?";

        try {
            return DbUtils.inTransaction(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, postId);

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
