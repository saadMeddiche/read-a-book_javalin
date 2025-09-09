package org.saadMeddiche.responses;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChapterResponse {

    public Long id;

    public String title;

    public ChapterResponse(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.title = rs.getString("title");
    }

}
