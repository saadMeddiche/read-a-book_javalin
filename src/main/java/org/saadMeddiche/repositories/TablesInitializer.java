package org.saadMeddiche.repositories;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.saadMeddiche.configurations.DataBaseConnectionConfiguration;
import org.saadMeddiche.utils.OrmLiteEntityScanner;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TablesInitializer {

    private final String entityPackage;

    public void initialize() throws SQLException {
        DataBaseConnectionConfiguration config = DataBaseConnectionConfiguration.INSTANCE;

        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(config.JDBC_URL);
        connectionSource.setUsername(config.JDBC_USER);
        connectionSource.setPassword(config.JDBC_PASSWORD);

        List<Class<?>> entities = OrmLiteEntityScanner.findEntities(entityPackage);

        for (Class<?> entity : entities) {
            TableUtils.createTableIfNotExists(connectionSource, entity);
            log.info("Initialized table for: {}" , entity.getSimpleName());
        }

    }

}
