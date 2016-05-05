package com.lxyg.app.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Product;

import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;
import dev.mirror.library.utils.PriceUtil;

public class ShoppingCarAdapter extends DevListBaseAdapter<Product> {

	private final LayoutInflater mInflater;
	private final List<Product> mList;
	public ShoppingCarAdapter(Context context, List<Product> list) {
		super(context, list);
		mInflater = LayoutInflater.from(context);
		mList = list;
	}

	@Override
	public View initView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_product, null);
		}
		TextView name = (TextView) ViewHolder.get(convertView, R.id.name);
		TextView price = (TextView) ViewHolder.get(convertView, R.id.price);
		ImageView img = (ImageView) ViewHolder.get(convertView, R.id.img);

		Product g = mList.get(position);
		name.setText(g.getName());
		price.setText("￥"+ PriceUtil.floatToString(g.getPrice()));
		AppContext.displayImage(img,g.getCover_img());

		return convertView;
	}
}