package org.saadMeddiche.services;

import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.saadMeddiche.configurations.DataGeneratorConfiguration;
import org.saadMeddiche.constants.Tables;
import org.saadMeddiche.utils.DatabaseConnectionProvider;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

@Slf4j
public class DataGeneratorService implements AutoCloseable {

    private final Faker faker = new Faker();
    private final Random random = new Random();

    private final int MAX_CHUNK_SIZE = DataGeneratorConfiguration.INSTANCE.MAX_CHUNK_SIZE;

    private final int BOOK_COUNT = DataGeneratorConfiguration.INSTANCE.BOOK_COUNT;
    private final Pair<Integer,Integer> CHAPTERS_COUNT_RANGE = DataGeneratorConfiguration.INSTANCE.CHAPTERS_COUNT_RANGE;
    private final Pair<Integer,Integer> PAGES_COUNT_RANGE = DataGeneratorConfiguration.INSTANCE.PAGES_COUNT_RANGE;
    private final Pair<Integer,Integer> PARAGRAPHS_COUNT_RANGE = DataGeneratorConfiguration.INSTANCE.PARAGRAPHS_COUNT_RANGE;

    public void generateData() {

        if(!DataGeneratorConfiguration.INSTANCE.IS_ENABLED) {
            log.info("Data Generator is disabled.");
            return;
        }

        Date total = new Date();

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

        long totalTime = new Date().getTime() - total.getTime();
        log.info("Total Time in seconds: {}", totalTime / 1000.0);

    }

    private void generateBooks() {

        int BOOK_ID_COUNT = 1;

        int CHUNK_SIZE_COUNT = 0;

        try (Connection connection = DatabaseConnectionProvider.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + Tables.BOOKS + " (id, title, author, summary, cover_image) VALUES (?, ?, ?, ?, ?)")) {

            for( int i = 0; i < BOOK_COUNT; i++) {
                String bookTitle = faker.book().title();
                preparedStatement.setLong(1, BOOK_ID_COUNT++);
                preparedStatement.setString(2, bookTitle);
                preparedStatement.setString(3, faker.book().author());
                preparedStatement.setString(4, faker.lorem().paragraph());
                preparedStatement.setBinaryStream(5, this.generateBookCoverImage(bookTitle));
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

    private ByteArrayInputStream generateBookCoverImage(String bookTitle) {

        // Define image dimensions
        int width = 400;
        int height = 300;
        int x = 0, y = 0;

        // Create a BufferedImage object
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Get the Graphics2D object to draw on the image
        Graphics2D g2d = image.createGraphics();

        // Fill the background with a color
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x, y, width, height);

        x = 50; y = 50;

        // Draw a rectangle with a border
        g2d.setColor(Color.BLUE);
        g2d.fillRect(x, y, width - 2*x , height - 2*y);

        g2d.setColor(Color.RED);
        g2d.drawRect(x, y, width - 2*x , height - 2*y);

        g2d.setColor(Color.WHITE);
        g2d.drawString(bookTitle, width / 2 - g2d.getFontMetrics().stringWidth(bookTitle) / 2, height / 2);

        // Dispose of the Graphics2D object
        g2d.dispose();

        // Transform the image into bytes
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            ImageIO.write(image, "png", outputStream);

            outputStream.flush();

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            log.error("Error while writing image", e);
            return new ByteArrayInputStream(new byte[0]);

        }

    }

    @Override
    public void close() {
        System.gc();
    }

}
