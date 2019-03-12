package io.phatcat.mana.network;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeNetworkService {
    @Inject RecipeApi recipeApi;
    private boolean shouldEnqueue;

    public RecipeNetworkService(boolean shouldEnqueue) {
        this.shouldEnqueue = shouldEnqueue;
    }

    public void loadRecipes(Callback<List<Recipe>> callback) {
        Call<List<Recipe>> call = recipeApi.getRecipes();
        if (shouldEnqueue) {
            recipeApi.getRecipes().enqueue(callback);
        }
        else {
            try {
                callback.onResponse(call, call.execute());
            } catch (IOException e) {
                e.printStackTrace();
                callback.onFailure(call, e);
            }
        }

    }

    @VisibleForTesting
    @Nullable Response<List<Recipe>> loadRecipes() throws IOException {
        return recipeApi.getRecipes().execute();
    }
}
