package dev.mirror.library.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by 王沛栋 on 2015/10/27.
 */
public class PullToRefreshRelativeLayoutView extends PullToRefreshBase<RelativeLayout> {
    public PullToRefreshRelativeLayoutView(Context context){
        super(context);
    }

    public PullToRefreshRelativeLayoutView(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    public PullToRefreshRelativeLayoutView(Context context,Mode mode){
        super(context,mode);
    }

    public PullToRefreshRelativeLayoutView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }

    //重写4个方法
    //1 滑动方向
    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    //重写4个方法
    //2  滑动的View
    @Override
    protected RelativeLayout createRefreshableView(Context context, AttributeSet attrs) {
        RelativeLayout view = new RelativeLayout(context, attrs);
        return view;
    }

    //重写4个方法
    //3 是否滑动到底部
    @Override
    protected boolean isReadyForPullEnd() {
        View view = getRefreshableView().getChildAt(getRefreshableView().getChildCount() - 1);
        if (null != view) {
            return getRefreshableView().getBottom() >= view.getBottom();
        }
        return false;
    }

    //重写4个方法
    //4 是否滑动到顶部
    @Override
    protected boolean isReadyForPullStart() {
        View view = getRefreshableView().getChildAt(0);

        if (view != null) {
            return view.getTop() >= getRefreshableView().getTop();
        }
        return false;
    }

}
