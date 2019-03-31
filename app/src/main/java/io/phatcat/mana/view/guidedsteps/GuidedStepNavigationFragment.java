package io.phatcat.mana.view.guidedsteps;


import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;
import io.phatcat.mana.R;
import io.phatcat.mana.databinding.FragmentGuidedStepNavigationBinding;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.model.Step;
import io.phatcat.mana.utils.BundleUtils;
import io.phatcat.mana.view.BaseListAdapter.ListItemClickListener;
import io.phatcat.mana.view.IngredientsListAdapter;
import io.phatcat.mana.view.StepsListAdapter;
import io.phatcat.mana.viewmodel.RecipeViewModel;
import io.phatcat.mana.viewmodel.ViewModelFactory;

public class GuidedStepNavigationFragment extends DaggerFragment {
    @Inject ViewModelFactory viewModelFactory;

    private FragmentGuidedStepNavigationBinding binding;
    private IngredientsListAdapter ingredientsListAdapter;
    private StepsListAdapter stepsListAdapter;

    private RecipeData recipeData;
    private int currentStepNumber;

    /**
     * Required empty public constructor
     */
    public GuidedStepNavigationFragment() {}

    static GuidedStepNavigationFragment getInstance(long recipeId, int currentStepIndex) {
        GuidedStepNavigationFragment fragment = new GuidedStepNavigationFragment();

        fragment.setArguments(BundleUtils.Builder.create()
                .putRecipeId(recipeId)
                .putCurrentStepIndex(currentStepIndex)
                .build());

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGuidedStepNavigationBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        Bundle bundle = (savedInstanceState == null) ? getArguments() : savedInstanceState;
        Long recipeId = BundleUtils.getRecipeId(bundle);
        currentStepNumber = BundleUtils.getCurrentStepIndex(bundle);
        if (recipeId == null) {
            requireActivity().finish();
        }
        else {
            initViews(recipeId, currentStepNumber);
        }

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem nextMenuItem = menu.findItem(R.id.action_next);
        nextMenuItem.setVisible(!isLastStep());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next: {
                // If end of list, this is actually "end"
                int nextStep = currentStepNumber + 1;
                if (nextStep >= recipeData.recipeSteps.size() - 1) {
                    Log.d("NavFrag", "Final element will have been reached!");
                    requireActivity().invalidateOptionsMenu();
                }
                stepsListAdapter.setSelectedItem(binding.stepsList, nextStep);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        BundleUtils.Builder.proxy(outState)
                .putRecipeId(recipeData.recipe.id)
                .putCurrentStepIndex(currentStepNumber);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (stepsListAdapter != null) {
            stepsListAdapter.reset();
        }
    }

    private void initViews(long recipeId, int stepIndex) {
        // Setup ViewModel
        RecipeViewModel viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(RecipeViewModel.class);

        viewModel.getRecipeData(recipeId).observe(this, recipeData -> {
            if (recipeData == null) {
                Toast.makeText(requireContext(), R.string.recipe_not_found, Toast.LENGTH_LONG).show();
                requireActivity().finish();
            }
            else {
                setRecipeDetails(recipeData);
            }
        });

        // Setup list adapters
        ingredientsListAdapter = new IngredientsListAdapter(Collections.emptyList(), null);
        stepsListAdapter = new StepsListAdapter(Collections.emptyList(), this::onStepClicked, true, stepIndex);

        binding.ingredientsList.setAdapter(ingredientsListAdapter);
        binding.stepsList.setAdapter(stepsListAdapter);

        // Setup toggle
        binding.showIngredientsButton.setOnClickListener(this::toggleIngredients);
        binding.hideIngredientsButton.setOnClickListener(this::toggleIngredients);
    }

    /**
     * Shows/hides the list of ingredients
     * @param toggleButton Button to toggle
     */
    private void toggleIngredients(@NonNull View toggleButton) {
        TransitionManager.beginDelayedTransition(binding.ingredientsList);
        if (toggleButton.equals(binding.hideIngredientsButton)) {
            binding.ingredientsList.setVisibility(View.GONE);
            binding.hideIngredientsButton.setVisibility(View.GONE);
            binding.showIngredientsButton.setVisibility(View.VISIBLE);
        }
        else if (toggleButton.equals(binding.showIngredientsButton)) {
            binding.ingredientsList.setVisibility(View.VISIBLE);
            binding.showIngredientsButton.setVisibility(View.GONE);
            binding.hideIngredientsButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Binds/refreshes all data
     * @param recipe Details to populate
     */
    private void setRecipeDetails(RecipeData recipe) {
        recipeData = recipe;
        ingredientsListAdapter.setList(recipe.recipeIngredients);
        stepsListAdapter.setList(recipe.recipeSteps);
    }

    private void onStepClicked(Step step) {
        currentStepNumber = step.stepNo;
        requireActivity().invalidateOptionsMenu();

        ListItemClickListener<Step> listener = (ListItemClickListener<Step>) requireActivity();
        listener.onClick(step);
    }

    private boolean isLastStep() {
        return recipeData != null && currentStepNumber >= recipeData.recipeSteps.size() - 1;
    }
}
