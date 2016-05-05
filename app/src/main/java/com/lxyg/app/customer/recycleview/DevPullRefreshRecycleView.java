package com.lxyg.app.customer.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by 王沛栋 on 2015/11/23.
 */
public class DevPullRefreshRecycleView extends PullToRefreshBase<RecyclerView> {
    public DevPullRefreshRecycleView(Context context) {
        super(context);
    }

    public DevPullRefreshRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DevPullRefreshRecycleView(Context context,Mode mode){
        super(context, mode);

    }

    public DevPullRefreshRecycleView(Context context, Mode mode, AnimationStyle animationStyle) {
        super(context, mode, animationStyle);
    }

    /**
     * 重写4个方法
     * 1.滑动方向
     * @return
     */
    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    /**
     * 重写4个方法
     * 2 滑动的view
     * @param context Context to create view with
     * @param attrs AttributeSet from wrapped class. Means that anything you
     *            include in the XML layout declaration will be routed to the
     *            created View
     * @return
     */
    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView view = new RecyclerView(context,attrs);
        return view;
    }

    /**
     * 重写4个方法
     * 3 是否滑动到底部
     * @return
     */
    @Override
    protected boolean isReadyForPullEnd() {
        View view = getRefreshableView().getChildAt(getRefreshableView().getChildCount() -1);
        if(null != view){
            return getRefreshableView().getBottom() >= view.getBottom();
        }
        return false;
    }

    /**
     * 重写4个方法
     * 4 是否滑动到顶部
     * @return
     */
    @Override
    protected boolean isReadyForPullStart() {
        View view = getRefreshableView().getChildAt(0);
        if(view != null){
            return view.getTop() >= getRefreshableView().getTop();
        }
        return false;
    }

}
