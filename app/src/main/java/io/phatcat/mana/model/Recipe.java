package io.phatcat.mana.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe")
public class Recipe {
    @PrimaryKey(autoGenerate=true)
    public long id;
    public String name;
    public double servings;
    public String imageUrl;

    @Ignore
    public Recipe(String name, double servings, String imageUrl) {
        this.name = name;
        this.servings = servings;
        this.imageUrl = imageUrl;
    }

    public Recipe(long id, String name, double servings, String imageUrl) {
        this(name, servings, imageUrl);
        this.id = id;
    }
}
