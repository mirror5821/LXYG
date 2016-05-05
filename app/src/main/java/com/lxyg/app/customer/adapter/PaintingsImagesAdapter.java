package com.lxyg.app.customer.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.alexvasilkov.gestures.views.interfaces.GestureView;
import com.alexvasilkov.gestures.views.utils.RecyclePagerAdapter;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Forum;
import com.lxyg.app.customer.iface.ImageGestureListener;

import java.util.List;

/**
 * Created by 王沛栋 on 2015/11/12.
 */
public class PaintingsImagesAdapter extends RecyclePagerAdapter<PaintingsImagesAdapter.ViewHolder> {
    private final ViewPager mViewPager;
    private final List<Forum.Imgs> mDatas;
    private final OnSetupGestureViewListener mSetupListener;
    private final Context mContext;

    public PaintingsImagesAdapter(ViewPager pager,List<Forum.Imgs> datas,OnSetupGestureViewListener listener,Context context){
        this.mViewPager = pager;
        this.mDatas = datas;
        this.mSetupListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup container) {
        ViewHolder holder = new ViewHolder(container);
        holder.image.getController().enableScrollInViewPager(mViewPager);
        holder.image.getController().getSettings().setFillViewport(true).setMaxZoom(3f);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mSetupListener != null)
            mSetupListener.onSetupGestureView(holder.image);
        holder.image.getController().resetState();


        final GestureDetector gestureDetector = new GestureDetector(mContext, new ImageGestureListener(mContext));

        holder.image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        AppContext.displayImage(holder.image,mDatas.get(position).getImg_url());
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    static class ViewHolder extends RecyclePagerAdapter.ViewHolder {
        public final GestureImageView image;

        public ViewHolder(ViewGroup container) {
            super(new GestureImageView(container.getContext()));
            image = (GestureImageView) itemView;
        }
    }

    public interface OnSetupGestureViewListener {
        void onSetupGestureView(GestureView view);
    }
}
