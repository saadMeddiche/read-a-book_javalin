package org.saadMeddiche.repositories.impl;

import org.saadMeddiche.entities.ToDo;
import org.saadMeddiche.repositories.ToDoRepository;
import org.saadMeddiche.requests.ToDoCreateRequest;
import org.saadMeddiche.requests.ToDoUpdateRequest;
import org.saadMeddiche.responses.ToDoResponse;
import org.saadMeddiche.utils.DatabaseConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToDoSimpleRepository implements ToDoRepository {

    public static ToDoRepository INSTANCE = new ToDoSimpleRepository();

    @Override
    public Optional<ToDo> retrieveById(Long id) {

        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todo WHERE id = ?")) {
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
    public List<ToDoResponse> retrieveAll() {

        List<ToDoResponse> toDoResponses = new ArrayList<>();
        try (Connection conn = DatabaseConnectionProvider.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT t.id, t.title, t.description, CONCAT(u.first_name , ' ', u.last_name) as userFullName FROM todo t LEFT JOIN public.user u on u.id = t.user_id")) {

            while (rs.next()) {
                toDoResponses.add(builderToDoResponse(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toDoResponses;

    }

    @Override
    public void create(ToDoCreateRequest toDoCreateRequest) {
        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO todo (title, description) VALUES (?, ?)")) {

            stmt.setString(1, toDoCreateRequest.title());
            stmt.setString(2, toDoCreateRequest.description());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(long id, ToDoUpdateRequest todoUpdateRequest) {
        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("UPDATE todo SET title = ?, description = ? WHERE id = ?")) {

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
        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM todo WHERE id = ?")) {

            stmt.setLong(1, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private ToDo builderToDo(ResultSet rs) throws SQLException {
        return ToDo.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .build();
    }

    private ToDoResponse builderToDoResponse(ResultSet rs) throws SQLException {
        return ToDoResponse.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .userFullName(rs.getString("userFullName"))
                .build();
    }

}
