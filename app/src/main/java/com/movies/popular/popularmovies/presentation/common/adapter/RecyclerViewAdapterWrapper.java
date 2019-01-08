package com.movies.popular.popularmovies.presentation.common.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

public class RecyclerViewAdapterWrapper extends RecyclerView.Adapter {

    @SuppressWarnings("WeakerAccess")
    @NonNull
    protected final RecyclerView.Adapter<RecyclerView.ViewHolder> wrapped;

    @SuppressWarnings("WeakerAccess")
    public RecyclerViewAdapterWrapper(@NonNull RecyclerView.Adapter<RecyclerView.ViewHolder> wrapped) {
        super();
        this.wrapped = wrapped;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return wrapped.onCreateViewHolder(parent, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        wrapped.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        return wrapped.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        wrapped.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return wrapped.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return wrapped.getItemCount();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        wrapped.onViewRecycled(holder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        return wrapped.onFailedToRecycleView(holder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        wrapped.onViewAttachedToWindow(holder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        wrapped.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        wrapped.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        wrapped.onDetachedFromRecyclerView(recyclerView);
    }

    @SuppressWarnings("WeakerAccess")
    public RecyclerView.Adapter getWrappedAdapter() {
        return wrapped;
    }
}

