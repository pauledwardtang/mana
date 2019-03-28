package io.phatcat.mana.view;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import io.phatcat.mana.R;
import io.phatcat.mana.databinding.StepsListItemBinding;
import io.phatcat.mana.model.Step;

/**
 * Adapter for displaying steps. Steps can be made selectable by enabling focus in constructor
 * arguments. Note: StepsListAdapter::reset must be called after using an adapter. Multiple
 * focusable lists are not supported by this class!
 */
public class StepsListAdapter
        extends BaseListAdapter<Step, StepsListAdapter.StepViewHolder> {
    private static final String TAG = StepsListAdapter.class.getName();
    private static final int DEFAULT_STEP_NUMBER = 0;

    private boolean shouldFocusItems;
    public static int SELECTED_ITEM;

    public StepsListAdapter(@NonNull List<Step> list,
                            ListItemClickListener<Step> listItemClickListener) {
        this(list, listItemClickListener, false, DEFAULT_STEP_NUMBER);
    }

    /**
     * @param list The list of steps to display
     * @param listItemClickListener An optional listener for steps clicked
     * @param shouldFocusItems Whether or not items should stay focused in this list when clicked.
     * @param selectedItem An optional item to have selected by default.
     */
    public StepsListAdapter(@NonNull List<Step> list,
                            @Nullable ListItemClickListener<Step> listItemClickListener,
                            boolean shouldFocusItems,
                            int selectedItem) {
        super(list, listItemClickListener);
        this.shouldFocusItems = shouldFocusItems;
        SELECTED_ITEM = selectedItem;

        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.steps_list_item;
    }

    @Override protected StepViewHolder createViewHolder(View view) {
        return new StepViewHolder(view, shouldFocusItems);
    }

    @Override
    public void onViewHolderBound(StepViewHolder holder, int position) {
        if (shouldFocusItems)
            holder.setSelectedByStepNumber(SELECTED_ITEM);
    }

    public void setSelectedItem(RecyclerView recyclerView, int stepNo) {
        StepViewHolder holder = (StepViewHolder) recyclerView.findViewHolderForItemId(stepNo);
        if (holder != null && shouldFocusItems) {
            holder.selectStep();
        }
    }

    /**
     * Callers MUST call this when uninitializing/detaching to avoid inconsistent state.
     */
    public void reset() {
        SELECTED_ITEM = DEFAULT_STEP_NUMBER;
        StepViewHolder.LAST_SELECTED_ITEM = null;
    }

    public static class StepViewHolder extends BaseViewHolder<Step> {
        static View LAST_SELECTED_ITEM;

        private boolean isFocusable;
        private Drawable defaultBackground;
        private int stepNumber;

        public StepViewHolder(@NonNull View itemView, boolean isFocusable) {
            super(itemView);
            this.isFocusable = isFocusable;
            defaultBackground = itemView.getBackground();
        }

        @Override
        public void bind(Step step, int position) {
            StepsListItemBinding binding = (StepsListItemBinding) getBinding();
            binding.setStep(step);

            stepNumber = step.stepNo;

            if (!isFocusable) {
                // Clear focusable from XML
                binding.cardView.setForeground(null);
            }
            else {
                // Focus change listener and onClickListener are not compatible together
                binding.cardView.setOnFocusChangeListener((v, hasFocus) -> {
                    if (hasFocus) {
                        SELECTED_ITEM = binding.getStep().stepNo;
                        setSelectedByStepNumber(SELECTED_ITEM);

                        // Reset the last item
                        setBackground(LAST_SELECTED_ITEM, false);

                        // Manually call here since focus change listener eats the first click
                        v.callOnClick();
                    }
                    else if (isSelected(SELECTED_ITEM)) {
                        LAST_SELECTED_ITEM = v;
                    }
                });
            }
        }

        public void selectStep() {
            if (isFocusable) {
                StepsListItemBinding binding = (StepsListItemBinding) getBinding();
                binding.cardView.requestFocus();
            }
        }

        public void setSelectedByStepNumber(int stepNumber) {
            StepsListItemBinding binding = (StepsListItemBinding) getBinding();

            boolean isSelected = isSelected(stepNumber);

            // Check if first time load
            if (isSelected && LAST_SELECTED_ITEM == null) {
                LAST_SELECTED_ITEM = binding.cardView;

                // Manual call since user isn't clicking the first item shown
                binding.cardView.callOnClick();
            }
            setBackground(binding.cardView, isSelected);
        }

        private boolean isSelected(int stepNumber) {
            return this.stepNumber == stepNumber;
        }

        private void setBackground(View v, boolean isSelected) {
            if (v == null) return;
            try {
                if (isSelected) {
                    v.setBackgroundResource(R.color.colorPrimary);
                }
                else {
                    v.setBackground(defaultBackground);
                }
            }
            catch (Exception e) {
                Log.e(TAG, "Error resetting focused state", e);
            }
        }
    }
}
