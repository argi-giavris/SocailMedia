package org.example.controllers.post;

import io.javalin.http.Context;
import org.example.dto.PostContentDto;
import org.example.dto.UserUsernameAndRoleDto;
import org.example.services.post.CreatePostService;
import org.example.utils.JwtUtils;

public class CreatePostController {

    public void post(Context ctx) {
        PostContentDto post = ctx.bodyAsClass(PostContentDto.class);
        UserUsernameAndRoleDto author = JwtUtils.getUserFromJwt(ctx);


        try {
            CreatePostService.createPost(post, author);
            ctx.status(201).json(post.getContent());
        } catch (RuntimeException e) {
            ctx.status(500).json(e.getMessage());
        }
    }
}
