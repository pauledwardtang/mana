package io.phatcat.mana.view;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import dagger.android.support.DaggerAppCompatActivity;
import io.phatcat.mana.R;
import io.phatcat.mana.databinding.ActivityRecipesBinding;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.utils.BundleUtils;
import io.phatcat.mana.view.details.RecipeDetailsFragment;

public class RecipesActivity extends DaggerAppCompatActivity implements RecipeListAdapter.OnRecipeDataClickedListener {

    private ActivityRecipesBinding binding;
    private @NonNull ActionBar actionBar;

    private boolean isMasterDetailFlow;
    private long recipeId;
    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipes);
        isMasterDetailFlow = binding.recipeDetailsFragment != null;

        actionBar = Objects.requireNonNull(getSupportActionBar());
        if (actionBar != null) {
            actionBar.setIcon(R.mipmap.ic_launcher_round);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setTitle(R.string.app_name);
        }

        // Handle orientation change
        if (savedInstanceState != null) {
            recipeName = BundleUtils.getRecipeName(savedInstanceState);
            if (recipeName != null) {
                recipeId = BundleUtils.getRecipeId(savedInstanceState);
                handleDataLoaded(recipeId, recipeName);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recipeName != null) {
            BundleUtils.Builder.proxy(outState).putRecipeId(recipeId).putRecipeName(recipeName);
        }
    }

    @Override
    public void onClick(RecipeData recipeData) {
        recipeId = recipeData.recipe.id;
        recipeName = recipeData.recipe.name;

        handleDataLoaded(recipeId, recipeName);
    }

    private void handleDataLoaded(long recipeId, String recipeName) {
        if (isMasterDetailFlow) {
            actionBar.setTitle(recipeName);

            // Show the details fragment when the first item is clicked!
            if (binding.recipeDetailsFragment.getVisibility() == View.GONE) {
                binding.recipeDetailsFragment.setVisibility(View.VISIBLE);
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            RecipeDetailsFragment detailsFragment
                    = RecipeDetailsFragment.getInstance(recipeId, recipeName);

            TransitionManager.beginDelayedTransition((ViewGroup) binding.getRoot());
            fragmentManager.beginTransaction()
                    .replace(R.id.recipeDetailsFragment, detailsFragment)
                    .commit();
        }
        else {
            startActivity(Intents.getDetailsIntent(this, recipeId, recipeName));
        }
    }
}
