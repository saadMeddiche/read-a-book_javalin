package org.saadMeddiche.initializeres.impl;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;
import lombok.extern.slf4j.Slf4j;
import org.saadMeddiche.configurations.PackageConfiguration;
import org.saadMeddiche.initializeres.Initializable;
import org.saadMeddiche.utils.ClassScanner;
import org.saadMeddiche.utils.DatabaseConnectionProvider;

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

    private void startProcess() throws SQLException {

        log.info("Initializing database tables for package: {}", entityPackage);

        log.info("Creating connection source...");
        ConnectionSource connectionSource = DatabaseConnectionProvider.getConnectionSource();

        log.info("Scanning for entities in package: {}", entityPackage);
        List<Class<?>> entities = ClassScanner.findClassesByPackageAndAnnotation(entityPackage, DatabaseTable.class);

        if (entities.isEmpty()) {
            log.warn("No entities found in package: {}", entityPackage);
            return;
        }

        log.info("Found {} entities in package: {}", entities.size(), entityPackage);
        for (Class<?> entity : entities) {
            TableUtils.createTableIfNotExists(connectionSource, entity);
            log.info("Initialized table for: {}", entity.getSimpleName());
        }

    }

    // TODO: Implement filtering logic to skip entities that already have a table
    private List<Class<?>> filterEntitiesThatAlreadyHaveTable(List<Class<?>> entities, ConnectionSource connectionSource) throws SQLException {
        List<Class<?>> filteredEntities = new ArrayList<>();
        for (Class<?> entity : entities) {}
        return filteredEntities;
    }

}
