package com.example.finalproject.ui.home;


import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class CustomLayoutManager extends LinearLayoutManager {

    private static final float MAXIMUM_SCALE = 1f;
    private static final float MILLISECONDS_PER_INCH = 50f; //speed

    public CustomLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scaleChildren();
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        scaleChildren();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getOrientation() == HORIZONTAL) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
            scaleChildren();
            return scrolled;
        } else {
            return 0;
        }
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getOrientation() == VERTICAL) {
            int scrolled = super.scrollVerticallyBy(dy, recycler, state);
            scaleChildren();
            return scrolled;
        } else {
            return 0;
        }
    }

    private void scaleChildren() {
        int[] location = new int[2];
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.getLocationInWindow(location);
            float childMid = location[1] + child.getHeight() / 2.0f;
            float mid = (getHeight() / 2.0f) + 200;
            float distanceFromCenter = Math.abs(mid - childMid);
            float scale = Math.min(MAXIMUM_SCALE,1-Math.min(.5f, (distanceFromCenter / mid) - .7f));
            if (scale != MAXIMUM_SCALE) {
                Log.i("vertscroll", "" + location[1]);
            }
            child.setScaleX(scale);
            child.setScaleY(scale);
        }
    }
}