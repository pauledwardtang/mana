package io.phatcat.mana.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName="recipe_step", foreignKeys={
        @ForeignKey(
                entity=Recipe.class,
                parentColumns="id",
                childColumns="recipeId",
                onUpdate=ForeignKey.CASCADE,
                onDelete=ForeignKey.CASCADE
        )
},
        primaryKeys={"recipeId", "stepNo"},
        indices={@Index(value="recipeId")})
public class Step {
    public long recipeId;
    public int stepNo;
    @NonNull public String shortDescription;
    @NonNull public String description;
    @Nullable public String videoUrl;
    @Nullable public String thumbnailUrl;
}
