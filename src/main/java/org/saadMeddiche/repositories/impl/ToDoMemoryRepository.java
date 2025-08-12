package org.saadMeddiche.repositories.impl;

import org.saadMeddiche.entities.ToDo;
import org.saadMeddiche.exceptions.NotFoundException;
import org.saadMeddiche.repositories.ToDoRepository;
import org.saadMeddiche.requests.ToDoCreateRequest;
import org.saadMeddiche.requests.ToDoUpdateRequest;
import org.saadMeddiche.responses.ToDoResponse;
import org.saadMeddiche.utils.CurrentAuthenticatedUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToDoMemoryRepository implements ToDoRepository {

    public static ToDoRepository INSTANCE = new ToDoMemoryRepository();

    private final List<ToDo> toDos = new ArrayList<>();

    public Optional<ToDo> retrieveById(Long id) {
        return toDos.stream()
                .filter(toDo -> toDo.id.equals(id))
                .findFirst();
    }

    public List<ToDoResponse> retrieveAll() {

        List<ToDoResponse> toDoResponses = new ArrayList<>();

        for(ToDo toDo : toDos) {

            ToDoResponse toDoResponse = new ToDoResponse(
                    toDo.id,
                    toDo.title,
                    toDo.description,
                    toDo.user.firstName + " " + toDo.user.lastName
            );

            toDoResponses.add(toDoResponse);
        }

        return toDoResponses;

    }

    public void create(ToDoCreateRequest toDoCreateRequest) {
        ToDo toDo = new ToDo();
        toDo.id = (long) (toDos.size() + 1);
        toDo.title = toDoCreateRequest.title();
        toDo.description = toDoCreateRequest.description();
        toDo.user = CurrentAuthenticatedUser.retrieve();
        toDos.add(toDo);
    }

    public void update(long id, ToDoUpdateRequest todoUpdateRequest) {
        ToDo toDo = retrieveById(id)
                .orElseThrow(() -> new NotFoundException("ToDo not found"));
        toDo.title = todoUpdateRequest.title();
        toDo.description = todoUpdateRequest.description();
    }

    public void delete(long id) {
        ToDo toDo = retrieveById(id)
                .orElseThrow(() -> new NotFoundException("ToDo not found"));
        toDos.remove(toDo);
    }

}
