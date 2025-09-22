package org.saadMeddiche.responses;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDetailsResponse  {

    public Long id;
    public String title;
    public String author;
    public String summary;
    public byte[] coverImage;

    public BookDetailsResponse(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.title = rs.getString("title");
        this.author = rs.getString("author");
        this.summary = rs.getString("summary");
        this.coverImage = rs.getBytes("cover_image");
    }

}