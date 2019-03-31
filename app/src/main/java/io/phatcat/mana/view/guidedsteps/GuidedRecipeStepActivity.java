package io.phatcat.mana.view.guidedsteps;

import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
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
        isTwoPane = binding.drawerLayout.getVisibility() == View.GONE;

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = (savedInstanceState == null) ? getIntent().getExtras() : savedInstanceState;
        setIntent(getIntent().putExtras(bundle));

        recipeId = BundleUtils.getRecipeId(bundle);
        currentStepIndex = BundleUtils.getCurrentStepIndex(bundle);
        currentStep = BundleUtils.getStep(bundle);
        if (recipeId == null) {
            Toast.makeText(this, R.string.recipe_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (isTwoPane) {
            String recipeName = BundleUtils.getRecipeName(bundle);
            setSupportTitle(recipeName);
        }
        else {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            if (currentStep != null) {
                setSupportTitle(currentStep.shortDescription);
            }
            else {
                // This is done because of a Toolbar bug that causes text to be truncated when
                // a default title is swapped with a longer title. In this case, "MANA" is displayed
                // first, then "Recipe Introduction" is set (usually). This becomes truncated to "Reci"
                // TLDR: Setting text after onCreate on first layout will truncate past 4 chars.
                setSupportTitle(getString(R.string.default_title_text));
            }
        }

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
        BundleUtils.Builder proxy = BundleUtils.Builder.proxy(outState)
                .putRecipeId(recipeId)
                .putCurrentStepIndex(currentStepIndex)
                .putStep(currentStep);

        if (isTwoPane) {
            proxy.putRecipeName(getSupportActionBar().getTitle().toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_guide, menu);

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
        if (!isTwoPane) {
            binding.drawerLayout.closeDrawers();
            setSupportTitle(step.shortDescription);
        }

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

    private void setSupportTitle(CharSequence title) {
        TransitionManager.beginDelayedTransition(binding.toolbar);
        getSupportActionBar().setTitle(title);
    }
}
