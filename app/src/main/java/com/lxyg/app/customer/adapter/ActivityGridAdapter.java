package com.lxyg.app.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.ActivityActivity;
import com.lxyg.app.customer.activity.ActivityImgActivity;
import com.lxyg.app.customer.activity.ActivityWebViewActivity;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Activitys;
import com.lxyg.app.customer.bean.Constants;

import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;
import dev.mirror.library.utils.DpUtil;

public class ActivityGridAdapter extends DevListBaseAdapter<Activitys> {

    private final LayoutInflater mInflater;
    private final List<Activitys> mList;
    private final Context mContext;

    private LinearLayout.LayoutParams mParams;
    public ActivityGridAdapter(Context context, List<Activitys> list,int screenWidth) {
        super(context, list);
        mInflater = LayoutInflater.from(context);
        mList = list;
        mContext = context;
        int width = (screenWidth/2- DpUtil.dip2px(context,2));
        int heght = width/5*3;
        mParams = new LinearLayout.LayoutParams(width,
                heght);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View initView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_home_activity, null);
        }
        ImageView img = (ImageView) ViewHolder.get(convertView, R.id.img);
        img.setLayoutParams(mParams);

        final Activitys g = mList.get(position);

        AppContext.displayImage(img, g.getImg_url());
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0 ) {
                    mContext.startActivity(new Intent(mContext, ActivityWebViewActivity.class).putExtra("uid", position).putExtra(Constants.INTENT_ID,
                            g.getActivityId()));

                } else if (position == 3) {
                    mContext.startActivity(new Intent(mContext, ActivityImgActivity.class));
                } else {
                    mContext.startActivity(new Intent(mContext, ActivityActivity.class).putExtra(Constants.INTENT_ID,
                            g.getActivityId()));
                }
            }
        });

        return convertView;
    }
}