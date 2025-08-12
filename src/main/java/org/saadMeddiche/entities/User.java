package org.saadMeddiche.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User {

    @DatabaseField(generatedId = true)
    public Long id;

    @DatabaseField(canBeNull = false)
    public String firstName;

    @DatabaseField(canBeNull = false)
    public String lastName;

    @DatabaseField(canBeNull = false, unique = true)
    public String username;

    @DatabaseField(canBeNull = false, unique = true)
    public String email;

    @DatabaseField(canBeNull = false)
    public String password;

    @ForeignCollectionField
    private ForeignCollection<ToDo> toDos;

    public String fullName() {
        return firstName + " " + lastName;
    }

}
