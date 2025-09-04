package org.saadMeddiche.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.saadMeddiche.constants.Tables;

@DatabaseTable(tableName = Tables.BOOKS)
@Builder @NoArgsConstructor @AllArgsConstructor
public class Book {

    @DatabaseField(generatedId = true)
    public Long id;

    @DatabaseField(canBeNull = false, unique = true)
    public String title;

    @DatabaseField(canBeNull = false)
    public String author;

    @DatabaseField()
    public String summary;

}