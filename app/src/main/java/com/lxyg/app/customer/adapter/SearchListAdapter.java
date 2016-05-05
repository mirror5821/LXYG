package com.lxyg.app.customer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Product;

import java.util.List;

import dev.mirror.library.utils.PriceUtil;

/**
 * Created by 王沛栋 on 2015/10/30.
 */
public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.TextViewHolder>{

    private final LayoutInflater mInflater;
    private List<Product> mDatas;
    public SearchListAdapter(Context context, List<Product> datas){
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    private static OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view, String data);
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TextViewHolder(mInflater.inflate(R.layout.item_fb_product,parent,false));
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
        Product p = mDatas.get(position);
        holder.mTv.setText(p.getName());
        holder.itemView.setTag(p.getProductId());
        holder.mTvPrice.setText("￥ "+PriceUtil.floatToString(p.getPrice()));
        holder.mTvCase.setText("电子现金:￥"+PriceUtil.floatToString(p.getCash_pay()));
        holder.mTvTitle.setText(p.getTitle());

        AppContext.displayImage(holder.mImg,p.getCover_img());

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public static class TextViewHolder extends RecyclerView.ViewHolder{
        TextView mTv,mTvTitle,mTvPrice,mTvCase;
        ImageView mImg;
        public TextViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView)itemView.findViewById(R.id.name);
            mTvTitle = (TextView)itemView.findViewById(R.id.title);
            mTvPrice = (TextView)itemView.findViewById(R.id.price);
            mTvCase = (TextView)itemView.findViewById(R.id.cash);
            mImg = (ImageView)itemView.findViewById(R.id.img);

            itemView.setOnClickListener(new View.OnClickListener() {
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
