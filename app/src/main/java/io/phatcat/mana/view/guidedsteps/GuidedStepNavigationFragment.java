package io.phatcat.mana.view.guidedsteps;


import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;
import io.phatcat.mana.R;
import io.phatcat.mana.databinding.FragmentGuidedStepNavigationBinding;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.model.Step;
import io.phatcat.mana.utils.BundleUtils;
import io.phatcat.mana.view.IngredientsListAdapter;
import io.phatcat.mana.view.Intents;
import io.phatcat.mana.view.StepsListAdapter;
import io.phatcat.mana.viewmodel.RecipeViewModel;
import io.phatcat.mana.viewmodel.ViewModelFactory;

public class GuidedStepNavigationFragment extends DaggerFragment {
    @Inject ViewModelFactory viewModelFactory;

    private FragmentGuidedStepNavigationBinding binding;
    private IngredientsListAdapter ingredientsListAdapter;
    private StepsListAdapter stepsListAdapter;

    private RecipeData recipeData;
    private int currentStepIndex = 0;

    /**
     * Required empty public constructor
     */
    public GuidedStepNavigationFragment() {}

    static GuidedStepNavigationFragment getInstance(long recipeId, int currentStepIndex) {
        GuidedStepNavigationFragment fragment = new GuidedStepNavigationFragment();

        Bundle bundle = new Bundle();
        bundle.putLong(Intents.EXTRA_RECIPE_ID, recipeId);
        bundle.putInt(Intents.EXTRA_CURRENT_STEP_INDEX, currentStepIndex);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGuidedStepNavigationBinding.inflate(inflater, container, false);
        Bundle bundle = (savedInstanceState == null) ? getArguments() : savedInstanceState;
        Long recipeId = BundleUtils.getRecipeId(bundle);
        currentStepIndex = BundleUtils.getCurrentStepIndex(bundle);
        if (recipeId == null) {
            requireActivity().finish();
        }
        else {
            initViews(recipeId, currentStepIndex);
        }

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(Intents.EXTRA_RECIPE_ID, getArguments().getLong(Intents.EXTRA_RECIPE_ID));

        // TODO remember expanded ingredients, remember selected step??
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
     * @param view
     */
    public void toggleIngredients(@NonNull View view) {
        TransitionManager.beginDelayedTransition(binding.ingredientsList);
        if (view.equals(binding.hideIngredientsButton)) {
            binding.ingredientsList.setVisibility(View.GONE);
            binding.hideIngredientsButton.setVisibility(View.GONE);
            binding.showIngredientsButton.setVisibility(View.VISIBLE);
        }
        else if (view.equals(binding.showIngredientsButton)) {
            binding.ingredientsList.setVisibility(View.VISIBLE);
            binding.showIngredientsButton.setVisibility(View.GONE);
            binding.hideIngredientsButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Binds/refreshes all data
     * @param recipe
     */
    private void setRecipeDetails(RecipeData recipe) {
        if (recipeData == null) {
            Step currentStep = recipe.recipeSteps.get(currentStepIndex);
            setTitle(currentStep.shortDescription);
        }
        recipeData = recipe;
        ingredientsListAdapter.setList(recipe.recipeIngredients);
        stepsListAdapter.setList(recipe.recipeSteps);
    }

    private void onStepClicked(Step step) {
        if (currentStepIndex != step.stepNo) {
            currentStepIndex = step.stepNo;
            setTitle(step.shortDescription);
        }
    }

    private void setTitle(String title) {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.getSupportActionBar().setTitle(title);
    }

}
