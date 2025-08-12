package org.saadMeddiche.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.saadMeddiche.entities.ToDo;
import org.saadMeddiche.requests.ToDoCreateRequest;
import org.saadMeddiche.requests.ToDoUpdateRequest;
import org.saadMeddiche.services.ToDoService;

import java.util.List;
import java.util.Optional;

public class ToDoController {

    private final ToDoService toDoService = ToDoService.INSTANCE;

    public ToDoController(Javalin app) {
        app.get("/to-dos/{id}", this::retrieveToDo)
                .get("/to-dos", this::retrieveAllToDo)
                .post("/to-dos", this::createToDo)
                .put("/to-dos/{id}", this::updateToDo)
                .delete("/to-dos/{id}", this::deleteToDo);
    }

    public void retrieveToDo(Context context) {

        long id = Long.parseLong(context.pathParam("id"));

        Optional<ToDo> toDoOptional = toDoService.retrieveById(id);

        toDoOptional.ifPresentOrElse((toDo) -> context.status(200).json(toDo), () -> context.status(404));

    }

    public void retrieveAllToDo(Context context) {
        List<ToDo> toDos = toDoService.retrieveAll();
        context.status(200).json(toDos);
    }

    public void createToDo(Context context) {
        ToDoCreateRequest createRequest = context.bodyValidator(ToDoCreateRequest.class)
                        .check(request -> request.title() != null && !request.title().trim().isEmpty(),"NOT_EMPTY")
                        .check(request -> request.description() != null && !request.description().trim().isEmpty(),"NOT_EMPTY")
                .get();
        toDoService.create(createRequest);
        context.status(201);
    }

    public void updateToDo(Context context) {
        long id = Long.parseLong(context.pathParam("id"));
        ToDoUpdateRequest request = context.bodyAsClass(ToDoUpdateRequest.class);
        toDoService.update(id, request);
        context.status(204);
    }

    public void deleteToDo(Context context) {
        long id = Long.parseLong(context.pathParam("id"));
        toDoService.delete(id);
        context.status(204);
    }

}
