package io.phatcat.mana.view.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;
import dagger.android.support.DaggerAppCompatActivity;
import io.phatcat.mana.R;
import io.phatcat.mana.utils.BundleUtils;
import io.phatcat.mana.view.Intents;

public class RecipeDetailsActivity extends DaggerAppCompatActivity {
    private Long recipeId;
    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Bundle bundle = (savedInstanceState == null) ? getIntent().getExtras() : savedInstanceState;
        recipeId = BundleUtils.getRecipeId(bundle);
        recipeName = BundleUtils.getRecipeName(bundle);
        if (recipeId == null) {
            finish();
            return;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(BundleUtils.getRecipeName(bundle));
        }

        RecipeDetailsFragment detailsFragment
                = RecipeDetailsFragment.getInstance(recipeId, recipeName);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipeDetailsFragment, detailsFragment)
                .commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleUtils.Builder.proxy(outState)
                .putRecipeId(recipeId)
                .putRecipeName(recipeName);
    }

}
