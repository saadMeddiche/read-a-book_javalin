package org.saadMeddiche.utils;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.saadMeddiche.configurations.DataBaseConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionProvider {

    private static final DataBaseConfiguration connectionConfiguration = DataBaseConfiguration.INSTANCE;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionConfiguration.JDBC_URL, connectionConfiguration.JDBC_USER, connectionConfiguration.JDBC_PASSWORD);
    }

    public static ConnectionSource getConnectionSource() throws SQLException {
        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(connectionConfiguration.JDBC_URL);
        connectionSource.setUsername(connectionConfiguration.JDBC_USER);
        connectionSource.setPassword(connectionConfiguration.JDBC_PASSWORD);
        return connectionSource;
    }

}
