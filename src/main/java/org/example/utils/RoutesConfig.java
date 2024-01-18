package org.example.utils;

import io.javalin.Javalin;
import org.example.controllers.comment.CreateCommentController;
import org.example.controllers.comment.ViewCommentsOfOwnPostController;
import org.example.controllers.follower.FollowController;
import org.example.controllers.follower.GetFollowersController;
import org.example.controllers.post.*;
import org.example.controllers.user.UserLoginController;
import org.example.controllers.user.UserRegisterController;
import org.example.controllers.user.UserSearchController;

public class RoutesConfig {

    public static void configureRoutes(Javalin app) {
        UserRegisterController urc = new UserRegisterController();
        UserLoginController ulc = new UserLoginController();
        CreatePostController cpc = new CreatePostController();
        CreateCommentController ccc = new CreateCommentController();
        FollowController fc = new FollowController();
        ViewPostController vpc = new ViewPostController();
        ViewUserPostsWithCommentsController vupc = new ViewUserPostsWithCommentsController();
        ViewCommentsOfOwnPostController vcoop = new ViewCommentsOfOwnPostController();
        GetFollowersController gfc = new GetFollowersController();
        UserSearchController usc = new UserSearchController();
        GetPostSharedLinkController gpsl = new GetPostSharedLinkController();
        ViewPostWithCommentsController vpwcc = new ViewPostWithCommentsController();
        JwtUtils jwt = new JwtUtils();

        app.post("/rest/register", urc::register);
        app.post("/rest/login", ulc::login);

        app.before("/api/**", jwt.ensureJwtAuth);
        app.post("/api/post", cpc::post);
        app.post("/api/comment",ccc::comment);
        app.post("/api/follow", fc::follow);
        app.post("/api/unfollow", fc::unfollow);
        app.get("/api/posts", vpc::viewFollowersPosts);
        app.get("/api/my-posts-comments", vupc::viewUserPostsWithComments);
        app.get("/api/comments", vcoop::viewCommentsOfOwnPost);
        app.get("/api/followers", gfc::viewFollowers);
        app.get("/api/following", gfc::viewFollowing);
        app.get("/api/search/user", usc::searchUser); //http://localhost:8080/api/search/user?name=test
        app.get("/api/share-post", gpsl::getSharedLinkOfPost);
        app.get("/rest/posts", vpwcc::viewPostWthComments); //http://localhost:8080/rest/posts?postId=7
    }
}
