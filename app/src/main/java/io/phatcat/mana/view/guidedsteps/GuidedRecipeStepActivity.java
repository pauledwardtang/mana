package io.phatcat.mana.view.guidedsteps;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import dagger.android.support.DaggerAppCompatActivity;
import io.phatcat.mana.R;
import io.phatcat.mana.databinding.ActivityGuidedRecipeStepBinding;
import io.phatcat.mana.utils.BundleUtils;

public class GuidedRecipeStepActivity extends DaggerAppCompatActivity {
    private ActivityGuidedRecipeStepBinding binding;

    private boolean isTwoPane;
    private int currentStepIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guided_recipe_step);

        // TODO Infer this from layout
        isTwoPane = false;

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = (savedInstanceState == null) ? getIntent().getExtras() : savedInstanceState;
        if (isTwoPane) {
            String recipeName = BundleUtils.getRecipeName(bundle);
            actionBar.setTitle(recipeName);
            binding.toolbar.setTitle(recipeName);
        }
        else {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }

        Long recipeId = BundleUtils.getRecipeId(bundle);
        if (recipeId == null) {
            Toast.makeText(this, R.string.recipe_not_found, Toast.LENGTH_SHORT).show();
            finish();
        }

        currentStepIndex = BundleUtils.getCurrentStepIndex(bundle);
        Fragment fragment = GuidedStepNavigationFragment.getInstance(recipeId, currentStepIndex);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.navigationView, fragment).commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleUtils.putCurrentStepIndex(outState, currentStepIndex);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!isTwoPane)
                    binding.drawerLayout.openDrawer(GravityCompat.START);
                else {
                    // Interpret as back button
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
