package org.saadMeddiche.responses;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PageResponse {

    public Long id;

    public PageResponse(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
    }

}
