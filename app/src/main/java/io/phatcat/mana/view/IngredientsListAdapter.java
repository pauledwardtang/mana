package io.phatcat.mana.view;

import android.view.View;

import java.util.List;

import androidx.annotation.NonNull;
import io.phatcat.mana.R;
import io.phatcat.mana.databinding.IngredientsListItemBinding;
import io.phatcat.mana.model.RecipeIngredientsData;

public class IngredientsListAdapter
        extends BaseListAdapter<RecipeIngredientsData, IngredientsListAdapter.IngredientViewHolder> {

    public IngredientsListAdapter(@NonNull List<RecipeIngredientsData> list,
                                  ListItemClickListener<RecipeIngredientsData> listItemClickListener) {
        super(list, listItemClickListener);
    }

    @Override
    public int getItemViewType(int position) { return R.layout.ingredients_list_item; }

    @Override
    protected IngredientViewHolder createViewHolder(View view) { return new IngredientViewHolder(view); }

    public static class IngredientViewHolder extends BaseViewHolder<RecipeIngredientsData> {
        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bind(RecipeIngredientsData data, int position) {
            IngredientsListItemBinding binding = (IngredientsListItemBinding) getBinding();
            binding.setIndex(position);
            binding.setIngredientsData(data);
        }
    }
}
