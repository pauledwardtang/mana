package io.phatcat.mana.storage.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import io.phatcat.mana.model.Ingredient;
import io.phatcat.mana.model.Measure;
import io.phatcat.mana.model.Recipe;
import io.phatcat.mana.model.RecipeIngredients;
import io.phatcat.mana.model.Step;

@Database(version=1, entities={
        Recipe.class,
        RecipeIngredients.class,
        Step.class,
        Measure.class,
        Ingredient.class})
public abstract class RecipeDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();
}
