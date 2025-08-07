package org.saadMeddiche.repositories;

import org.saadMeddiche.entities.ToDo;
import org.saadMeddiche.requests.ToDoCreateRequest;
import org.saadMeddiche.requests.ToDoUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface ToDoRepository {

    Optional<ToDo> retrieveById(Long id);

    List<ToDo> retrieveAll();

    void create(ToDoCreateRequest toDoCreateRequest);

    void update(long id, ToDoUpdateRequest todoUpdateRequest);

    void delete(long id);

}
