package org.saadMeddiche.configurations;

public class DataBaseConnectionConfiguration extends Configuration {

    public final String JDBC_URL = prop.getProperty("JDBC_URL");
    public final String JDBC_USER = prop.getProperty("JDBC_USER");
    public final String JDBC_PASSWORD = prop.getProperty("JDBC_PASSWORD");

    @Override
    protected String path() {
        return "/application.properties";
    }

}
