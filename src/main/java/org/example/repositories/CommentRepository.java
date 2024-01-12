package org.example.repositories;

import org.example.models.Comment;
import org.example.models.Post;
import org.example.utils.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentRepository {


    public static void insertComment(Comment comment) {

        String query = "INSERT INTO comments (postId, userId, content, timestamp) VALUES (?, ?, ?, ?) ";

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
                    return resultSet.getInt(1);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Comment> getLimitedCommentsByPostId(Integer postId, Integer limit) {
        String query = "SELECT * FROM comments WHERE postId = ? Order by timestamp asc limit ?";
        List<Comment> comments = new ArrayList<>();

        try {
            return DbUtils.inTransaction(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(query)) {

                    statement.setInt(1, postId);
                    statement.setInt(2, limit);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            Comment comment = new Comment();
                            comment.setPostId(resultSet.getInt("postId"));
                            comment.setUserId(resultSet.getInt("userId"));
                            comment.setContent(resultSet.getString("content"));
                            comment.setTimestamp(resultSet.getTimestamp("timestamp").toLocalDateTime());

                            comments.add(comment);
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                return comments;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Comment> getAllCommentsByPostId(Integer postId) {
        String query = "SELECT * FROM comments WHERE postId = ? Order by timestamp asc";
        List<Comment> comments = new ArrayList<>();

        try {
            return DbUtils.inTransaction(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(query)) {

                    statement.setInt(1, postId);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            Comment comment = new Comment();
                            comment.setPostId(resultSet.getInt("postId"));
                            comment.setUserId(resultSet.getInt("userId"));
                            comment.setContent(resultSet.getString("content"));
                            comment.setTimestamp(resultSet.getTimestamp("timestamp").toLocalDateTime());

                            comments.add(comment);
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                return comments;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
