package org.example;

import io.javalin.Javalin;
import org.example.controllers.comment.CreateCommentController;
import org.example.controllers.follower.FollowController;
import org.example.controllers.post.CreatePostController;
import org.example.controllers.user.UserLoginController;
import org.example.controllers.user.UserRegisterController;
import org.example.utils.JwtUtils;

public class Main {

    public static void main(String[] args) {
        Javalin app = Javalin
                .create()
                .get("/", ctx -> ctx.result("Hello"));
        UserRegisterController urc = new UserRegisterController();
        UserLoginController ulc = new UserLoginController();
        CreatePostController cpc = new CreatePostController();
        CreateCommentController ccc = new CreateCommentController();
        FollowController fc = new FollowController();
        JwtUtils jwt = new JwtUtils();



        app.post("/rest/register", urc::register);
        app.post("/rest/login", ulc::login);

        app.before("/api/**", jwt.ensureJwtAuth);
        app.post("/api/post", cpc::post);
        app.post("/api/comment",ccc::comment);
        app.post("/api/follow", fc::follow);
        app.post("/api/unfollow", fc::unfollow);

        app.start(8080);
    }

}