package org.saadMeddiche.repositories;

import org.saadMeddiche.entities.NotFoundException;
import org.saadMeddiche.entities.ToDo;
import org.saadMeddiche.requests.ToDoCreateRequest;
import org.saadMeddiche.requests.ToDoUpdateRequest;

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

    public List<ToDo> retrieveAll() {
        return toDos;
    }

    public void create(ToDoCreateRequest toDoCreateRequest) {
        ToDo toDo = new ToDo();
        toDo.id = (long) (toDos.size() + 1);
        toDo.title = toDoCreateRequest.title();
        toDo.description = toDoCreateRequest.description();
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
