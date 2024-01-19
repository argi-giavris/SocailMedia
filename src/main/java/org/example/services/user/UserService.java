package org.example.services.user;


import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.utils.DbUtils;
import org.example.utils.JwtUtils;

import java.sql.SQLException;
import java.util.List;

public class UserService {


    public static void registerUser(User user) throws RuntimeException, SQLException {

        DbUtils.inTransactionWithoutResult(connection -> {
            if (UserRepository.userExistsByEmail(connection, user.getUsername())) {
                throw new RuntimeException("User with current email already exists");
            }
            if (!UserRepository.isValidPassword(user.getPassword())) {
                throw new RuntimeException("Invalid password. Password must meet the specified criteria.");
            }

            UserRepository.registerUser(connection, user);
        });


    }

    public static String loginUser(User user) throws SQLException {
        User authenticatedUser = authenticateUser(user);
        if (authenticatedUser != null) {
            return JwtUtils.generateJwt(authenticatedUser);
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
