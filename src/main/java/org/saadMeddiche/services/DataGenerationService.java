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

@Slf4j
public class DataGenerationService {

    private final Faker faker = new Faker();
    private final Random random = new Random();

    private final int BOOK_COUNT = 200_000;
    private final Pair<Integer,Integer> CHAPTERS_COUNT_RANGE = new Pair<>(5, 20);
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

        int BOOK_ID_COUNT = 1;

        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + Tables.BOOKS + " (id, title, author, summary) VALUES (?, ?, ?, ?)")) {

            for( int i = 0; i < BOOK_COUNT; i++) {
                stmt.setLong(1, BOOK_ID_COUNT++);
                stmt.setString(2, faker.book().title());
                stmt.setString(3, faker.book().author());
                stmt.setString(4, faker.lorem().paragraph());
                stmt.addBatch();
            }

            stmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void generateChapters() {

        int BOOK_ID_COUNT = 1;

        int CHAPTER_ID_COUNT = 1;

        try(Connection connection = DatabaseConnectionProvider.getConnection(); PreparedStatement statement = connection.prepareStatement("INSERT INTO " + Tables.CHAPTERS + " (id, title, book_id) VALUES (?, ?, ?)") ) {

            for(int i = 0; i < BOOK_COUNT; i++) {

                int chapterNumberForBook = random.nextInt(CHAPTERS_COUNT_RANGE.getFirst(), CHAPTERS_COUNT_RANGE.getSecond() + 1);

                for(int j = 0; j < chapterNumberForBook; j++) {
                    statement.setLong(1, CHAPTER_ID_COUNT++);
                    statement.setString(2, faker.text().text(5, 15));
                    statement.setLong(3, BOOK_ID_COUNT);
                    statement.addBatch();
                }

                BOOK_ID_COUNT++;

            }

            statement.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
