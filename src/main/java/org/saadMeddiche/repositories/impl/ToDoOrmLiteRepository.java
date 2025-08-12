package org.saadMeddiche.repositories.impl;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import lombok.SneakyThrows;
import org.saadMeddiche.entities.ToDo;
import org.saadMeddiche.entities.User;
import org.saadMeddiche.repositories.ToDoRepository;
import org.saadMeddiche.requests.ToDoCreateRequest;
import org.saadMeddiche.requests.ToDoUpdateRequest;
import org.saadMeddiche.responses.ToDoResponse;
import org.saadMeddiche.utils.DatabaseConnectionProvider;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ToDoOrmLiteRepository implements ToDoRepository {

    public static ToDoRepository INSTANCE = new ToDoOrmLiteRepository();

    private final Dao<ToDo,Long> todoDao;

    private final Dao<User,Long> userDao;

    public ToDoOrmLiteRepository() {
        try {
            todoDao = DaoManager.createDao(DatabaseConnectionProvider.getConnectionSource(), ToDo.class);
            userDao = DaoManager.createDao(DatabaseConnectionProvider.getConnectionSource(), User.class);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create DAO for ToDo", e);
        }
    }

    @Override
    @SneakyThrows
    public Optional<ToDo> retrieveById(Long id) {
        return Optional.ofNullable(todoDao.queryForId(id));
    }

    @Override
    @SneakyThrows
    public List<ToDoResponse> retrieveAll() {
        QueryBuilder<User,Long> userQb = userDao.queryBuilder();
        QueryBuilder<ToDo,Long> toDoQb = todoDao.queryBuilder();
        List<ToDo> toDos = toDoQb.leftJoin(userQb).query();
        return toDos.stream()
                .map(toDo -> new ToDoResponse(
                        toDo.id,
                        toDo.title,
                        toDo.description,
                        toDo.user.fullName()
                )).toList();
    }

    @Override
    @SneakyThrows
    public void create(ToDoCreateRequest toDoCreateRequest) {

        ToDo toDo = ToDo.builder()
                .title(toDoCreateRequest.title())
                .description(toDoCreateRequest.description())
                .build();

        todoDao.create(toDo);

    }

    @Override
    @SneakyThrows
    public void update(long id, ToDoUpdateRequest todoUpdateRequest) {

        ToDo toDo = todoDao.queryForId(id);
        if (toDo == null) return;

        toDo.title = todoUpdateRequest.title();
        toDo.description = todoUpdateRequest.description();

        todoDao.update(toDo);

    }

    @Override
    @SneakyThrows
    public void delete(long id) {

        todoDao.deleteById(id);

    }

}
