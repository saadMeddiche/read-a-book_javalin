package org.saadMeddiche.services;

import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.saadMeddiche.constants.Tables;
import org.saadMeddiche.utils.DatabaseConnectionProvider;

import java.sql.*;
import java.util.*;
import java.util.Date;

@Slf4j
public class DataGenerationService {

    private final Faker faker = new Faker();
    private final Random random = new Random();

    private final int MAX_CHUNK_SIZE = 500_000;

    private final int BOOK_COUNT = 100_000;
    private final Pair<Integer,Integer> CHAPTERS_COUNT_RANGE = new Pair<>(2, 6);
    private final Pair<Integer,Integer> PAGES_COUNT_RANGE = new Pair<>(3, 5);
    private final Pair<Integer,Integer> PARAGRAPHS_COUNT_RANGE = new Pair<>(2, 7);

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

        currentDate = new Date();
        log.info("Generating Pages...");
        generatePages();
        log.info("Pages Generated in {}", new Date().getTime() - currentDate.getTime());

        currentDate = new Date();
        log.info("Generating Paragraphs...");
        generateParagraphs();
        log.info("Paragraphs Generated in {}", new Date().getTime() - currentDate.getTime());

    }

    private void generateBooks() {

        int BOOK_ID_COUNT = 1;

        int CHUNK_SIZE_COUNT = 0;

        try (Connection connection = DatabaseConnectionProvider.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + Tables.BOOKS + " (id, title, author, summary) VALUES (?, ?, ?, ?)")) {

            for( int i = 0; i < BOOK_COUNT; i++) {
                preparedStatement.setLong(1, BOOK_ID_COUNT++);
                preparedStatement.setString(2, faker.book().title());
                preparedStatement.setString(3, faker.book().author());
                preparedStatement.setString(4, faker.lorem().paragraph());
                preparedStatement.addBatch();

                if(++CHUNK_SIZE_COUNT % MAX_CHUNK_SIZE == 0) {
                    preparedStatement.executeBatch();
                }

            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void generateChapters() {

        int BOOK_ID_COUNT = 1;

        int CHAPTER_ID_COUNT = 1;

        int CHUNK_SIZE_COUNT = 0;

        try(Connection connection = DatabaseConnectionProvider.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + Tables.CHAPTERS + " (id, title, book_id) VALUES (?, ?, ?)") ) {

            for(int i = 0; i < BOOK_COUNT; i++) {

                int chapterNumberForBook = random.nextInt(CHAPTERS_COUNT_RANGE.getFirst(), CHAPTERS_COUNT_RANGE.getSecond() + 1);

                for(int j = 0; j < chapterNumberForBook; j++) {
                    preparedStatement.setLong(1, CHAPTER_ID_COUNT++);
                    preparedStatement.setString(2, faker.text().text(5, 15));
                    preparedStatement.setLong(3, BOOK_ID_COUNT);
                    preparedStatement.addBatch();

                    if(++CHUNK_SIZE_COUNT % MAX_CHUNK_SIZE == 0) {
                        preparedStatement.executeBatch();
                    }

                }

                BOOK_ID_COUNT++;

            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void generatePages() {

        int CHAPTERS_COUNT = 0;

        int CHAPTER_ID_COUNT = 1;

        int PAGE_ID_COUNT = 1;

        int CHUNK_SIZE_COUNT = 0;

        try(Connection connection = DatabaseConnectionProvider.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT count(*) FROM " + Tables.CHAPTERS)) {

            if(resultSet.next()) {
                CHAPTERS_COUNT = resultSet.getInt(1);
            }

        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

        try(Connection connection = DatabaseConnectionProvider.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + Tables.PAGES + " (id,chapter_id) VALUES (?, ?)")) {

            for(int i = 0; i < CHAPTERS_COUNT; i++) {

                int ageNumberForChapter = random.nextInt(PAGES_COUNT_RANGE.getFirst(), PAGES_COUNT_RANGE.getSecond() + 1);

                for(int j = 0; j < ageNumberForChapter; j++) {
                    preparedStatement.setLong(1, PAGE_ID_COUNT++);
                    preparedStatement.setLong(2, CHAPTER_ID_COUNT);
                    preparedStatement.addBatch();

                    if(++CHUNK_SIZE_COUNT % MAX_CHUNK_SIZE == 0) {
                        preparedStatement.executeBatch();
                    }

                }

                CHAPTER_ID_COUNT++;

            }

            preparedStatement.executeBatch();

        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void generateParagraphs() {

        int PAGES_COUNT = 0;

        int PAGE_ID_COUNT = 1;

        int PARAGRAPHS_ID_COUNT = 1;

        int CHUNK_SIZE_COUNT = 0;

        String countQuery = "SELECT count(*) FROM "  + Tables.PAGES;
        try(Connection connection = DatabaseConnectionProvider.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(countQuery)) {

            if(resultSet.next()) {
                PAGES_COUNT = resultSet.getInt(1);
            }

        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

        String insertQuery = "INSERT INTO " + Tables.PARAGRAPHS + " (id, content, page_id) VALUES (?, ?, ?)";
        try(Connection connection = DatabaseConnectionProvider.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            for(int i = 0; i < PAGES_COUNT; i++) {

                int paragraphNumberForPage = random.nextInt(PARAGRAPHS_COUNT_RANGE.getFirst(), PARAGRAPHS_COUNT_RANGE.getSecond() + 1);

                for(int j = 0; j < paragraphNumberForPage; j++) {
                    preparedStatement.setLong(1, PARAGRAPHS_ID_COUNT++);
                    preparedStatement.setString(2, faker.lorem().sentence(20, 60));
                    preparedStatement.setLong(3, PAGE_ID_COUNT);
                    preparedStatement.addBatch();

                    if(++CHUNK_SIZE_COUNT % MAX_CHUNK_SIZE == 0) {
                        preparedStatement.executeBatch();
                    }

                }

                PAGE_ID_COUNT++;

            }

            preparedStatement.executeBatch();

        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
