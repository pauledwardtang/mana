package io.phatcat.mana.view.recipes;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;
import io.phatcat.mana.R;
import io.phatcat.mana.databinding.FragmentRecipesBinding;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.viewmodel.RecipeViewModel;
import io.phatcat.mana.viewmodel.ViewModelFactory;

public class RecipesFragment extends DaggerFragment {
    @Inject ViewModelFactory viewModelFactory;

    private RecipeViewModel viewModel;
    private FragmentRecipesBinding binding;
    private RecipeListAdapter listAdapter;

    /**
     * Required empty public constructor
     */
    public RecipesFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipesBinding.inflate(inflater, container, false);

        // TODO Check binding here for isTablet logic!
        View rootView = binding.getRoot();

        listAdapter = new RecipeListAdapter(Collections.emptyList(), recipeData -> {
            // Don't hold a reference to the activity :P
            RecipeListAdapter.OnRecipeDataClickedListener listener
                    = (RecipeListAdapter.OnRecipeDataClickedListener) requireActivity();
            listener.onClick(recipeData);
        });

        binding.recipeList.setAdapter(listAdapter);

        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(RecipeViewModel.class);

        viewModel.getAllRecipes().observe(this, this::setRecipes);
        viewModel.loadRecipes(new RecipeViewModel.LoadRecipesCallback() {
            @Override public void onSuccess() {}
            @Override public void onFailure(Throwable t) {
                Toast.makeText(getContext(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });

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
