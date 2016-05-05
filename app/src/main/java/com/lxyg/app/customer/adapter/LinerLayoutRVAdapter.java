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

import dev.mirror.library.utils.PhoneFliter;

/**
 * Created by 王沛栋 on 2015/11/24.
 */
public abstract class LinerLayoutRVAdapter extends UltimateViewAdapter {
    final private List<Forum.Comment> list;
    private Context mContext;

    public LinerLayoutRVAdapter(List<Forum.Comment> list, Context context) {
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
            final Forum.Comment item = list.get(hasHeaderView() ? position - 1 : position);


            HolderView h = (HolderView)holder;

//            String phoneUser = mContext.getResources().getString(R.string.unlogin_user);

            if(TextUtils.isEmpty(item.getTu_name())){
                if(item.getU_uid().equals(AppContext.USER_ID)){
                    h.name.setText("我:");
                }else{
                    if(item.getName().length() == 11 && item.getName().startsWith("1")){
                        h.name.setText(PhoneFliter.hideMiddleFourNum(item.getName()));
//                        h.name.setText(phoneUser+":");
                    }else
                        h.name.setText(item.getName()+":");
                }
            }else{
                String name1;
                String name2;
                if(item.getU_uid().equals(AppContext.USER_ID)){
                    name1 = "我";
                }else{
                    name1 = item.getName();
                }
                if(item.getTo_u_uid().equals(AppContext.USER_ID)){
                    name2 = "我";
                }else{
                    name2 = item.getTu_name();
                }

                System.out.println("jjjjj-------"+name1);
                if(!TextUtils.isEmpty(name1)){
                    if(name1.length() == 11 && name1.startsWith("1")){
                        name1 = PhoneFliter.hideMiddleFourNum(name1);
//                    name1 = phoneUser;
                    }
                    if(name2.length() == 11 && name2.startsWith("1")){
                        name2 = PhoneFliter.hideMiddleFourNum(name2);
                    }
                }


                h.name.setText(name1+" 回复 "+name2);

                if(!TextUtils.isEmpty(name1)) {
                    if (name1.equals(name2)) {
                        h.name.setText(name1 + ":");
                    }
                }
            }

            h.content.setText(item.getContent());

            if (getOnItemClickListener() != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getOnItemClickListener().onItemClickListener(v,position);
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
