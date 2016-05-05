package com.lxyg.app.customer.recycleview;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.view.AutoAdjustHeightImageView;

import net.tsz.afinal.FinalBitmap;

/**
 * Created by 王沛栋 on 2015/11/20.
 */
public class DevRecycleViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
    private DevRecycleViewAdapter mAdapter;
    public DevRecycleViewHolder(View itemView) {
        super(itemView);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT);
        itemView.setLayoutParams(lp);
        this.mViews = new SparseArray<View>();
        this.mConvertView = itemView;

    }

    public <T> DevRecycleViewHolder(View itemView,final DevRecycleViewAdapter<T> adapter){
        super(itemView);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT);
        itemView.setLayoutParams(lp);
        this.mViews = new SparseArray<View>();
        this.mConvertView = itemView;
        this.mAdapter = adapter;
    }


    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的id获取相对的控件，如果没有则加入views
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if(view == null){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        try {
            return (T)view;
        }catch (ClassCastException e){
            return null;
        }
    }

    /**
     * 通过控件的id获取相对的控件，如果没有则加入views
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T findViewById(int viewId){
        View view = mViews.get(viewId);
        if(view == null){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        try {
            return (T) view;
        }catch (ClassCastException e){
            return null;
        }
    }

    /**
     * 为view设置文字
     * @param viewId
     * @param text
     * @return
     */
    public DevRecycleViewHolder setText(int viewId,String text){
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 设置本地资源图片
     * @param viewId
     * @param drawableId
     * @return
     */
    public DevRecycleViewHolder setImgResource(int viewId,int drawableId){
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 设置图片by Bitmap
     * @param viewId
     * @param bm
     * @return
     */
    public DevRecycleViewHolder setImgBitmap(int viewId,Bitmap bm){
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 设置网络图片
     * @param viewId
     * @param url
     * @return
     */
    public DevRecycleViewHolder setDisplayImg(int viewId,String url){
        ImageView view = getView(viewId);
        AppContext.displayImage(view, url);
        return this;
    }



}
