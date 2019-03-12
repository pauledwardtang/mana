package io.phatcat.mana.viewmodel;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import io.phatcat.mana.AppComponent;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.network.Recipe;
import io.phatcat.mana.network.RecipeNetworkService;
import io.phatcat.mana.storage.RecipeRepository;
import io.phatcat.mana.utils.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeViewModel extends ViewModel {
    private RecipeRepository recipeRepository;
    private RecipeNetworkService networkService;

    @Inject
    public RecipeViewModel(RecipeRepository recipeRepository, RecipeNetworkService networkService) {
        this.recipeRepository = recipeRepository;
        this.networkService = networkService;
    }

    // TODO THe repository should probably be doing this and NOT the viewmodel?
    public void loadRecipes(LoadRecipesCallback callback) {
        networkService.loadRecipes(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    List<RecipeData> recipeData = NetworkUtils.from(response.body());
                    recipeRepository.addRecipeData(recipeData);
                    if (callback != null) callback.onSuccess();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, Throwable t) {
                if (callback != null) callback.onFailure(t);
            }
        });
    }

    public LiveData<RecipeData> getRecipeData(long recipeId) {
        return recipeRepository.getRecipeData(recipeId);
    }

    public LiveData<List<RecipeData>> getAllRecipes() {
        return recipeRepository.getAllRecipeData();
    }

    public void addRecipes(List<RecipeData> recipeData) {
        recipeRepository.addRecipeData(recipeData);
    }

    @VisibleForTesting
    RecipeNetworkService getNetworkService() {
        return networkService;
    }

    public interface LoadRecipesCallback {
        void onSuccess();
        void onFailure(Throwable t);
    }
}
