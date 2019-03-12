package io.phatcat.mana.storage;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.phatcat.mana.model.RecipeData;

public interface RecipeDataSource {

    /**
     * Get all Recipes and associated data (ingredients and steps)
     * @return All Recipes with fully associated data
     */
    LiveData<List<RecipeData>> getAllRecipeData();

    /**
     * Gets a Recipe by ID
     * @param recipeId The ID to search for.
     * @return LiveData of the recipe
     */
    LiveData<RecipeData> getRecipeData(long recipeId);

    /**
     * Adds recipe data to this data source.
     * @param recipeData The data to add
     */
    void addRecipeData(List<RecipeData> recipeData);
}
