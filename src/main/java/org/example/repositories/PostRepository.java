package org.example.repositories;

import org.example.models.Post;
import org.example.utils.DbUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static List<Post> getPostsById(List<Integer> usersIds) {
        String query = "SELECT * FROM posts WHERE userId IN (" + String.join(",", Collections.nCopies(usersIds.size(), "?")) + ") Order by timestamp asc";
        List<Post> posts = new ArrayList<>();

        try {
            return DbUtils.inTransaction(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    for (int i = 0; i < usersIds.size(); i++) {
                        statement.setInt(i + 1, usersIds.get(i));
                    }

                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            Post post = new Post();
                            post.setId(resultSet.getInt("postId"));
                            post.setUserId(resultSet.getInt("userId"));
                            post.setContent(resultSet.getString("content"));
                            post.setTimestamp(resultSet.getTimestamp("timestamp").toLocalDateTime());

                            posts.add(post);
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                return posts;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
