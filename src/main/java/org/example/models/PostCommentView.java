package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentView {
    String postContent;
    LocalDateTime postCreatedAt;
    List<CommentView> postComments;
}
