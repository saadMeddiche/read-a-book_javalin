package org.saadMeddiche.repositories.impl;

import org.saadMeddiche.constants.Tables;
import org.saadMeddiche.entities.User;
import org.saadMeddiche.repositories.UserRepository;
import org.saadMeddiche.requests.UserCreateRequest;
import org.saadMeddiche.requests.UserUpdateRequest;
import org.saadMeddiche.utils.DatabaseConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserSimpleRepository implements UserRepository {

    public static final UserRepository INSTANCE = new UserSimpleRepository();

    @Override
    public Optional<User> retrieveById(Long id) {
        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + Tables.USERS +" WHERE id = ?")) {
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
    public List<User> retrieveAll() {
        return List.of();
    }

    @Override
    public void create(UserCreateRequest userCreateRequest) {
        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + Tables.USERS + " (first_name, last_name, username, email, password) VALUES (?, ?, ?, ?, ?)")) {

            stmt.setString(1, userCreateRequest.firstName());
            stmt.setString(2, userCreateRequest.lastName());
            stmt.setString(3, userCreateRequest.username());
            stmt.setString(4, userCreateRequest.email());
            stmt.setString(5, userCreateRequest.password());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(long id, UserUpdateRequest todoUpdateRequest) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public boolean existsByUsername(String username) {

        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM " + Tables.USERS + " WHERE username = ?")) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private User builderToDo(ResultSet rs) throws SQLException {
        User user = new User();
        user.id = rs.getLong("id");
        user.firstName = rs.getString("first_name");
        user.lastName = rs.getString("last_name");
        user.username = rs.getString("username");
        user.email = rs.getString("email");
        user.password = "[PROTECTED]";
        return user;
    }

}
