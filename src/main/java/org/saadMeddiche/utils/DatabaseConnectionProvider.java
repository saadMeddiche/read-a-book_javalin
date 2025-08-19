package org.saadMeddiche.utils;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.saadMeddiche.configurations.DataBaseConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionProvider {

    private static final DataBaseConfiguration connectionConfiguration = DataBaseConfiguration.INSTANCE;

    private static final HikariDataSource ds;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(connectionConfiguration.JDBC_URL);
        config.setUsername(connectionConfiguration.JDBC_USER);
        config.setPassword(connectionConfiguration.JDBC_PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static ConnectionSource getConnectionSource() throws SQLException {
        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(connectionConfiguration.JDBC_URL);
        connectionSource.setUsername(connectionConfiguration.JDBC_USER);
        connectionSource.setPassword(connectionConfiguration.JDBC_PASSWORD);
        return connectionSource;
    }

}
