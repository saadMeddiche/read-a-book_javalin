package org.saadMeddiche.configurations;

import kotlin.Pair;

public class DataGeneratorConfiguration extends Configuration {

    public static DataGeneratorConfiguration INSTANCE = new DataGeneratorConfiguration();

    public final boolean IS_ENABLED = Boolean.parseBoolean(prop.getProperty("IS_ENABLED"));

    public final int MAX_CHUNK_SIZE = Integer.parseInt(prop.getProperty("MAX_CHUNK_SIZE"));
    public final int BOOK_COUNT = Integer.parseInt(prop.getProperty("BOOK_COUNT"));

    public final Pair<Integer,Integer> CHAPTERS_COUNT_RANGE = parseRange(prop.getProperty("CHAPTERS_COUNT_RANGE"));
    public final Pair<Integer,Integer> PAGES_COUNT_RANGE = parseRange(prop.getProperty("PAGES_COUNT_RANGE"));
    public final Pair<Integer,Integer> PARAGRAPHS_COUNT_RANGE = parseRange(prop.getProperty("PARAGRAPHS_COUNT_RANGE"));

    @Override
    protected String path() {
        return "/data-generator.properties";
    }

    private Pair<Integer,Integer> parseRange(String range) {
        String[] ranges = range.split(",");
        return new Pair<>(Integer.parseInt(ranges[0]), Integer.parseInt(ranges[1]));
    }

}
