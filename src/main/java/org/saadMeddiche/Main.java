package org.saadMeddiche;

import io.javalin.Javalin;
import org.saadMeddiche.controllers.ToDoController;
import org.saadMeddiche.repositories.TablesInitializer;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        Main main = new Main();
        main.setupDatabaseTable();
        main.setupEndpoints();
    }

    private final Javalin app = Javalin.create().start(7070);

    public void setupDatabaseTable() throws SQLException {
        new TablesInitializer("org.saadMeddiche.entities");
    }

    public void setupEndpoints() {
        new ToDoController(app);
    }

}