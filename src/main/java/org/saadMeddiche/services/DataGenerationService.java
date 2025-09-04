package org.saadMeddiche.services;

import kotlin.Pair;
import net.datafaker.Faker;
import org.saadMeddiche.constants.Tables;
import org.saadMeddiche.utils.DatabaseConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class DataGenerationService {

    private final Faker faker = new Faker();

    private final Random random = new Random();

    private final int BOOK_COUNT = 16_383;
    private final Pair<Integer,Integer> CHAPTERS_COUNT_RANGE = new Pair<>(5, 20);
    private final Pair<Integer,Integer> PAGES_COUNT_RANGE = new Pair<>(10, 50);
    private final Pair<Integer,Integer> PARAGRAPHS_COUNT_RANGE = new Pair<>(3, 10);

    public void generateData() {
        generateBooks();
    }

    private void generateBooks() {

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + Tables.BOOKS + " (id, title, author, summary) VALUES ");

        for (int i = 1; i <= BOOK_COUNT; i++) {

            if(i == BOOK_COUNT) {
                queryBuilder.append("(?, ?, ?, ?);");
                break;
            }

            queryBuilder.append("(?, ?, ?, ?), ");

        }

        try (Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {

            for( int i = 0; i < BOOK_COUNT; i++) {
                stmt.setLong(i * 4 + 1, i + 1);
                stmt.setString(i * 4 + 2, faker.book().title());
                stmt.setString(i * 4 + 3, faker.book().author());
                stmt.setString(i * 4 + 4, faker.lorem().paragraph());
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
