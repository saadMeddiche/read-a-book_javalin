package org.saadMeddiche.web.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.saadMeddiche.requests.BookCreateRequest;
import org.saadMeddiche.responses.BookDetailsResponse;
import org.saadMeddiche.services.BookService;

import java.sql.SQLException;
import java.util.Optional;

public class BookController {

    private final BookService bookService = BookService.INSTANCE;

    public BookController(Javalin app) {
        app.get("/books/{id}", this::retrieveBookDetails);
        app.get("/books", this::retrieveAllBooks);
        app.post("/books", this::createBook);
    }

    public void retrieveBookDetails(Context context) {

        long id = Long.parseLong(context.pathParam("id"));

        Optional<BookDetailsResponse> bookDetailsResponse = bookService.getBookById(id);

        bookDetailsResponse.ifPresentOrElse((book -> context.status(200).json(book)), () -> context.status(404));

    }

    public void retrieveAllBooks(Context context) {
        context.status(200).json(bookService.getAllBooks());
    }

    public void createBook(Context context) {

        BookCreateRequest bookCreateRequest = context.bodyValidator(BookCreateRequest.class)
                .check("title", req -> req.title() != null && !req.title().trim().isEmpty(), "NOT_EMPTY")
                .check("author", req -> req.author() != null && !req.author().trim().isEmpty(), "NOT_EMPTY")
                .check("summary", req -> req.summary() != null && !req.summary().trim().isEmpty(), "NOT_EMPTY")
                .get();

        try {
            bookService.createBook(bookCreateRequest);
        } catch (SQLException e) {
            context.status(500).result("Error creating book: " + e.getMessage());
            return;
        }

        context.status(201);

    }

}
