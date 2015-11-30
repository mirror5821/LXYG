package dev.mirror.library.view.recycleview;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
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

//    private int stateToSave;
//    //试图暂存写的如下方法
//    @Override
//    public Parcelable onSaveInstanceState() {
//        //begin boilerplate code that allows parent classes to save state
//        Parcelable superState = super.onSaveInstanceState();
//
//        SavedState ss = new SavedState(superState);
//        //end
//
//        ss.stateToSave = this.stateToSave;
//
//        return ss;
//    }
//
//    @Override
//    public void onRestoreInstanceState(Parcelable state) {
//        //begin boilerplate code so parent classes can restore state
//        if(!(state instanceof SavedState)) {
//            super.onRestoreInstanceState(state);
//            return;
//        }
//
//        SavedState ss = (SavedState)state;
//        super.onRestoreInstanceState(ss.getSuperState());
//        //end
//
//        this.stateToSave = ss.stateToSave;
//    }
//
//    static class SavedState extends BaseSavedState {
//        int stateToSave;
//
//        SavedState(Parcelable superState) {
//            super(superState);
//        }
//
//        private SavedState(Parcel in) {
//            super(in);
//            this.stateToSave = in.readInt();
//        }
//
//        @Override
//        public void writeToParcel(Parcel out, int flags) {
//            super.writeToParcel(out, flags);
//            out.writeInt(this.stateToSave);
//        }
//
//        //required field that makes Parcelables from a Parcel
//        public static final Parcelable.Creator<SavedState> CREATOR =
//                new Parcelable.Creator<SavedState>() {
//                    public SavedState createFromParcel(Parcel in) {
//                        return new SavedState(in);
//                    }
//                    public SavedState[] newArray(int size) {
//                        return new SavedState[size];
//                    }
//                };
//    }


}
