package io.phatcat.mana.storage;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.lifecycle.LiveData;
import io.phatcat.mana.model.RecipeData;

public class RecipeRepository implements RecipeDataSource {

    private RecipeDataSource localDataSource;

    @Inject
    public RecipeRepository(@Named("local") RecipeDataSource localDataSource) {
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
