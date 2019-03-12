package io.phatcat.mana.view.details;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;
import dagger.android.support.DaggerAppCompatActivity;
import io.phatcat.mana.R;
import io.phatcat.mana.utils.BundleUtils;
import io.phatcat.mana.view.Intents;

public class RecipeDetailsActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Bundle bundle = (savedInstanceState == null) ? getIntent().getExtras() : savedInstanceState;
        Long recipeId = BundleUtils.getRecipeId(bundle);
        String recipeName = BundleUtils.getRecipeName(bundle);
        if (recipeId == null) {
            finish();
            return;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(bundle.getString(Intents.EXTRA_RECIPE_NAME));
        }

        RecipeDetailsFragment detailsFragment
                = RecipeDetailsFragment.getInstance(recipeId, recipeName);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipeDetailsFragment, detailsFragment)
                .commit();
    }
}
