package org.saadMeddiche.repositories.impl;

import org.saadMeddiche.constants.Tables;
import org.saadMeddiche.entities.ToDo;
import org.saadMeddiche.entities.User;
import org.saadMeddiche.exceptions.AuthenticationException;
import org.saadMeddiche.repositories.ToDoRepository;
import org.saadMeddiche.requests.ToDoCreateRequest;
import org.saadMeddiche.requests.ToDoUpdateRequest;
import org.saadMeddiche.responses.ToDoResponse;
import org.saadMeddiche.utils.CurrentAuthenticatedUser;
import org.saadMeddiche.utils.DatabaseConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToDoSimpleRepository implements ToDoRepository {

    public static ToDoRepository INSTANCE = new ToDoSimpleRepository();

    @Override
    public Optional<ToDo> retrieveById(Long id) {

        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT t.* , u.id as user_id, u.* FROM " + Tables.TODOS + " t JOIN " + Tables.USERS + " u on u.id = t.user_id WHERE t.id = ?")) {
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
        try (Connection conn = DatabaseConnectionProvider.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT t.id, t.title, t.description, CONCAT(u.first_name , ' ', u.last_name) as userFullName FROM " + Tables.TODOS + " t LEFT JOIN " + Tables.USERS + " u on u.id = t.user_id")) {

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

        User authenticatedUser = CurrentAuthenticatedUser.retrieve()
                .orElseThrow(() -> new AuthenticationException("User not logged in"));

        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + Tables.TODOS + " (title, description, user_id) VALUES (?, ?, ?)")) {

            stmt.setString(1, toDoCreateRequest.title());
            stmt.setString(2, toDoCreateRequest.description());
            stmt.setLong(3, authenticatedUser.id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(long id, ToDoUpdateRequest todoUpdateRequest) {
        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("UPDATE "  + Tables.TODOS + " SET title = ?, description = ? WHERE id = ?")) {

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
        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + Tables.TODOS + " WHERE id = ?")) {

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
                .user(builderUser(rs))
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

    private User builderUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .username(rs.getString("username"))
                .password("[PROTECTED]")
                .build();
    }

}
