package com.lxyg.app.customer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.bean.Categorys;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

/**
 * Created by 王沛栋 on 2015/12/29.
 */
public abstract class CategoryAdapter extends UltimateViewAdapter {
    final private List<Categorys.cate> list;
    private Context mContext;

    public CategoryAdapter(List<Categorys.cate> list, Context context) {
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
            final Categorys.cate item = list.get(hasHeaderView() ? position - 1 : position);


            final HolderView h = (HolderView)holder;

            h.name.setText(item.getName());

            if (getOnItemClickListener() != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getOnItemClickListener().onItemClickListener(v,position);
//                        h.onItemSelected();
                    }
                });
            }

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
        TextView name;
        public HolderView(View itemView, boolean isItem) {
            super(itemView);
            if(isItem){
                name = (TextView) itemView.findViewById(R.id.name);
            }

        }

        @Override
        public void onItemSelected() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                itemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_rb_category));
            }

//            itemView.setBackgroundColor(Color.LTGRAY);
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
