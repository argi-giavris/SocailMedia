package org.example.controllers.post;

import io.javalin.http.Context;
import io.javalin.validation.Validator;
import org.example.models.Paging;
import org.example.models.PostView;
import org.example.services.post.ViewPostService;
import org.example.utils.JwtUtils;



import java.util.List;

public class ViewPostController {

    public void viewFollowersPosts(Context ctx){

        String username = JwtUtils.getUserUsernameFromJwt(ctx);
        Paging paging = Paging.fromContext(ctx);
        try {
            List<PostView> viewPosts = ViewPostService.getFollowersViewPosts(username,paging);
            ctx.status(201).json(viewPosts);
        }catch (RuntimeException e) {
            ctx.status(500).json(e.getMessage());
        }
    }
}
