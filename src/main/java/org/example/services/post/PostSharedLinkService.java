package org.example.services.post;

import org.example.models.PostSharedLink;
import org.example.repositories.PostRepository;
import org.example.utils.*;

import java.sql.SQLException;

public class PostSharedLinkService {

    public static PostSharedLink generatePostSharedLink(Integer postId) {

        try{
            return DbUtils.inTransaction(connection -> {
                if(!PostRepository.postExistsById(connection, postId)) {
                    throw new RuntimeException("Post not found");
                }

                return PostSharedLink.generateLink( postId);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
