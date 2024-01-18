package org.example.repositories;

import org.example.models.Comment;
import org.example.models.Paging;
import org.example.models.Post;
import org.example.utils.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentRepository {


    public static void insertComment(Connection connection, Comment comment) throws SQLException {

        String query = "INSERT INTO comments (postId, userId, content, timestamp) VALUES (?, ?, ?, ?) ";


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

    }

    public Integer getNumberOfCommentsPerPost(Connection connection, Integer postId, Integer userId) {
        String query = "SELECT COUNT(*) FROM comments WHERE postId = ? and userId = ?";

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

    }

    public static List<Comment> getLimitedCommentsByPostId(Connection connection, Integer postId, Integer limit) {
        String query = "SELECT * FROM comments WHERE postId = ? Order by timestamp asc limit ?";
        List<Comment> comments = new ArrayList<>();


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

    }

    public static List<Comment> getPaginatedCommentsOfUsersOwnPosts(Connection connection, Integer userId, Paging paging) {
        String query = """
                select c.postid, c.userId, c.content, c.timestamp
                from comments as c
                inner join posts as p on p.postid = c.postid
                where p.userid = ?
                Limit ? Offset ?
                """;
        List<Comment> comments = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setInt(2, paging.getPageSize());
            statement.setInt(3, (paging.getPage() - 1) * paging.getPageSize());

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
    }
}
