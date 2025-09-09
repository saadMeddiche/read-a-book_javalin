package org.saadMeddiche.responses;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParagraphResponse {

    public Long id;

    public String content;

    public ParagraphResponse(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getLong("id");
        this.content = resultSet.getString("content");
    }

}
