package org.saadMeddiche.configurations;

public class WebAppConfiguration extends Configuration {

    public static WebAppConfiguration INSTANCE = new WebAppConfiguration();

    public final String ACCESS_CONTROL_ALLOW_ORIGIN = prop.getProperty("ACCESS_CONTROL_ALLOW_ORIGIN");

    public final int PORT = Integer.parseInt(prop.getProperty("PORT")) ;

    @Override
    protected String path() {
        return "/web-app.properties";
    }

}
