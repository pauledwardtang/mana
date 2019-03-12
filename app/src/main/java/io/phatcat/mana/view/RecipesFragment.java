package io.phatcat.mana.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;
import io.phatcat.mana.databinding.FragmentRecipesBinding;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.viewmodel.RecipeViewModel;
import io.phatcat.mana.viewmodel.ViewModelFactory;

public class RecipesFragment extends DaggerFragment {
    @Inject ViewModelFactory viewModelFactory;

    private FragmentRecipesBinding binding;
    private RecipeListAdapter listAdapter;
    private boolean isMasterDetailFlow;

    /**
     * Required empty public constructor
     */
    public RecipesFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipesBinding.inflate(inflater, container, false);

        // TODO Check binding here for isTablet logic!
        isMasterDetailFlow = false;
        View rootView = binding.getRoot();

        listAdapter = new RecipeListAdapter(Collections.emptyList(), recipeData -> {
            // TODO Do a thing
            if (isMasterDetailFlow) {
                // Update detail fragment instead of launching activity
            }
            else {
                startActivity(Intents.getDetailsIntent(getContext(), recipeData.recipe.id,
                        recipeData.recipe.name));
            }
        });
        binding.recipeList.setAdapter(listAdapter);

        RecipeViewModel viewModel = ViewModelProviders.of(getActivity(), viewModelFactory)
                .get(RecipeViewModel.class);

        viewModel.getAllRecipes().observe(this, this::setRecipes);
        return rootView;
    }

    private void setRecipes(List<RecipeData> recipes) {
        if (recipes == null || recipes.isEmpty()) {
            binding.emptyListText.setVisibility(View.VISIBLE);
            binding.recipeList.setVisibility(View.GONE);
        }
        else {
            binding.emptyListText.setVisibility(View.GONE);
            binding.recipeList.setVisibility(View.VISIBLE);
        }
        listAdapter.setRecipes(recipes);
    }

}
