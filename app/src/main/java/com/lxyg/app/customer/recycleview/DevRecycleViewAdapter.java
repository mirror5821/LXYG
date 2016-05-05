package com.lxyg.app.customer.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 王沛栋 on 2015/11/20.
 */
public abstract class DevRecycleViewAdapter<T> extends RecyclerView.Adapter<DevRecycleViewHolder> {
    private Context mContext;
    private List<T> mDatas;
    private final int mItemLayoutId;
    private LayoutInflater mInflater;

    public DevRecycleViewAdapter(Context context,List<T> datas,int itemLayoutId){
        this.mDatas = datas;
        this.mItemLayoutId = itemLayoutId;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(DevRecycleViewHolder holder, int position) {
        convert(holder,mDatas.get(position));
    }

    /**
     * 设置每个页面要显示的内容
     * @param holder
     * @param item
     */
    public abstract void convert(DevRecycleViewHolder holder, T item);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public DevRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(mItemLayoutId,null);
        return new DevRecycleViewHolder(v,this);
    }

    protected OnItemClickListener onItemClickListener;



    public OnItemClickListener getItemClickListener() {
        return onItemClickListener;
    }

    /**
     * 设置点击事件监听器
     *
     * @param onItemClickListener 监听器对象
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClickListener(int position);
    }
}
