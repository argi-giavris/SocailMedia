package org.example.utils;

import io.javalin.Javalin;
import org.example.controllers.comment.CreateCommentController;
import org.example.controllers.follower.FollowController;
import org.example.controllers.post.CreatePostController;
import org.example.controllers.post.ViewPostController;
import org.example.controllers.user.UserLoginController;
import org.example.controllers.user.UserRegisterController;

public class RoutesConfig {

    public static void configureRoutes(Javalin app) {
        UserRegisterController urc = new UserRegisterController();
        UserLoginController ulc = new UserLoginController();
        CreatePostController cpc = new CreatePostController();
        CreateCommentController ccc = new CreateCommentController();
        FollowController fc = new FollowController();
        ViewPostController vpc = new ViewPostController();
        JwtUtils jwt = new JwtUtils();

        app.post("/rest/register", urc::register);
        app.post("/rest/login", ulc::login);

        app.before("/api/**", jwt.ensureJwtAuth);
        app.post("/api/post", cpc::post);
        app.post("/api/comment",ccc::comment);
        app.post("/api/follow", fc::follow);
        app.post("/api/unfollow", fc::unfollow);
        app.get("/api/posts", vpc::viewFollowersPosts);
    }
}
