package com.lxyg.app.customer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lxyg.app.customer.R;

/**
 * Created by 王沛栋 on 2015/10/30.
 */
public class SearchRecommedAdapter extends RecyclerView.Adapter<SearchRecommedAdapter.TextViewHolder>{

    private final LayoutInflater mInflater;
    private String [] mDatas;
    public SearchRecommedAdapter(Context context,String [] datas){
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    private static OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,String data);
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TextViewHolder(mInflater.inflate(R.layout.item_search_recommed_tv,parent,false));
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
        holder.mTv.setText(mDatas[position]);
        holder.itemView.setTag(mDatas[position]);
    }

    @Override
    public int getItemCount() {
        return mDatas.length;
    }


    public static class TextViewHolder extends RecyclerView.ViewHolder{
        TextView mTv;
        public TextViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView)itemView;
            mTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnRecyclerViewItemClickListener != null){
                        mOnRecyclerViewItemClickListener.onItemClick(v,(String)v.getTag());
                    }
                }
            });
        }
    }

    /**
     * 单项点击事件
     * @param listener
     */
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOnRecyclerViewItemClickListener = listener;
    }

}
