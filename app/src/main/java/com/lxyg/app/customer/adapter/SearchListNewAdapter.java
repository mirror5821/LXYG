package com.lxyg.app.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.ProductDetailsActivity;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.bean.Product;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

import dev.mirror.library.utils.PriceUtil;

/**
 * Created by 王沛栋 on 2015/11/24.
 */
public class SearchListNewAdapter extends UltimateViewAdapter {
    final private List<Product> list;
    private Context mContext;

    public SearchListNewAdapter(List<Product> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    @Override
    public UltimateRecyclerviewViewHolder getViewHolder(View view) {
        UltimateRecyclerviewViewHolder g = new UltimateRecyclerviewViewHolder(view);
        return g;
    }


    @Override
    public HolderView onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fb_product, parent, false);
        return new HolderView(view, true);
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
            final Product p = list.get(hasHeaderView() ? position - 1 : position);


            HolderView h = (HolderView) holder;


            h.mTv.setText(p.getName());
            h.itemView.setTag(p.getProductId());
            h.mTvPrice.setText("￥ " + PriceUtil.floatToString(p.getPrice()));
            h.mTvCase.setText("电子现金:￥"+PriceUtil.floatToString(p.getCash_pay()));
            h.mTvTitle.setText(p.getTitle());

            AppContext.displayImage(h.mImg, p.getCover_img());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, ProductDetailsActivity.class).putExtra(Constants.INTENT_ID,
                            p.getProductId()));
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



    public class HolderView extends UltimateRecyclerviewViewHolder {

        TextView mTv,mTvTitle,mTvPrice,mTvCase;
        ImageView mImg;
        public HolderView(View itemView, boolean isItem) {
            super(itemView);
            if(isItem){
                mTv = (TextView)itemView.findViewById(R.id.name);
                mTvTitle = (TextView)itemView.findViewById(R.id.title);
                mTvPrice = (TextView)itemView.findViewById(R.id.price);
                mTvCase = (TextView)itemView.findViewById(R.id.cash);
                mImg = (ImageView)itemView.findViewById(R.id.img);
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
