package org.saadMeddiche.repositories;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.extern.slf4j.Slf4j;
import org.saadMeddiche.configurations.DataBaseConfiguration;
import org.saadMeddiche.utils.OrmLiteEntityScanner;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class TablesInitializer {

    public TablesInitializer(String entityPackage) throws SQLException {

        log.info("Initializing database tables for package: {}", entityPackage);

        log.info("Creating connection source...");
        JdbcPooledConnectionSource connectionSource = createConnectionSource();

        log.info("Scanning for entities in package: {}", entityPackage);
        List<Class<?>> entities = OrmLiteEntityScanner.findEntities(entityPackage);

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

    private JdbcPooledConnectionSource createConnectionSource() throws SQLException {

        DataBaseConfiguration config = DataBaseConfiguration.INSTANCE;

        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(config.JDBC_URL);
        connectionSource.setUsername(config.JDBC_USER);
        connectionSource.setPassword(config.JDBC_PASSWORD);

        return connectionSource;

    }

}
