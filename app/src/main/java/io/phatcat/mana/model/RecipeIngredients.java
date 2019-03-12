package io.phatcat.mana.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(tableName = "recipe_ingredients", foreignKeys = {
        @ForeignKey(
                entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipeId",
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = Measure.class,
                parentColumns = "id",
                childColumns = "measureId",
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = Ingredient.class,
                parentColumns = "id",
                childColumns = "ingredientId",
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE
        )
},
    primaryKeys={"recipeId", "measureId", "ingredientId"},
    indices={@Index(value="recipeId"), @Index(value="measureId"), @Index(value="ingredientId")})
public class RecipeIngredients {
    public long recipeId;
    public long measureId;
    public long ingredientId;
    public double quantity;

    @Ignore
    public RecipeIngredients(double quantity) {
        this(0, 0, 0, quantity);
    }

    public RecipeIngredients(long recipeId, long measureId, long ingredientId, double quantity) {
        this.recipeId = recipeId;
        this.measureId = measureId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
    }
}
