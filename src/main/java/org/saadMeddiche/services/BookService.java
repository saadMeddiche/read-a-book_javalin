package org.saadMeddiche.services;

import lombok.extern.slf4j.Slf4j;
import org.saadMeddiche.constants.Tables;
import org.saadMeddiche.requests.BookCreateRequest;
import org.saadMeddiche.responses.BookDetailsResponse;
import org.saadMeddiche.responses.BookPaginationResponse;
import org.saadMeddiche.utils.DatabaseConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class BookService {

    public static final BookService INSTANCE = new BookService();

    public Optional<BookDetailsResponse> getBookById(Long id) {

        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + Tables.BOOKS + " WHERE id = ?")) {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new BookDetailsResponse(rs));
            }

        } catch (SQLException e) {
            log.error("Error retrieving book with id {}: {}", id, e.getMessage(), e);
            return Optional.empty();
        }

        return Optional.empty();
    }

    public List<BookPaginationResponse> getAllBooks() {

        List<BookPaginationResponse> books = new ArrayList<>();

        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + Tables.BOOKS)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(new BookPaginationResponse(rs));
            }

        } catch (SQLException e) {
            log.error("Error retrieving books: {}", e.getMessage(), e);
            return Collections.emptyList();
        }

        return books;
    }

    public void createBook(BookCreateRequest bookCreateRequest) throws SQLException {
        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + Tables.BOOKS + " (title, author, summary) VALUES (?, ?, ?)")) {

            stmt.setString(1, bookCreateRequest.title());
            stmt.setString(2, bookCreateRequest.author());
            stmt.setString(3, bookCreateRequest.summary());

            stmt.executeUpdate();
        }
    }

}
