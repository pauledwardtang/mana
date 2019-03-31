package io.phatcat.mana.utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import io.phatcat.mana.model.Ingredient;
import io.phatcat.mana.model.Measure;
import io.phatcat.mana.model.Recipe;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.model.RecipeIngredients;
import io.phatcat.mana.model.RecipeIngredientsData;
import io.phatcat.mana.model.Step;

public class NetworkUtils {

    // Disallow instantiation
    private NetworkUtils() {}

    public static List<RecipeData> from(@NonNull List<io.phatcat.mana.network.Recipe> recipes) {
        List<RecipeData> recipeData = new ArrayList<>();
        for (io.phatcat.mana.network.Recipe recipe: recipes) {
            RecipeData data = new RecipeData();
            Recipe recipeModel = new Recipe(recipe.id, recipe.name, recipe.servings, recipe.image);

            List<RecipeIngredientsData> recipeIngredientsData = new ArrayList<>();
            for (io.phatcat.mana.network.Recipe.Ingredient ingredient: recipe.ingredients) {
                RecipeIngredientsData ingredientsData = new RecipeIngredientsData();

                ingredientsData.recipeIngredients = new RecipeIngredients(ingredient.quantity);
                ingredientsData.setIngredient(new Ingredient(ingredient.ingredient));
                ingredientsData.setMeasure(new Measure(ingredient.measure, ingredient.measure));
                recipeIngredientsData.add(ingredientsData);
            }
            List<Step> recipeSteps = new ArrayList<>();
            for (io.phatcat.mana.network.Recipe.Step step: recipe.steps) {
                Step stepModel = new Step();

                stepModel.recipeId = recipe.id;
                stepModel.stepNo = step.id;
                stepModel.shortDescription = step.shortDescription;
                stepModel.description = step.description;
                stepModel.videoUrl = step.videoURL;
                stepModel.thumbnailUrl = step.thumbnailURL;
                recipeSteps.add(stepModel);
            }

            data.recipe = recipeModel;
            data.recipeIngredients = recipeIngredientsData;
            data.recipeSteps = recipeSteps;
            recipeData.add(data);
        }
        return recipeData;
    }
}
