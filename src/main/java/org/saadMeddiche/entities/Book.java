package org.saadMeddiche.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
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

    @DatabaseField(canBeNull = false)
    public String title;

    @DatabaseField(canBeNull = false)
    public String author;

    @DatabaseField(columnDefinition = "VARCHAR(1000)")
    public String summary;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    public byte[] coverImage;

    @ForeignCollectionField
    private ForeignCollection<Chapter> chapters;

}