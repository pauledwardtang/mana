package io.phatcat.mana.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Simple network interface, this project isn't asking for a whole lot
 */
public interface RecipeApi {
    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
