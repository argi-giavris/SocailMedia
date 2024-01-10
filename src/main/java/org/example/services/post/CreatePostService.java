package org.example.services.post;

import org.example.dto.PostContentDto;
import org.example.dto.UserUsernameAndRoleDto;
import org.example.models.Post;
import org.example.models.Role;
import org.example.repositories.PostRepository;
import org.example.repositories.UserRepository;

import java.time.LocalDateTime;


public class CreatePostService {

    public static void createPost(PostContentDto postDto, UserUsernameAndRoleDto author) {

        if (postExceededCharactersLimit(postDto.getContent(), author.getRole())) {
            throw new RuntimeException("Post length exceeded characters limit");
        }

            UserRepository userRepo = new UserRepository();
            Integer userId = userRepo.getUserIdByUsername(author.getUsername());

        LocalDateTime now = LocalDateTime.now();
        Post post = Post.fromPostContentDto(postDto, userId, now);

        PostRepository postRepo = new PostRepository();
        postRepo.insertPost(post);

    }

    private static boolean postExceededCharactersLimit(String content, Role role) {
        if ((content.length() > 1000  && String.valueOf(role).equals("FREE"))
                || (content.length() > 3000 && String.valueOf(role).equals("PREMIUM"))) {
            return true;
        }
        return false;
    }
}
