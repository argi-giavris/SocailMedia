package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.CommentContentAndPostDto;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    Integer id;
    Integer postId;
    Integer userId;
    String content;
    LocalDateTime timestamp;

    public static Comment fromCommentContentAndPostDto(CommentContentAndPostDto commentContentAndPostDto,  Integer userId, LocalDateTime now)   {
        final Comment comment = new Comment();
        comment.id = null;
        comment.postId = commentContentAndPostDto.getPostId();
        comment.userId = userId;
        comment.content = commentContentAndPostDto.getContent();
        comment.timestamp = now;
        return comment;
    }
}
