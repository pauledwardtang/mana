package io.phatcat.mana.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Base list adapter for view binding list items.
 * @param <T>
 * @param <V>
 */
public abstract class BaseListAdapter<T, V extends BaseViewHolder<T>> extends RecyclerView.Adapter {
    private List<T> list;
    private ListItemClickListener<T> listItemClickListener;

    public BaseListAdapter(@NonNull List<T> list) {
        this(list, null);
    }

    public BaseListAdapter(@NonNull List<T> list, ListItemClickListener<T> listItemClickListener) {
        this.list = list;
        this.listItemClickListener = listItemClickListener;
    }

    /**
     * Required for proper layout inflation. This class assumes views are homogeneous.
     * @param position Item position, although should be irrelevant.
     * @return A homogeneous resource ID
     */
    @Override
    public abstract int getItemViewType(int position);

    /**
     * Factory method for creating a ViewHolder of type V.
     * @return the viewholder to hold bindings
     */
    protected abstract V createViewHolder(View view);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        T item = list.get(position);
        V parameterizedHolder = (V) holder;
        parameterizedHolder.bind(item, position);

        if (listItemClickListener != null) {
            parameterizedHolder.itemView.setOnClickListener(v -> listItemClickListener.onClick(item));
        }

        onViewHolderBound(parameterizedHolder, position);
    }

    /**
     * Optional callback after ViewHolder has been bound.
     * @param holder
     */
    public void onViewHolderBound(V holder, int position) {}

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    public void setList(List<T> list) {
        if (list != null) {
            this.list = list;
            notifyDataSetChanged();
        }
    }

    public interface ListItemClickListener<T> {
        void onClick(T item);
    }

}
