package org.saadMeddiche.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@DatabaseTable(tableName = "todo")
@Builder @NoArgsConstructor @AllArgsConstructor
public class ToDo {

    @DatabaseField(generatedId = true)
    public Long id;

    @DatabaseField(canBeNull = false)
    public String title;

    @DatabaseField()
    public String description;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    public User user;

}
