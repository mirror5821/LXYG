package com.lxyg.app.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.ProductDetailsActivity;
import com.lxyg.app.customer.activity.ProductListActivity;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.bean.HomeProduct;
import com.lxyg.app.customer.bean.Product;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

import dev.mirror.library.view.NoScrollGridView;

/**
 * Created by 王沛栋 on 2015/11/24.
 */
public abstract class IndexAdapter extends UltimateViewAdapter {
    final private List<HomeProduct> list;
    private Context mContext;
    private TextView mTvCarNum;

    public IndexAdapter(List<HomeProduct> list, Context context,TextView tv) {
        this.list = list;
        this.mContext = context;
        mTvCarNum = tv;
    }

    @Override
    public UltimateRecyclerviewViewHolder getViewHolder(View view) {
        UltimateRecyclerviewViewHolder g = new UltimateRecyclerviewViewHolder(view);
        return g;
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
        if (VIEW_TYPES.HEADER == getItemViewType(position)) {
            onBindHeaderViewHolder(holder, position);
        } else if (VIEW_TYPES.NORMAL == getItemViewType(position)) {
            final HomeProduct item = list.get(hasHeaderView() ? position - 1 : position);
            HolderView h = (HolderView)holder;
            final HomeProduct.Types type = item.getType();

            final String pName = type.getName();
            final int pId = type.getTypeId();

            h.tv.setText(pName);
            switch (pId) {
                case 1:
                    h.viewI.setBackgroundResource(R.drawable.g_d1);
                    break;
                case 2:
                    h.viewI.setBackgroundResource(R.drawable.g_d2);
                    break;
                case 3:
                    h.viewI.setBackgroundResource(R.drawable.g_d3);
                    break;
                case 4:
                    h.viewI.setBackgroundResource(R.drawable.g_d4);
                    break;
                case 5:
                    h.viewI.setBackgroundResource(R.drawable.g_d5);
                    break;
                case 6:
                    h.viewI.setBackgroundResource(R.drawable.g_d6);
                    break;
                case 7:
                    h.viewI.setBackgroundResource(R.drawable.g_d7);
                    break;
                case 8:
                    h.viewI.setBackgroundResource(R.drawable.g_d8);
                    break;
                default:
                    h.viewI.setBackgroundResource(R.drawable.g_d1);
                    break;
            }

            final List<Product> pList = item.getProducts();
            h.gView.setAdapter(new ProductGridAdapter(mContext, pList,mTvCarNum));

            h.gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    mContext.startActivity(new Intent(mContext, ProductDetailsActivity.class).putExtra(
                            Constants.INTENT_ID, pList.get(position).getProductId()));
                }
            });

            h.more.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    mContext.startActivity(new Intent(mContext, CategoryNewActivity.class).putExtra(Constants.INTENT_ID,
//                            pId));

                    mContext.startActivity(new Intent(mContext, ProductListActivity.class).
                            putExtra(Constants.INTENT_ID, pId).putExtra("typeId", pId));
                }
            });


        }else if(VIEW_TYPES.FOOTER == getItemViewType(position)){//结尾同理头部
            onBindHeaderViewHolder(holder,position);
        }
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        setHeaderView(holder,position);
    }

    public abstract void setHeaderView(RecyclerView.ViewHolder holder, int position);



    public class HolderView extends UltimateRecyclerviewViewHolder {
        LinearLayout more;
        View viewI;
        TextView tv;
        NoScrollGridView gView;// = (NoScrollGridView)view.findViewById(R.id.p_gridview);

        public HolderView(View itemView, boolean isItem) {
            super(itemView);
            if(isItem){
                tv = (TextView)  itemView.findViewById(R.id.p_name);
                more = (LinearLayout)  itemView.findViewById(R.id.view_more);
                viewI = itemView.findViewById(R.id.view_item);
                gView = (NoScrollGridView) itemView.findViewById(R.id.p_gridview);
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

    public interface OnItemClickListener {
        void onItemClickListener(View view, int position);
    }

    public OnItemClickListener l;

    public OnItemClickListener getOnItemClickListener() {
        return l;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.l = l;
    }
}
