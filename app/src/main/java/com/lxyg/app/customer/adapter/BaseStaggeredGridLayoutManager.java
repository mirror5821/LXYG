package com.lxyg.app.customer.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

/**
 * Created by 王沛栋 on 2015/11/24.
 */
public class BaseStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

    private UltimateViewAdapter mAdapter;
    public BaseStaggeredGridLayoutManager(int spanCount, int orientation,UltimateViewAdapter mAdapter) {
        super(spanCount,orientation);
        this.mAdapter = mAdapter;
        setOrientation(orientation);


    }

    protected class VIEW_TYPES {
        public static final int NORMAL = 0;
        public static final int HEADER = 1;
        public static final int FOOTER = 2;
        public static final int CHANGED_FOOTER = 3;
    }



    public BaseStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BaseStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }
}
