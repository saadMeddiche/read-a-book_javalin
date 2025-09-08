package org.saadMeddiche.services;

import kotlin.Pair;
import net.datafaker.Faker;
import org.saadMeddiche.constants.Tables;
import org.saadMeddiche.utils.DatabaseConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

public class DataGenerationService {

    private final Faker faker = new Faker();
    private final Random random = new Random();

    private final int PREPARED_STATEMENT_MAX_PARAMETERS = 65_535;

    private final int BOOK_COUNT = 200_000;
    private final int BOOK_PARAMETERS = 4;
    private int BOOK_ID_COUNT = 1;

    private final Pair<Integer,Integer> CHAPTERS_COUNT_RANGE = new Pair<>(5, 20);
    private final Pair<Integer,Integer> PAGES_COUNT_RANGE = new Pair<>(10, 50);
    private final Pair<Integer,Integer> PARAGRAPHS_COUNT_RANGE = new Pair<>(3, 10);

    public void generateData() {
        generate(BOOK_PARAMETERS, BOOK_COUNT, this::generateBookChunk);
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
