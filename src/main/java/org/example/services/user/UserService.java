package org.example.services.user;


import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.utils.DbUtils;
import org.example.utils.JwtUtils;

import java.sql.SQLException;
import java.util.List;

public class UserService {


    public static void registerUser(User user) throws RuntimeException {

        UserRepository userRepo = new UserRepository();
//        if (!userRepo.emailHasValidFormat(user.getUsername())) {
//            throw new RuntimeException("Email is not valid");
//        }


        try {
            DbUtils.inTransactionWithoutResult(connection -> {
                if (userRepo.userExistsByEmail(connection, user.getUsername())) {
                    throw new RuntimeException("User with current email already exists");
                }
                if (!userRepo.isValidPassword(user.getPassword())) {
                    throw new RuntimeException("Invalid password. Password must meet the specified criteria.");
                }

                userRepo.registerUser(connection, user);
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error registering user", e);
        }

    }

    public static String loginUser(User user) throws SQLException {
        User authenticatedUser = authenticateUser(user);
        if (authenticatedUser != null) {
            JwtUtils jwt = new JwtUtils();
            return jwt.generateJwt(authenticatedUser);
        } else {
            throw new RuntimeException("Authentication failed");
        }
    }

    private static User authenticateUser(User user) throws SQLException {

        UserRepository userRepo = new UserRepository();

        return DbUtils.inTransaction(connection -> {
            User storedUser = userRepo.getUserByUsername(connection, user.getUsername());
            if (storedUser != null && userRepo.verifyPassword(user.getPassword(), storedUser.getPassword())) {
                return storedUser;
            }
            return null;
        });
    }




}
