package org.saadMeddiche.services;

import org.saadMeddiche.entities.ToDo;
import org.saadMeddiche.repositories.ToDoRepository;
import org.saadMeddiche.repositories.impl.ToDoOrmLiteRepository;
import org.saadMeddiche.requests.ToDoCreateRequest;
import org.saadMeddiche.requests.ToDoUpdateRequest;

import java.util.List;
import java.util.Optional;

public class ToDoService {

    public static final ToDoService INSTANCE = new ToDoService();

    private final ToDoRepository toDoRepository = ToDoOrmLiteRepository.INSTANCE;

    public Optional<ToDo> retrieveById(Long id) {
        return toDoRepository.retrieveById(id);
    }

    public List<ToDo> retrieveAll() {
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
