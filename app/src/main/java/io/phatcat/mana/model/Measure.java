package io.phatcat.mana.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "measure",
        indices={@Index(value="shortName", unique=true), @Index(value="fullName", unique=true)})
public class Measure {
    @PrimaryKey(autoGenerate=true)
    public long id;
    public String shortName;
    public String fullName;

    public Measure() {}

    @Ignore
    public Measure(String shortName, String fullName) {
        this.shortName = shortName;
        this.fullName = fullName;
    }
}
