package org.saadMeddiche.services;

import lombok.extern.slf4j.Slf4j;
import org.saadMeddiche.constants.Tables;
import org.saadMeddiche.requests.BookCreateRequest;
import org.saadMeddiche.responses.*;
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

    public Optional<BookDetailsResponse> getBookDetailsById(long id) {

        Optional<BookResponse> bookOpt = getBookById(id);
        if (bookOpt.isEmpty()) {
            return Optional.empty();
        }

        BookResponse book = bookOpt.get();
        List<ChapterResponse> chapters = getChaptersByBookId(id);
        List<PageResponse> pages = getPagesByBookId(id);
        List<ParagraphResponse> paragraphs = getParagraphsByBookId(id);

        BookDetailsResponse bookDetails = new BookDetailsResponse(book, chapters, pages, paragraphs);
        return Optional.of(bookDetails);
    }

    private Optional<BookResponse> getBookById(Long id) {

        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + Tables.BOOKS + " WHERE id = ?")) {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new BookResponse(rs));
            }

        } catch (SQLException e) {
            log.error("Error retrieving book with id {}: {}", id, e.getMessage(), e);
            return Optional.empty();
        }

        return Optional.empty();
    }

    private List<ChapterResponse> getChaptersByBookId(Long bookId) {
        List<ChapterResponse> chapters = new ArrayList<>();

        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT c.id, c.title FROM " + Tables.CHAPTERS + " as c WHERE book_id = ?")) {
            stmt.setLong(1, bookId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                chapters.add(new ChapterResponse(rs));
            }

        } catch (SQLException e) {
            log.error("Error retrieving chapters for book id {}: {}", bookId, e.getMessage(), e);
            return Collections.emptyList();
        }

        return chapters;
    }

    private List<PageResponse> getPagesByBookId(Long bookId) {

        List<PageResponse> pages = new ArrayList<>();

        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT p.id FROM " + Tables.PAGES + " as p JOIN " + Tables.CHAPTERS + " as c ON p.chapter_id = c.id WHERE c.book_id = ?")) {
            stmt.setLong(1, bookId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                pages.add(new PageResponse(rs));
            }

        } catch (SQLException e) {
            log.error("Error retrieving pages for book id {}: {}", bookId, e.getMessage(), e);
            return Collections.emptyList();
        }

        return pages;

    }

    private List<ParagraphResponse> getParagraphsByBookId(Long bookId) {
        List<ParagraphResponse> paragraphs = new ArrayList<>();

        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT par.id, par.content FROM " + Tables.PARAGRAPHS + " as par JOIN " + Tables.PAGES + " as p ON par.page_id = p.id JOIN " + Tables.CHAPTERS + " as c ON p.chapter_id = c.id WHERE c.book_id = ?")) {
            stmt.setLong(1, bookId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                paragraphs.add(new ParagraphResponse(rs));
            }

        } catch (SQLException e) {
            log.error("Error retrieving paragraphs for book id {}: {}", bookId, e.getMessage(), e);
            return Collections.emptyList();
        }

        return paragraphs;
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
