package org.example.controllers.post;

import io.javalin.http.Context;
import org.example.models.PostSharedLink;
import org.example.services.post.PostSharedLinkService;

import java.sql.SQLException;
import java.util.Objects;

public class GetPostSharedLinkController {

    public void getSharedLinkOfPost(Context ctx) throws SQLException {

        Integer postId = Integer.valueOf(Objects.requireNonNull(ctx.queryParam("postId")));
        PostSharedLink postSharedLink = PostSharedLinkService.generatePostSharedLink(postId);
        ctx.status(201).json(postSharedLink);


    }
}
