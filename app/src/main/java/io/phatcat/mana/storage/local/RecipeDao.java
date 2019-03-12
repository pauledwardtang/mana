package io.phatcat.mana.storage.local;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import io.phatcat.mana.model.Ingredient;
import io.phatcat.mana.model.Measure;
import io.phatcat.mana.model.Recipe;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.model.RecipeIngredients;
import io.phatcat.mana.model.RecipeIngredientsData;
import io.phatcat.mana.model.Step;

@Dao
public abstract class RecipeDao {
    @Transaction
    @Query("SELECT * from recipe")
    abstract LiveData<List<RecipeData>> getAllRecipeData();

    @Transaction
    @Query("SELECT * FROM recipe WHERE id=:id")
    abstract LiveData<RecipeData> getRecipeData(long id);

    @Query("SELECT * from recipe")
    abstract LiveData<List<Recipe>> getAllRecipes();

    @Transaction
    @Query("SELECT * from recipe")
    abstract List<RecipeData> getRecipes();

    @Query("SELECT * FROM recipe_step where recipeId=:recipeId")
    abstract LiveData<List<Step>> findStepsByRecipeId(long recipeId);

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    abstract long insert(Recipe recipe);

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    abstract long insert(RecipeIngredients recipeIngredients);

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    abstract long insert(Step step);

    @Query("SELECT id FROM ingredient WHERE name=:name")
    abstract long getIngredientIdByName(String name);

    @Insert(onConflict=OnConflictStrategy.IGNORE)
    abstract long insert(Ingredient ingredient);

    @Query("SELECT id FROM measure WHERE shortName=:shortName")
    abstract long getMeasureIdByName(String shortName);

    @Insert(onConflict=OnConflictStrategy.IGNORE)
    abstract long insert(Measure measure);

    /**
     * Inserts RecipeData and updates reference IDs to RecipeData
     * @param recipeData List of recipe information to store in database
     */
    @Transaction
    public void insert(List<RecipeData> recipeData) {
        for (RecipeData data: recipeData) {
            long recipeId = insert(data.recipe);
            if (recipeId < 0) {
                // Recipe exists, skip - but should probably just delete and replace!
                continue;
            }

            data.recipe.id = recipeId;

            for (Step step: data.recipeSteps) {
                step.recipeId = recipeId;
                insert(step);
            }
            for (RecipeIngredientsData ingredientsData: data.recipeIngredients) {
                Ingredient ingredient = ingredientsData.getIngredient();
                long ingredientId = insert(ingredient);
                if (ingredientId < 0) {
                    // Ingredient already exists, so use existing id instead
                    ingredientId = getIngredientIdByName(ingredient.name);
                }
                ingredient.id = ingredientId;

                Measure measure = ingredientsData.getMeasure();
                long measureId = insert(measure);
                if (measureId < 0) {
                    // Measure already exists, so use existing id instead
                    measureId = getMeasureIdByName(measure.shortName);
                }
                measure.id = measureId;

                ingredientsData.recipeIngredients = new RecipeIngredients(
                        recipeId,
                        measureId,
                        ingredientId,
                        ingredientsData.recipeIngredients.quantity);
                ingredientsData.setIngredient(ingredient);
                ingredientsData.setMeasure(measure);
                insert(ingredientsData.recipeIngredients);
            }
        }
    }

}
