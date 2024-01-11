package org.example.repositories;

import org.example.models.Role;
import org.example.models.User;
import org.example.utils.DbUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class UserRepository {
    public static void registerUser(User user) {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try {
            DbUtils.inTransactionWithoutResult(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, user.getUsername());
                    statement.setString(2,  hashPassword(user.getPassword()));
                    statement.setString(3, String.valueOf(user.getRole()));
                    statement.executeUpdate();

                    try (ResultSet keys = statement.getGeneratedKeys()) {
                        if (keys.next()) {
                            user.setId(keys.getInt(1));
                        }
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean userExistsByEmail(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection connection = DbUtils.dataSource.getConnection()) {
            try (var statement = connection.prepareStatement(query)) {
                statement.setString(1, email);
                try (var resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return resultSet.getInt(1) == 1;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
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
    public boolean emailHasValidFormat(String email){
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    private static String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    public User getUserByUsername(String username){
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = DbUtils.dataSource.getConnection()) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getUserIdByUsername(String username) {
        String query = "SELECT id FROM users WHERE username = ?";
        final Integer[] userId = {null};

        try {
            DbUtils.inTransactionWithoutResult(connection -> {
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
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userId[0];
    }

    public String getUsernameById(Integer userId) {
        String query = "SELECT username FROM users WHERE id = ?";
        final String[] username = new String[1];

        try {
            DbUtils.inTransactionWithoutResult(connection -> {
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
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return username[0];
    }




}
