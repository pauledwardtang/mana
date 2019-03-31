package io.phatcat.mana.view.recipes;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import io.phatcat.mana.R;
import io.phatcat.mana.databinding.RecipeCardBinding;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.utils.StringUtils;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {
    private static final String TAG = RecipeListAdapter.class.getName();
    private OnRecipeDataClickedListener clickListener;
    private List<RecipeData> data;

    public RecipeListAdapter(List<RecipeData> data, @NonNull OnRecipeDataClickedListener clickListener) {
        this.clickListener = clickListener;
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.recipe_card;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeData recipeData = data.get(position);
        holder.bind(recipeData);
        holder.binding.cardView.setOnClickListener(v -> clickListener.onClick(holder.binding.getData()));
        loadImage(holder, recipeData.recipe.imageUrl);
    }

    public void setRecipes(List<RecipeData> recipes) {
        if (recipes != null) {
            this.data = recipes;
            notifyDataSetChanged();
        }
    }

    private void loadImage(RecipeViewHolder holder, String url) {
        if (StringUtils.isNotBlank(url)) {
            Picasso.get().load(url).fit()
                    .into(holder.binding.cardIcon, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.binding.titleFirstLetter.setVisibility(View.GONE);
                        }

                        @Override public void onError(Exception e) {
                            Log.e(TAG, "Error:", e);
                        }
                    });
        }
    }

    interface OnRecipeDataClickedListener {
        void onClick(RecipeData data);
    }

    static final class RecipeViewHolder extends RecyclerView.ViewHolder {
        private RecipeCardBinding binding;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        void bind(RecipeData data) {
            binding.setData(data);

        }
    }
}
