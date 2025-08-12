package org.saadMeddiche;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;
import org.saadMeddiche.controllers.ToDoController;
import org.saadMeddiche.initializers.Launcher;

import java.sql.SQLException;

@Slf4j
public class Main {

    public static void main(String[] args) throws SQLException {

        Launcher launcher = new Launcher();
        launcher.start();

        Main main = new Main();
        main.setupEndpoints();
    }

    private final Javalin app = Javalin.create(javalinConfig -> {
        javalinConfig.useVirtualThreads = true;
    }).start(7070);

    public void setupEndpoints() {
        new ToDoController(app);
    }

}