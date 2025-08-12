package org.saadMeddiche.initializers.impl;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;
import lombok.extern.slf4j.Slf4j;
import org.saadMeddiche.configurations.PackageConfiguration;
import org.saadMeddiche.initializers.Initializable;
import org.saadMeddiche.utils.ClassScanner;
import org.saadMeddiche.utils.DatabaseConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TableInitializer implements Initializable {

    private final String entityPackage = PackageConfiguration.INSTANCE.ENTITY_PACKAGE;

    @Override
    public void initialize() {
        try {
            startProcess();
        } catch (SQLException e) {
            log.error("Failed to initialize database tables: {}", e.getMessage(), e);
        }
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    private void startProcess() throws SQLException {

        log.info("Initializing database tables for package: {}", entityPackage);

        log.info("Scanning for entities in package: {}", entityPackage);
        List<Class<?>> entities = ClassScanner.findClassesByPackageAndAnnotation(entityPackage, DatabaseTable.class);

        if (entities.isEmpty()) {
            log.warn("No entities found in package: {}", entityPackage);
            return;
        }

        log.info("Filtering entities that already have tables...");
        List<Class<?>> filteredEntities = filterEntitiesThatAlreadyHaveTable(entities);

        if (filteredEntities.isEmpty()) {
            log.info("All entities already have tables, skipping initialization.");
            return;
        }

        log.info("Creating connection source...");
        ConnectionSource connectionSource = DatabaseConnectionProvider.getConnectionSource();

        log.info("Found {} entities to initialize tables for in package: {}", filteredEntities.size(), entityPackage);
        for (Class<?> entity : filteredEntities) {
            TableUtils.createTableIfNotExists(connectionSource, entity);
            log.info("Initialized table for: {}", entity.getSimpleName());
        }

    }

    private List<Class<?>> filterEntitiesThatAlreadyHaveTable(List<Class<?>> entities) throws SQLException {
        List<Class<?>> filteredEntities = new ArrayList<>();
        for (Class<?> entity : entities) {
             String table = entity.getAnnotation(DatabaseTable.class).tableName();

            if (!isTableExists(table)) {
                filteredEntities.add(entity);
                log.info("Entity {} does not have a table, adding to initialization list.", entity.getSimpleName());
            }

        }
        return filteredEntities;
    }

    // TODO: FIX THE QUERY
    private boolean isTableExists(String tableName) throws SQLException {
        try(Connection conn = DatabaseConnectionProvider.getConnection(); PreparedStatement stmt = conn.prepareStatement ("SELECT 1 FROM ? LIMIT 1")) {
            stmt.setString(1, tableName);
            return stmt.execute();
        } catch (SQLException e) {
            log.warn("Table {} does not exist: {}", tableName, e.getMessage());
            return false;
        }
    }

}
