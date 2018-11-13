package com.artem.mindvalleysample.downloader.components;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemDecoration extends RecyclerView.ItemDecoration {

    private int columnCount;
    private int height;

    public ItemDecoration(int columnCount, int height) {
        this.columnCount = columnCount;
        this.height = height;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // Sets the "inset" of the top views in the grid
        if (parent.getChildPosition(view) < columnCount) {
            outRect.set(0, height, 0, 0);
        } else {
            outRect.set(0, 0, 0, 0);
        }

    }

}
