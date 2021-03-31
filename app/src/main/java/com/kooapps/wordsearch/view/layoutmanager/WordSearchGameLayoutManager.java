package com.kooapps.wordsearch.view.layoutmanager;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

public class WordSearchGameLayoutManager extends GridLayoutManager {

    private boolean mScrollable;

    public WordSearchGameLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public boolean isScrollable() {
        return mScrollable;
    }

    public void setScrollable(boolean isScrollable) {
        mScrollable = isScrollable;
    }

    @Override
    public boolean canScrollHorizontally() {
        return super.canScrollHorizontally() && isScrollable();
    }

    @Override
    public boolean canScrollVertically() {
        return super.canScrollVertically() && isScrollable();
    }
}
