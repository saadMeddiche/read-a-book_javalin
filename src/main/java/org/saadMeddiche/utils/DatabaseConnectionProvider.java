package org.saadMeddiche.utils;

import org.saadMeddiche.configurations.DataBaseConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionProvider {

    private static final DataBaseConfiguration connectionConfiguration = DataBaseConfiguration.INSTANCE;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionConfiguration.JDBC_URL, connectionConfiguration.JDBC_USER, connectionConfiguration.JDBC_PASSWORD);
    }

}
