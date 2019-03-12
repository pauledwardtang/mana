package io.phatcat.mana.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredient", indices={@Index(value="name", unique=true)})
public class Ingredient {
    @PrimaryKey(autoGenerate=true)
    public long id;
    public String name;

    public Ingredient() {}

    @Ignore
    public Ingredient(String name) {
        this.name = name;
    }
}
