package org.saadMeddiche.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.saadMeddiche.constants.Tables;

@DatabaseTable(tableName = Tables.CHAPTERS)
@Builder @NoArgsConstructor @AllArgsConstructor
public class Chapter {

    @DatabaseField(generatedId = true)
    public Long id;

    @DatabaseField(canBeNull = false)
    public String title;

    @ForeignCollectionField
    private ForeignCollection<Page> pages;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    public Book book;

}