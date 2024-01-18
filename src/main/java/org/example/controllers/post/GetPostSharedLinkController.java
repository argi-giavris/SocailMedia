package org.example.controllers.post;

import io.javalin.http.Context;
import org.example.models.PostSharedLink;
import org.example.services.post.PostSharedLinkService;

public class GetPostSharedLinkController {

    public void getSharedLinkOfPost(Context ctx) {
        try {
            Integer postId = Integer.valueOf(ctx.queryParam("postId"));
            PostSharedLink postSharedLink = PostSharedLinkService.generatePostSharedLink(postId);
            ctx.status(201).json(postSharedLink);
        } catch (NumberFormatException e) {
            // Handle case where postId is not a valid integer
            ctx.status(500).json(e.getMessage());
        }

    }
}
