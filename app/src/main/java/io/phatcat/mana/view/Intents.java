package io.phatcat.mana.view;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.view.details.RecipeDetailsActivity;
import io.phatcat.mana.view.guidedsteps.GuidedRecipeStepActivity;

/**
 * Helper class to encapsulate bundle arguments for consistency
 */
public class Intents {

    public static final String EXTRA_RECIPE_ID = "io.phatcat.mana.extra.RECIPE_ID";
    public static final String EXTRA_RECIPE_NAME = "io.phatcat.mana.extra.RECIPE_NAME";
    public static final String EXTRA_CURRENT_STEP_INDEX = "io.phatcat.mana.extra.CURRENT_STEP_INDEX";
    public static final String EXTRA_STEP = "io.phatcat.mana.extra.STEP";

    // Disable instantiation
    private Intents() {}

    public static Intent getDetailsIntent(Context context, long recipeId, @NonNull String recipeName) {
        return new Intent(context, RecipeDetailsActivity.class)
                .putExtra(EXTRA_RECIPE_ID, recipeId)
                .putExtra(EXTRA_RECIPE_NAME, recipeName);
    }

    public static Intent getGuidedRecipeStepIntent(Context context, long recipeId,
                                                   @NonNull String recipeName) {
        return new Intent(context, GuidedRecipeStepActivity.class)
                .putExtra(EXTRA_RECIPE_ID, recipeId)
                .putExtra(EXTRA_RECIPE_NAME, recipeName);
    }
}
