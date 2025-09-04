package org.saadMeddiche.web;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.saadMeddiche.web.controllers.BookController;
import org.saadMeddiche.web.controllers.ToDoController;

public class JavalinApp {

    private final Javalin app;

    public JavalinApp() {
        this.app = Javalin.create(config -> {
            config.useVirtualThreads = true;
        }).start(7070);
    }

    public void start() {
        this.registerControllers();
        this.cors();
    }

    private void registerControllers() {
        new ToDoController(app);
        new BookController(app);
    }

    private void cors() {
        app.before(JavalinApp::corsHeaders);
        app.options("/*", JavalinApp::corsHeadersOptions);
    }

    private static void corsHeaders(Context ctx) {
        ctx.header("Access-Control-Allow-Origin", "http://localhost:5173");
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true");
    }

    private static void corsHeadersOptions(Context ctx) {
        ctx.header("Access-Control-Allow-Origin", "http://localhost:5173");
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true");
        ctx.status(204);
    }

}
