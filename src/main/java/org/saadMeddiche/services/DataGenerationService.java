package org.saadMeddiche.services;

import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.saadMeddiche.constants.Tables;
import org.saadMeddiche.utils.DatabaseConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
public class DataGenerationService {

    private final Faker faker = new Faker();
    private final Random random = new Random();

    private final int PREPARED_STATEMENT_MAX_PARAMETERS = 65_535;

    private final int BOOK_COUNT = 10_000;

    private final int BOOK_PARAMETERS = 4;
    private int BOOK_ID_COUNT = 1;

    private final int CHAPTER_PARAMETERS = 3;
    private int CHAPTER_ID_COUNT = 1;

    private final Pair<Integer,Integer> CHAPTERS_COUNT_RANGE = new Pair<>(20, 20);
    private final Pair<Integer,Integer> PAGES_COUNT_RANGE = new Pair<>(10, 50);
    private final Pair<Integer,Integer> PARAGRAPHS_COUNT_RANGE = new Pair<>(3, 10);

    public void generateData() {
        Date currentDate;

        currentDate = new Date();
        log.info("Generating Books...");
        generateBooks();
        log.info("Books Generated in {}", new Date().getTime() - currentDate.getTime());

        currentDate = new Date();
        log.info("Generating Chapters...");
        generateChapters();
        log.info("Chapters Generated in {}", new Date().getTime() - currentDate.getTime());

    }

    private void generateBooks() {
        generate(BOOK_PARAMETERS, BOOK_COUNT, this::generateBookChunk);
    }

    private void generateChapters() {

        int bookIdCount = 1;

        try(Connection connection = DatabaseConnectionProvider.getConnection(); PreparedStatement statement = connection.prepareStatement("INSERT INTO " + Tables.CHAPTERS + " (id, title, book_id) VALUES (?, ?, ?)") ) {

            for(int i = 0; i < BOOK_COUNT; i++) {

                int chapterNumberForBook = random.nextInt(CHAPTERS_COUNT_RANGE.getFirst(), CHAPTERS_COUNT_RANGE.getSecond() + 1);

                for(int j = 0; j < chapterNumberForBook; j++) {
                    statement.setLong(1, CHAPTER_ID_COUNT++);
                    statement.setString(2, faker.text().text(5, 15));
                    statement.setLong(3, bookIdCount);
                    statement.addBatch();
                }

                bookIdCount++;

            }

            statement.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void generateBookChunk(int count) {

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + Tables.BOOKS + " (id, title, author, summary) VALUES ");

        appendParameterToQuery(queryBuilder, count, BOOK_PARAMETERS);

        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {

            for( int i = 0; i < count; i++) {
                stmt.setLong(i * BOOK_PARAMETERS + 1, BOOK_ID_COUNT++);
                stmt.setString(i * BOOK_PARAMETERS + 2, faker.book().title());
                stmt.setString(i * BOOK_PARAMETERS + 3, faker.book().author());
                stmt.setString(i * BOOK_PARAMETERS + 4, faker.lorem().paragraph());
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void generateChapterChunk(int count, int bookId) {

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + Tables.CHAPTERS + " (id, title, book_id) VALUES ");

        appendParameterToQuery(queryBuilder, count, CHAPTER_PARAMETERS);

        try(Connection connection = DatabaseConnectionProvider.getConnection(); PreparedStatement statement = connection.prepareStatement(queryBuilder.toString()) ) {

            for(int i = 0; i < count; i++) {
                statement.setLong(i * CHAPTER_PARAMETERS + 1, CHAPTER_ID_COUNT++);
                statement.setString(i * CHAPTER_PARAMETERS + 2, faker.text().text(5, 15));
                statement.setLong(i * CHAPTER_PARAMETERS + 3, bookId);

            }

            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void generate(int tableParameters, int dataCount, Consumer<Integer> generator) {

        int chunkSize = PREPARED_STATEMENT_MAX_PARAMETERS / tableParameters;

        int chunksCount = Math.max(1, dataCount / chunkSize);

        for (int i = 0; i < chunksCount; i++) {
            generator.accept(chunkSize);
        }

        int modulo = dataCount % chunkSize;

        if(modulo != 0) {
            generator.accept((modulo));
        }

    }

    private void appendParameterToQuery(StringBuilder queryBuilder, int count, int parameters) {

        StringBuilder parametersGroupsBuilder = new StringBuilder();

        // Result (?,?,?,...)
        String parametersGroups = parametersGroupsBuilder
                .append("(")
                .append("?,".repeat(parameters - 1)).append("?")
                .append(")")
                .toString();

        // Repeat (?,?,?,...), (?,?,?,...), (?,?,?,...), ..., (?,?,?,...);
        queryBuilder
                .append((parametersGroups + ",").repeat(count - 1)).append(parametersGroups)
                .append(";");

    }

}
