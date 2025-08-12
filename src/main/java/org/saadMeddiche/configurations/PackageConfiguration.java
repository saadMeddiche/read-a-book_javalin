package org.saadMeddiche.configurations;

public class PackageConfiguration extends Configuration {

    public static PackageConfiguration INSTANCE = new PackageConfiguration();

    private final String PACKAGE_NAME = prop.getProperty("PACKAGE_NAME");

    public final String ENTITY_PACKAGE = PACKAGE_NAME + prop.getProperty("ENTITY_PACKAGE");

    public final String INITIALIZER_PACKAGE = PACKAGE_NAME + prop.getProperty("INITIALIZER_PACKAGE");

    @Override
    protected String path() {
        return "/package.properties";
    }

}
