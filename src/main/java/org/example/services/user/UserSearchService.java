package org.example.services.user;

import org.example.models.Paging;
import org.example.repositories.UserRepository;
import org.example.utils.DbUtils;

import java.sql.SQLException;
import java.util.List;

public class UserSearchService {

    public static List<String> searchUsersWithSearchTerm(String searchTerm, Paging paging) throws SQLException {

        return DbUtils.inTransaction(connection -> {
            List<String> matchingUsernames = UserRepository.searchUsers(connection, searchTerm, paging);

            if (matchingUsernames.isEmpty()) {
                throw new RuntimeException("No matching users found");
            }
            return matchingUsernames;
        });

    }
}
