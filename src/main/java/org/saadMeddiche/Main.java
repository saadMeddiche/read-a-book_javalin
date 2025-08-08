package org.saadMeddiche;

import io.javalin.Javalin;
import org.saadMeddiche.controllers.ToDoController;
import org.saadMeddiche.repositories.TablesInitializer;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        Javalin app = Javalin.create().start(7070);
        ToDoController controller = new ToDoController(app);

        TablesInitializer tablesInitializer = new TablesInitializer("org.saadMeddiche.entities");
        tablesInitializer.initialize();

    }

}