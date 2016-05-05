package com.lxyg.app.customer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Forum;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

/**
 * Created by 王沛栋 on 2015/11/24.
 */
public abstract class IndexRVAdapter extends UltimateViewAdapter {
    final private List<Forum.Comment> list;
    private Context mContext;

    public IndexRVAdapter(List<Forum.Comment> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    @Override
    public UltimateRecyclerviewViewHolder getViewHolder(View view) {
        UltimateRecyclerviewViewHolder g = new UltimateRecyclerviewViewHolder(view);
        return g;
    }

    private  final int VIEW_ITEM1 = 0;
    private  final int VIEW_ITEM2 = 1;
    private  final int VIEW_ITEM3 = 2;
    private  final int VIEW_ITEM4 = 3;

    @Override
    public int getItemViewType(int position) {
        int p = 0;
        switch (position){
            case 0:
                p = VIEW_ITEM1;
                break;
            case 1:
                p = VIEW_ITEM2;
                break;
            case 2:
                p = VIEW_ITEM3;
                break;
            case 3:
                p = VIEW_ITEM4;
                break;
        }
        return p;
    }

    @Override
    public HolderView onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(setItemLayoutId(), parent, false);
        return new HolderView(view, true);
    }

    public abstract int setItemLayoutId();
    @Override
    public UltimateRecyclerviewViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new UltimateRecyclerviewViewHolder(parent);
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (VIEW_ITEM1 == getItemViewType(position)) {
            onBindHeaderViewHolder(holder, position);
        } else if (VIEW_ITEM2 == getItemViewType(position)) {
            setCategoryView(holder, position);
        }else if(VIEW_ITEM3 == getItemViewType(position)){
            setCommentView(holder, position);
        }else{
            setProductView(holder,position);
        }
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        setHeaderView(holder,position);
    }

    public abstract void setHeaderView(RecyclerView.ViewHolder holder, int position);
    public abstract void setCategoryView(RecyclerView.ViewHolder holder, int position);
    public abstract void setCommentView(RecyclerView.ViewHolder holder, int position);
    public abstract void setProductView(RecyclerView.ViewHolder holder, int position);



    public class HolderView extends UltimateRecyclerviewViewHolder {
        TextView content,name;
        public HolderView(View itemView, boolean isItem) {
            super(itemView);
            if(isItem){
                name = (TextView) itemView.findViewById(R.id.name);
                content = (TextView) itemView.findViewById(R.id.content);
            }

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
