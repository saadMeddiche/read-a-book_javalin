package org.saadMeddiche.repositories.impl;

import org.saadMeddiche.configurations.DataBaseConnectionConfiguration;
import org.saadMeddiche.entities.ToDo;
import org.saadMeddiche.repositories.ToDoRepository;
import org.saadMeddiche.requests.ToDoCreateRequest;
import org.saadMeddiche.requests.ToDoUpdateRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToDoSimpleRepository implements ToDoRepository {

    public static ToDoRepository INSTANCE = new ToDoSimpleRepository();

    private final DataBaseConnectionConfiguration connectionConfiguration = DataBaseConnectionConfiguration.INSTANCE;

    @Override
    public Optional<ToDo> retrieveById(Long id) {

        try(Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todo WHERE id = ?")) {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(builderToDo(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public List<ToDo> retrieveAll() {

        List<ToDo> toDos = new ArrayList<>();
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM todo")) {

            while (rs.next()) {
                toDos.add(builderToDo(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toDos;

    }

    @Override
    public void create(ToDoCreateRequest toDoCreateRequest) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO todo (title, description) VALUES (?, ?)")) {

            stmt.setString(1, toDoCreateRequest.title());
            stmt.setString(2, toDoCreateRequest.description());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(long id, ToDoUpdateRequest todoUpdateRequest) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement("UPDATE todo SET title = ?, description = ? WHERE id = ?")) {

            stmt.setString(1, todoUpdateRequest.title());
            stmt.setString(2, todoUpdateRequest.description());
            stmt.setLong(3, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(long id) {
        try(Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM todo WHERE id = ?")) {

            stmt.setLong(1, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionConfiguration.JDBC_URL, connectionConfiguration.JDBC_USER, connectionConfiguration.JDBC_PASSWORD);
    }

    private ToDo builderToDo(ResultSet rs) throws SQLException {
        return ToDo.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .build();
    }

}
