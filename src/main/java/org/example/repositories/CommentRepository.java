package org.example.repositories;

import org.example.models.Comment;
import org.example.utils.DbUtils;

import java.sql.*;

public class CommentRepository {


    public static void insertComment(Comment comment) {

        String query ="INSERT INTO comments (postId, userId, content, timestamp) VALUES (?, ?, ?, ?) ";

        try {
                DbUtils.inTransactionWithoutResult(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    statement.setInt(1, comment.getPostId());
                    statement.setInt(2, comment.getUserId());
                    statement.setString(3, comment.getContent());
                    statement.setTimestamp(4, Timestamp.valueOf(comment.getTimestamp()));
                    statement.executeUpdate();

                    try (ResultSet keys = statement.getGeneratedKeys()) {
                        if (keys.next()) {
                            comment.setId(keys.getInt(1));
                        }
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Integer getNumberOfCommentsPerPost(Integer postId, Integer userId) {
        String query = "SELECT COUNT(*) FROM comments WHERE postId = ? and userId = ?";
        try (Connection connection = DbUtils.dataSource.getConnection()) {
            try (var statement = connection.prepareStatement(query)) {
                statement.setInt(1, postId);
                statement.setInt(2, userId);
                try (var resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return resultSet.getInt(1) ;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
