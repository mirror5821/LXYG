package com.lxyg.app.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.ForumDetailsActivity;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.bean.Forum;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

/**
 * Created by 王沛栋 on 2015/11/24.
 */
public class StaggeredLayoutRVAdapter extends UltimateViewAdapter {
    final private List<Forum> list;
    private Context mContext;

    public StaggeredLayoutRVAdapter(List<Forum> list,Context context) {
        this.list = list;
        this.mContext = context;
    }

    @Override
    public UltimateRecyclerviewViewHolder getViewHolder(View view) {
        UltimateRecyclerviewViewHolder g = new UltimateRecyclerviewViewHolder(view);
        return g;
    }


    @Override
    public HolderGirdCell onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum_new, parent, false);
        return new HolderGirdCell(view, true);
    }

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (VIEW_TYPES.HEADER == getItemViewType(position)) {
            onBindHeaderViewHolder(holder, position);
        } else if (VIEW_TYPES.NORMAL == getItemViewType(position)) {
            final Forum item = list.get(hasHeaderView() ? position - 1 : position);

            List<Forum.Imgs> imgs =  item.getFormImgs();

            HolderGirdCell h = (HolderGirdCell) holder;
            if (imgs != null){
                h.imageView.setVisibility(View.VISIBLE);
                AppContext.displayImage(h.imageView, imgs.get(0).getImg_url());
            }else{
                h.imageView.setVisibility(View.GONE);
            }


            h.name.setText(item.getTitle());
            h.btnZan.setText(item.getZanNum()+"");
            h.btnComment.setText(item.getReplayNum() + "");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, ForumDetailsActivity.class).
                            putExtra(Constants.INTENT_ID, item.getForm_id()).putExtra("from", 0));
                }
            });
        }else if(VIEW_TYPES.FOOTER == getItemViewType(position)){//结尾同理头部
            onBindHeaderViewHolder(holder,position);
        }
    }

    private StaggeredGridLayoutManager.LayoutParams mLayoutParams;
    private StaggeredGridLayoutManager.LayoutParams getLayoutParams(){
        if(mLayoutParams == null){
            mLayoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        return mLayoutParams;
    }
    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        //设置占满全格
        holder.itemView.setLayoutParams(getLayoutParams());
        mLayoutParams.setFullSpan(true);

    }



    public class HolderGirdCell extends UltimateRecyclerviewViewHolder {

        TextView name,btnZan,btnComment;
        ImageView imageView;

        public HolderGirdCell(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {


                name = (TextView) itemView.findViewById(R.id.name);
                btnZan = (TextView) itemView.findViewById(R.id.btn_zan);
                btnComment = (TextView)itemView.findViewById(R.id.btn_comment);

                imageView = (ImageView) itemView.findViewById(R.id.pic);
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
