package org.example.repositories;

import org.example.models.Paging;
import org.example.models.Role;
import org.example.models.User;
import org.example.utils.DbUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserRepository {
    public static void registerUser(Connection connection, User user) throws SQLException {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, hashPassword(user.getPassword()));
            statement.setString(3, String.valueOf(user.getRole()));
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getInt(1));
                }
            }
        }
    }

    public boolean userExistsByEmail(Connection connection, String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (var resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) == 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isValidPassword(String password) {
        if (password.length() < 4) {
            return false;
        }

//        if (!password.matches(".*[A-Z].*")) {
//            return false;
//        }
//
//        if (!password.matches(".*[a-z].*")) {
//            return false;
//        }
//
//        if (!password.matches(".*\\d.*")) {
//            return false;
//        }
//
//        if (!password.matches(".*[!@#$%^&*()-_=+\\\\|[{]};:'\",<.>/?].*")) {
//            return false;
//        }

        return true;
    }


    /*
     Allows numeric values from 0 to 9.
     Allow both uppercase and lowercase letters from a to z.
     Hyphen “-” and dot “.” aren’t allowed at the start and end of the domain part.
     No consecutive dots.
     */

    private static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    public User getUserByUsername(Connection connection, String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRole(Role.valueOf(resultSet.getString("role")));

                    return user;
                } else {
                    // User not found
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer getUserIdByUsername(Connection connection, String username) {
        String query = "SELECT id FROM users WHERE username = ?";
        final Integer[] userId = {null};

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    userId[0] = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userId[0];
    }

    public static String getUsernameById(Connection connection, Integer userId) {
        String query = "SELECT username FROM users WHERE id = ?";
        final String[] username = new String[1];

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    username[0] = resultSet.getString("username");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return username[0];
    }

    public static List<String> getListOfUsernamesById(Connection connection, List<Integer> userIds) {
        String query = "SELECT username FROM users WHERE id IN (" +
                userIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")) +
                ")";

        List<String> usernames = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    usernames.add(resultSet.getString("username"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usernames;
    }

    public static List<String> searchUsers(Connection connection, String searchTerm, Paging paging) {
        String query = "SELECT username FROM users WHERE username LIKE ? Limit ? Offset ?";

        List<String> matchingUsernames = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + searchTerm + "%");
            statement.setInt(2, paging.getPageSize());
            statement.setInt(3, (paging.getPage() - 1) * paging.getPageSize());


            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    matchingUsernames.add(resultSet.getString("username"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return matchingUsernames;

    }


}
