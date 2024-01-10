package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.PostContentDto;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    Integer id;
    Integer userId;
    String content;
    LocalDateTime timestamp;

    public static Post fromPostContentDto(PostContentDto postContent, Integer userId, LocalDateTime now) {
        final Post post = new Post();
        post.id = null;
        post.content = postContent.getContent();
        post.userId = userId;
        post.timestamp = now;
        return post;
    }

}
