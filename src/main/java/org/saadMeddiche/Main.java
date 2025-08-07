package org.saadMeddiche;

import io.javalin.Javalin;
import org.saadMeddiche.controllers.ToDoController;

public class Main {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);
        ToDoController controller = new ToDoController(app);
    }

}