package org.saadMeddiche.services;

import org.saadMeddiche.entities.ToDo;
import org.saadMeddiche.repositories.ToDoRepository;
import org.saadMeddiche.repositories.impl.ToDoSimpleRepository;
import org.saadMeddiche.requests.ToDoCreateRequest;
import org.saadMeddiche.requests.ToDoUpdateRequest;
import org.saadMeddiche.responses.ToDoResponse;

import java.util.List;
import java.util.Optional;

public class ToDoService {

    public static final ToDoService INSTANCE = new ToDoService();

    private final ToDoRepository toDoRepository = ToDoSimpleRepository.INSTANCE;

    public Optional<ToDo> retrieveById(Long id) {
        return toDoRepository.retrieveById(id);
    }

    public List<ToDoResponse> retrieveAll() {
        return toDoRepository.retrieveAll();
    }

    public void create(ToDoCreateRequest toDoCreateRequest) {
        toDoRepository.create(toDoCreateRequest);
    }

    public void update(long id, ToDoUpdateRequest todoUpdateRequest) {
        toDoRepository.update(id, todoUpdateRequest);
    }

    public void delete(long id) {
        toDoRepository.delete(id);
    }

}
