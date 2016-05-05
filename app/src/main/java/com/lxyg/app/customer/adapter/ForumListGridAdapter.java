package com.lxyg.app.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.GestureViewsActivity;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.bean.Forum;

import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;
import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.view.SquareImageView;

/**
 * Created by 王沛栋 on 2015/11/11.
 */
public class ForumListGridAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private final List<Forum.Imgs> mData;
    private Context mContext;

    public ForumListGridAdapter(Context context,List<Forum.Imgs> data){
        mInflater = LayoutInflater.from(context);
        this.mData = data;
        mContext = context;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.view_square_image, null);
        }
        SquareImageView iv = (SquareImageView) DevListBaseAdapter.ViewHolder.get(convertView, R.id.img_s);
        AppContext.displayImage(iv,mData.get(position).getImg_url());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, GestureViewsActivity.class)
                        .putExtra(Constants.INTENT_ID, JsonUtils.listToString(mData,Forum.Imgs.class)));
            }
        });



        return convertView;
    }
}
