package io.phatcat.mana.storage.local;

import java.util.Collections;
import java.util.List;

import io.phatcat.mana.model.Ingredient;
import io.phatcat.mana.model.Measure;
import io.phatcat.mana.model.Recipe;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.model.RecipeIngredients;
import io.phatcat.mana.model.RecipeIngredientsData;
import io.phatcat.mana.model.Step;

public class TestUtil {

    public static RecipeData createRecipeData(long recipeId) {
        return createRecipeData(
                new Recipe(recipeId, "Brownies", 2.0, null),
                createRecipeIngredientsData(recipeId),
                createStep(recipeId));
    }

    public static RecipeData createRecipeData(Recipe recipe,
                                              RecipeIngredientsData ingredientsData, Step step) {
        RecipeData data = new RecipeData();
        data.recipe = recipe;
        data.recipeIngredients = Collections.singletonList(ingredientsData);
        data.recipeSteps = Collections.singletonList(step);
        return data;
    }

    public static RecipeData createRecipeData(Recipe recipe,
                                              List<RecipeIngredientsData> ingredientsData, List<Step> steps) {
        RecipeData data = new RecipeData();
        data.recipe = recipe;
        data.recipeIngredients = ingredientsData;
        data.recipeSteps = steps;
        return data;
    }

    public static Step createStep(long recipeId) {
        Step step = new Step();
        step.recipeId = recipeId;
        step.stepNo = 0;
        step.shortDescription = "Short description";
        step.description = "Long description";
        step.videoUrl = null;
        step.thumbnailUrl = null;
        return step;
    }

    public static RecipeIngredientsData createRecipeIngredientsData(long recipeId) {
        Ingredient ingredient = TestUtil.createIngredient();
        Measure measure = TestUtil.createMeasure();
        RecipeIngredients recipeIngredients = createRecipeIngredients(recipeId, ingredient.id, measure.id);

        RecipeIngredientsData recipeIngredientsData = new RecipeIngredientsData();
        recipeIngredientsData.setIngredient(ingredient);
        recipeIngredientsData.setMeasure(measure);
        recipeIngredientsData.recipeIngredients = recipeIngredients;
        return recipeIngredientsData;
    }

    public static RecipeIngredients createRecipeIngredients(
            long recipeId, long ingredientId, long measureId) {
        return new RecipeIngredients(recipeId, ingredientId, measureId, 2.0);
    }

    public static Measure createMeasure() {
        Measure measure = new Measure();
        measure.id = 1;
        measure.shortName = "CUPS";
        measure.fullName = "cups";
        return measure;
    }

    public static Ingredient createIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.id = 1;
        ingredient.name = "butter";
        return ingredient;
    }

    public static Ingredient getIngredient(RecipeData data) {
        return data.recipeIngredients.get(0).getIngredient();
    }

    public static Measure getMeasure(RecipeData data) {
        return data.recipeIngredients.get(0).getMeasure();
    }
}
