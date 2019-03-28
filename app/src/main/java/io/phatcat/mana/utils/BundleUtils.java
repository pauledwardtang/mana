package io.phatcat.mana.utils;

import android.os.Bundle;

import androidx.annotation.NonNull;
import io.phatcat.mana.model.Step;
import io.phatcat.mana.view.Intents;

public class BundleUtils {
    // Introduction
    public static final int DEFAULT_STEP_NUMBER = 0;

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

    /**
     * Attempts to retrieve the current step number, returning the default value of 1 by default.
     * @param bundle The Bundle for which to get the step number.
     * @return The current step number, or 1 if not found.
     */
    public static int getCurrentStepIndex(Bundle bundle) {
        if (bundle != null && bundle.containsKey(Intents.EXTRA_CURRENT_STEP_INDEX)) {
            return bundle.getInt(Intents.EXTRA_CURRENT_STEP_INDEX);
        }
        return DEFAULT_STEP_NUMBER;
    }

    public static Step getStep(Bundle bundle) {
        if (bundle != null && bundle.containsKey(Intents.EXTRA_STEP)) {
            return (Step) bundle.getSerializable(Intents.EXTRA_STEP);
        }
        return null;
    }

    /**
     * Utility class for building bundles with common extras and actions.
     */
    public static class Builder {
        private Bundle bundle;

        private Builder() {
            this(new Bundle());
        }

        private Builder(@NonNull Bundle bundle) {
            this.bundle = bundle;
        }

        /**
         * Creates a new Builder instance and underlying Bundle.
         * @return
         */
        public static Builder create() {
            return new Builder();
        }

        /**
         * Creates a Builder instance using a non-null source Bundle. Bundles are REFERENCED, not
         * cloned!!
         * @param bundle The source bundle to reference
         * @return The constructed builder
         */
        public static Builder proxy(@NonNull Bundle bundle) {
            return new Builder(bundle);
        }

        public Builder putRecipeId(long recipeId) {
            bundle.putLong(Intents.EXTRA_RECIPE_ID, recipeId);
            return this;
        }

        public Builder putRecipeName(@NonNull String recipeName) {
            bundle.putString(Intents.EXTRA_RECIPE_NAME, recipeName);
            return this;
        }

        public Builder putStep(Step step) {
            bundle.putSerializable(Intents.EXTRA_STEP, step);
            return this;
        }

        public Builder putCurrentStepIndex(int stepIndex) {
            bundle.putInt(Intents.EXTRA_CURRENT_STEP_INDEX, stepIndex);
            return this;
        }

        public Bundle build() { return bundle; }
    }
}
