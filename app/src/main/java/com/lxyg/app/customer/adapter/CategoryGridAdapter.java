package com.lxyg.app.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.CategoryNew2Activity;
import com.lxyg.app.customer.activity.FBShopListActivity;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Category;
import com.lxyg.app.customer.bean.Constants;

import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;

public class CategoryGridAdapter extends DevListBaseAdapter<Category> {

	private final LayoutInflater mInflater;
	private final List<Category> mList;
	private final Context mContext;

	public CategoryGridAdapter(Context context, List<Category> list) {
		super(context, list);
		mInflater = LayoutInflater.from(context);
		mList = list;
		mContext = context;
	}

	@Override
	public int getCount() {
		int i = mList.size();
		if(i>8){
			return 8;
		}else{
			return i;
		}
	}

	@Override
	public View initView(final int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_category, null);
		}
		TextView name = (TextView) ViewHolder.get(convertView, R.id.tv);
		ImageView img = (ImageView) ViewHolder.get(convertView, R.id.img);

		final Category g = mList.get(position);

//		convertView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				if (g.getName().equals("订单跟踪")) {
//					if (!AppContext.IS_LOGIN) {
//						Toast.makeText(mContext,"请先登录",Toast.LENGTH_SHORT).show();
//					} else {
//						mContext.startActivity(new Intent(mContext, MapTrackActivity.class));
//					}
//				} else {
//					if (g.getIs_norm() != 1) {
//						mContext.startActivity(new Intent(mContext, FBShopListActivity.class).putExtra(
//								Constants.INTENT_ID, g.getTypeId()));
//
//					} else {
//						mContext.startActivity(new Intent(mContext, ProductListActivity.class).putExtra(
//								Constants.INTENT_ID, Integer.valueOf(g.getTypeId())).putExtra("typeId",Integer.valueOf(g.getTypeId())));
//					}
//				}
//			}
//		});

		//第二版数据
//		if(position == 7){
//			name.setText("更多分类");
//			img.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.ic_c_more));
////			img.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_c_more, null));
//			convertView.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
////					mContext.startActivity(new Intent(mContext, CategoryActivity.class));
//					mContext.startActivity(new Intent(mContext, CategoryNew2Activity.class).putExtra(Constants.INTENT_ID,0));
//				}
//			});
//
//
////			img.setImageResource(mContext.getResources().getDrawable(R.mipmap.ic_c_more,null));
////			AppContext.displayImage(img, g.getImg());
//		}else{
//			name.setText(g.getName());
//			AppContext.displayImage(img, g.getImg());
//			convertView.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
////					mContext.startActivity(new Intent(mContext, ProductListActivity.class).putExtra(
////							Constants.INTENT_ID, Integer.valueOf(g.getTypeId())).putExtra("typeId",Integer.valueOf(g.getTypeId())));
//					mContext.startActivity(new Intent(mContext, CategoryNewActivity.class).putExtra(Constants.INTENT_ID,
//							Integer.valueOf(g.getTypeId())));
//				}
//			});
//		}

		name.setText(g.getName());
		AppContext.displayImage(img, g.getImg());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//					mContext.startActivity(new Intent(mContext, CategoryActivity.class));
				mContext.startActivity(new Intent(mContext, CategoryNew2Activity.class).putExtra(Constants.INTENT_ID,position));
//				if(position == 7){
//					mContext.startActivity(new Intent(mContext, FBShopListActivity.class));
//				}else
//					mContext.startActivity(new Intent(mContext, CategoryNew2Activity.class).putExtra(Constants.INTENT_ID,position));

			}
		});

		return convertView;
	}
}