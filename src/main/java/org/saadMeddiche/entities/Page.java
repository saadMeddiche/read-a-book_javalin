package org.saadMeddiche.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.saadMeddiche.constants.Tables;

@DatabaseTable(tableName = Tables.PAGES)
@Builder @NoArgsConstructor @AllArgsConstructor
public class Page {

    @DatabaseField(generatedId = true)
    public Long id;

    @ForeignCollectionField
    private ForeignCollection<Paragraph> paragraphs;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    public Chapter chapter;

}
