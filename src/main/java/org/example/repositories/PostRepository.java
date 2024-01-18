package org.example.repositories;

import org.example.models.Paging;
import org.example.models.Post;
import org.example.utils.DbUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.sql.Timestamp;
import java.sql.*;

public class PostRepository {

    public static void insertPost(Connection connection, Post post) throws SQLException {
        String query = "INSERT INTO posts (userId, content, timestamp) VALUES (?,?,?) ";


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

    }

    public static boolean postExistsById(Connection connection, Integer postId) {
        String query = "Select postId from posts where postId = ?";

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

    }

    public static List<Post> getPostsByUserId(Connection connection, List<Integer> usersIds, Paging paging) {
        String query = "SELECT * FROM posts WHERE userId IN (" + String.join(",", Collections.nCopies(usersIds.size(), "?")) + ") Order by timestamp asc LIMIT ? OFFSET ?";
        List<Post> posts = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int usersIdsCount = usersIds.size();
            for (int i = 0; i < usersIds.size(); i++) {
                statement.setInt(i + 1, usersIds.get(i));
            }

            statement.setInt(usersIdsCount + 1, paging.getPageSize());
            statement.setInt(usersIdsCount + 2, (paging.getPage() - 1) * paging.getPageSize());

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

    }

    public static List<Integer> getPostsIdByUserId(Connection connection, Integer userId) {
        String query = "SELECT postid FROM posts WHERE userId = ?";
        List<Integer> postIds = new ArrayList<>();


        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    postIds.add(resultSet.getInt("postId"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return postIds;

    }

    public static List<Post> getPostsOfFollowingUsers(Connection connection, Integer userId, Paging paging) {
        String query = """
                Select p.postId, p.userId, p.content, p.timestamp
                from posts as p
                inner join followers as f on p.userid = f.following_user_id
                where f.user_id = ?
                Limit ? Offset ?
                """;
        List<Post> posts = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, paging.getPageSize());
            statement.setInt(3, (paging.getPage() - 1) * paging.getPageSize());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Post post = new Post();
                    post.setId(resultSet.getInt("postId"));
                    post.setUserId(resultSet.getInt("userId"));
                    post.setContent(resultSet.getString("content"));
                    post.setTimestamp(resultSet.getObject("timestamp", LocalDateTime.class));
                    posts.add(post);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }

    public static Integer getNumberOfPostsByUserId(Connection connection, Integer userId) {
        String query = "SELECT COUNT(*) FROM posts WHERE  userId = ?";

        try (var statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (var resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static Post getPostByPostId(Connection connection, Integer postId) {
        String query = "SELECT * FROM posts WHERE postId = ?";
        Post post = new Post();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, postId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    post.setId(resultSet.getInt("postId"));
                    post.setUserId(resultSet.getInt("userId"));
                    post.setContent(resultSet.getString("content"));
                    post.setTimestamp(resultSet.getTimestamp("timestamp").toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return post;
    }
}
