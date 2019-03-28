package io.phatcat.mana.view.details;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;
import io.phatcat.mana.R;
import io.phatcat.mana.databinding.FragmentRecipeDetailsBinding;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.utils.BundleUtils;
import io.phatcat.mana.view.IngredientsListAdapter;
import io.phatcat.mana.view.Intents;
import io.phatcat.mana.view.StepsListAdapter;
import io.phatcat.mana.viewmodel.RecipeViewModel;
import io.phatcat.mana.viewmodel.ViewModelFactory;

public class RecipeDetailsFragment extends DaggerFragment {
    @Inject ViewModelFactory viewModelFactory;

    private FragmentRecipeDetailsBinding binding;
    private boolean isMasterDetailFlow;
    private IngredientsListAdapter ingredientsListAdapter;
    private StepsListAdapter stepsListAdapter;

    private Long recipeId;
    private String recipeName;

    /**
     * Required empty public constructor
     */
    public RecipeDetailsFragment() {}

    public static RecipeDetailsFragment getInstance(long recipeId, @NonNull String recipeName) {
        RecipeDetailsFragment detailsFragment = new RecipeDetailsFragment();

        Bundle bundle = BundleUtils.Builder.create()
                .putRecipeId(recipeId)
                .putRecipeName(recipeName)
                .build();

        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false);
        isMasterDetailFlow = false; // TODO Check binding here for isTablet logic!

        Bundle bundle = (savedInstanceState == null) ? getArguments() : savedInstanceState;
        recipeId = BundleUtils.getRecipeId(bundle);
        recipeName = BundleUtils.getRecipeName(bundle);

        if (recipeId == null) {
            Toast.makeText(getContext(), R.string.recipe_not_found, Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        }
        else {
            initViews(recipeId, recipeName);
        }

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        BundleUtils.Builder.proxy(outState)
                .putRecipeId(recipeId)
                .putRecipeName(recipeName);
    }

    private void initViews(long recipeId, String recipeName) {
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
        stepsListAdapter = new StepsListAdapter(Collections.emptyList(), null);
        binding.ingredientsList.setAdapter(ingredientsListAdapter);
        binding.stepsList.setAdapter(stepsListAdapter);

        // Setup FAB
        binding.fab.setOnClickListener(v -> {
            startActivity(Intents.getGuidedRecipeStepIntent(getContext(), recipeId, recipeName));
        });

    }

    private void setRecipeDetails(RecipeData recipe) {
        binding.setData(recipe);
        ingredientsListAdapter.setList(recipe.recipeIngredients);
        stepsListAdapter.setList(recipe.recipeSteps);
    }
}
