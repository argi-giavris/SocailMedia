package org.example.utils;

import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    public static void handleException(Exception e, Context ctx) {
        logger.error("Exception caught:", e);

        ctx.status(500).json("Exception handler " + e.getMessage());

    }


}
