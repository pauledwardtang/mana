package io.phatcat.mana.storage;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.phatcat.mana.model.RecipeData;

public class RecipeRepository implements RecipeDataSource {

    private RecipeDataSource localDataSource;

    public RecipeRepository(RecipeDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    @Override
    public LiveData<List<RecipeData>> getAllRecipeData() {
        return localDataSource.getAllRecipeData();
    }

    @Override
    public LiveData<RecipeData> getRecipeData(long recipeId) {
        return localDataSource.getRecipeData(recipeId);
    }

    @Override
    public void addRecipeData(List<RecipeData> recipeData) {
        localDataSource.addRecipeData(recipeData);
    }
}
