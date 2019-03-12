package io.phatcat.mana.utils;

import android.os.Bundle;

import androidx.annotation.NonNull;
import io.phatcat.mana.view.Intents;

public class BundleUtils {

    // Disallow instantiation
    private BundleUtils() {}

    /**
     * Gets a recipe ID from a bundle or null if not found.
     * @param bundle Bundle for which to get the recipe ID
     * @return the recipe ID or null
     */
    public static Long getRecipeId(Bundle bundle) {
        if (bundle != null && bundle.containsKey(Intents.EXTRA_RECIPE_ID)) {
            return bundle.getLong(Intents.EXTRA_RECIPE_ID);
        }
        return null;
    }

    /**
     * Gets the recipe name from a bundle or null if not found
     * @param bundle Bundle for which to get the recipe name
     * @return the recipe name or null
     */
    public static String getRecipeName(Bundle bundle) {
        if (bundle != null && bundle.containsKey(Intents.EXTRA_RECIPE_NAME)) {
            return bundle.getString(Intents.EXTRA_RECIPE_NAME);
        }
        return null;
    }

    public static int getCurrentStepIndex(Bundle bundle) {
        if (bundle != null && bundle.containsKey(Intents.EXTRA_CURRENT_STEP_INDEX)) {
            return bundle.getInt(Intents.EXTRA_CURRENT_STEP_INDEX);
        }
        return 0;
    }

    public static void putCurrentStepIndex(@NonNull Bundle bundle, int stepIndex) {
        bundle.putInt(Intents.EXTRA_CURRENT_STEP_INDEX, stepIndex);
    }
}
