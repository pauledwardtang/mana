package io.phatcat.mana.storage.local;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.storage.RecipeDataSource;

public class RecipeLocalDataSource implements RecipeDataSource {
    private RecipeDao dao;

    @Inject
    public RecipeLocalDataSource(RecipeDao dao) {
        this.dao = dao;
    }

    @Override
    public LiveData<List<RecipeData>> getAllRecipeData() {
        return dao.getAllRecipeData();
    }

    @Override
    public LiveData<RecipeData> getRecipeData(long recipeId) {
        return dao.getRecipeData(recipeId);
    }

    @Override
    public void addRecipeData(List<RecipeData> recipeData) {
        dao.insert(recipeData);
    }

}
