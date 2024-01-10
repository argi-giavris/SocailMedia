package org.example;

import io.javalin.Javalin;
import org.example.utils.RoutesConfig;

public class Main {

    public static void main(String[] args) {
        Javalin app = Javalin
                .create()
                .get("/", ctx -> ctx.result("Hello"));
        RoutesConfig.configureRoutes(app);
        app.start(8080);
    }

}