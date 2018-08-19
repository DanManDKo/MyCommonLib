package com.movies.popular.popularmovies.presentation.common.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.movies.popular.popularmovies.R;
import com.movies.popular.popularmovies.databinding.ItemLoadingBinding;

import timber.log.Timber;

/**
 * Created with Android Studio.
 * User: Sasha Shcherbinin
 * Date: 9/30/17
 * Time: 11:07 AM
 */

public class LoadingAdapter extends RecyclerViewAdapterWrapper {

    private static final int ITEM_TYPE_LOADING = -1000;

    private final RecyclerView.AdapterDataObserver observer;

    private boolean loading = false;

    private boolean observerAdded;

    public LoadingAdapter(RecyclerView.Adapter adapter) {
        //noinspection unchecked
        super(adapter);
        observer = new RecyclerView.AdapterDataObserver() {

            public void onChanged() {
                notifyDataSetChanged();
            }

            public void onItemRangeChanged(int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
            }

            public void onItemRangeInserted(int positionStart, int itemCount) {
                Timber.d("onItemRangeInserted " + positionStart + " " + itemCount);
                notifyItemRangeInserted(positionStart, itemCount);
            }

            public void onItemRangeRemoved(int positionStart, int itemCount) {
                Timber.d("onItemRangeRemoved " + positionStart + " " + itemCount);
                if (positionStart == 0 && itemCount >= wrapped.getItemCount()) {
                    notifyDataSetChanged();
                } else {
                    notifyItemRangeRemoved(positionStart, itemCount);
                }
            }

            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                notifyItemMoved(fromPosition, toPosition);
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOADING) {
            ItemLoadingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_loading, parent, false);
            return new LoadingViewHolder(binding);
        } else {
            return wrapped.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof LoadingViewHolder)) {
            wrapped.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadingPosition(position)) {
            return ITEM_TYPE_LOADING;
        } else {
            return wrapped.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = wrapped.getItemCount();
        return loading ? itemCount + 1 : itemCount;
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (!(holder instanceof LoadingViewHolder)) {
            wrapped.onViewRecycled(holder);
        }
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        if (!observerAdded) {
            this.wrapped.registerAdapterDataObserver(this.observer);
            observerAdded = true;
        }
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        if (observerAdded) {
            this.wrapped.unregisterAdapterDataObserver(this.observer);
        }
    }

    private int getLoadingPosition() {
        return getItemCount() - 1;
    }

    private boolean isLoadingPosition(int position) {
        return loading && position == getLoadingPosition();
    }

    public RecyclerView.LayoutManager getGridLayoutManager(Context context) {
        GridLayoutManager layout = new GridLayoutManager(context, 2);
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (isLoadingPosition(position)) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        return layout;
    }

    public void updateLoading(boolean loading) {
        if (this.loading == loading) {
            try {
                notifyItemChanged(getLoadingPosition());
            } catch (Throwable e) {
                // ignore
            }
        } else {
            this.loading = loading;
            try {
                if (loading) {
                    notifyItemInserted(getLoadingPosition());
                } else {
                    notifyItemRemoved(getLoadingPosition());
                }
            } catch (Throwable e) {
                // ignore
            }
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        LoadingViewHolder(ItemLoadingBinding binding) {
            super(binding.getRoot());
        }

    }
}