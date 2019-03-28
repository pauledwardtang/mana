package io.phatcat.mana.view.guidedsteps;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
import io.phatcat.mana.model.Step;
import io.phatcat.mana.utils.BundleUtils;
import io.phatcat.mana.view.BaseListAdapter.ListItemClickListener;

public class GuidedRecipeStepActivity extends DaggerAppCompatActivity implements ListItemClickListener<Step> {
    private ActivityGuidedRecipeStepBinding binding;

    private boolean isTwoPane;
    private Long recipeId;
    private int currentStepIndex;
    private Step currentStep;

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

        recipeId = BundleUtils.getRecipeId(bundle);
        if (recipeId == null) {
            Toast.makeText(this, R.string.recipe_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentStepIndex = BundleUtils.getCurrentStepIndex(bundle);
        currentStep = BundleUtils.getStep(bundle);

        // Load the fragment if it exists instead of nuking and clearing nav state
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment navFragment = (savedInstanceState == null) ?
                GuidedStepNavigationFragment.getInstance(recipeId, currentStepIndex) :
                fragmentManager.findFragmentById(R.id.navigationView);

        fragmentManager.beginTransaction().replace(R.id.navigationView, navFragment).commit();
        fragmentManager.addOnBackStackChangedListener(() -> {
            // A future implementation can handle the title and nav updating appropriately. Since
            // a new details fragment is created, this would require a change or else memory will
            // stack up for every click on a nav element.
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleUtils.Builder.proxy(outState)
                .putRecipeId(recipeId)
                .putCurrentStepIndex(currentStepIndex)
                .putStep(currentStep);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_recipe_guide, menu);

        // Allow fragments to handle the menu during inflation
        return false;
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
            case R.id.action_next: {
                return false;
            }
            case R.id.action_close: {
                finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Step step) {
        // Don't reload if the step hasn't changed! This will clear the information on orientation change
        if (currentStep != null && currentStep.stepNo == step.stepNo) return;

        currentStep = step;
        binding.drawerLayout.closeDrawers();
        binding.toolbar.setTitle(step.shortDescription);
        loadDetailFragment(step);
    }

    private void loadDetailFragment(Step step) {
        Fragment fragment = GuidedStepDetailsFragment.getInstance(step);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.stepDetailsLayout, fragment)
                // See notes in onCreate
//                .addToBackStack(String.valueOf(currentStepIndex))
                .commit();

    }
}
