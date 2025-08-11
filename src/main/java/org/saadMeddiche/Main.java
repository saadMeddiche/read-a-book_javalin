package org.saadMeddiche;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;
import org.saadMeddiche.controllers.ToDoController;
import org.saadMeddiche.repositories.TablesInitializer;

import java.sql.SQLException;

@Slf4j
public class Main {

    public static void main(String[] args) throws SQLException {
        Main main = new Main();
        try {
            main.setupDatabaseTable();
        }catch (Exception e){
            log.error("This is only a temporary fix to avoid the application from crashing when the database table already exists. " , e);
        }
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