package com.lxyg.app.customer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.lxyg.app.customer.R;

import java.util.List;

/**
 * Created by haha on 2015/9/12.
 */
public class ImageAddAdapter extends BaseAdapter {
    private List<Bitmap> mList;
    private LayoutInflater mInflater;
    private Context mContext;
    public ImageAddAdapter(Context context,List<Bitmap> mList){
        this.mList = mList;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_img_add, null);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img_add);
            viewHolder.imgDelete = (ImageView)convertView.findViewById(R.id.img_delete);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Bitmap imgUrl = mList.get(position);
        if (imgUrl!=null){
            viewHolder.img.setImageBitmap(imgUrl);
            viewHolder.imgDelete.setVisibility(View.VISIBLE);
        }else{
            viewHolder.imgDelete.setVisibility(View.GONE);
            viewHolder.img.setImageResource(R.mipmap.ic_img_add);
        }

        return convertView;
    }

    private static class ViewHolder{
        ImageView img,imgDelete;
    }
}
