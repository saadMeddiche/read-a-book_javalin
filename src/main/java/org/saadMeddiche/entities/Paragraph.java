package org.saadMeddiche.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.saadMeddiche.constants.Tables;

@DatabaseTable(tableName = Tables.PARAGRAPHS)
@Builder @NoArgsConstructor @AllArgsConstructor
public class Paragraph {

    @DatabaseField(generatedId = true)
    public Long id;

    @DatabaseField(canBeNull = false, columnDefinition = "VARCHAR(1000)")
    public String content;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, index = true)
    public Page page;

}
