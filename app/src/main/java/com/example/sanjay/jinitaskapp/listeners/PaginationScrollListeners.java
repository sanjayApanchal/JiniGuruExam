package com.example.sanjay.jinitaskapp.listeners;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public abstract class PaginationScrollListeners extends RecyclerView.OnScrollListener {
    LinearLayoutManager layoutManager;
    Context mContext;


    public PaginationScrollListeners(LinearLayoutManager layoutManager,Context context) {
        this.layoutManager = layoutManager;
        mContext = context;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (firstVisibleItemPosition + visibleItemCount >= totalItemCount ) {

            loadMoreItems();
        }
    }

    protected abstract void loadMoreItems();



}
