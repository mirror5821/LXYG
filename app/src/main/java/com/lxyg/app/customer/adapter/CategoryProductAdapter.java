package com.lxyg.app.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Car;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.bean.Product;

import net.tsz.afinal.FinalDb;

import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;
import dev.mirror.library.utils.PriceUtil;

public class CategoryProductAdapter extends DevListBaseAdapter<Product> {

	private final LayoutInflater mInflater;
	private final List<Product> mList;
	private final Context mContext;
	private final FinalDb db;

	public CategoryProductAdapter(Context context, List<Product> list) {
		super(context, list);
		mInflater = LayoutInflater.from(context);
		mList = list;
		mContext = context;
		db = AppContext.mDb;
	}

	@Override
	public int getCount() {
		int i = mList.size();
		if(i<4){
			return i;
		}else{
			return (i -i%3);
		}

	}

	@Override
	public View initView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_product_new, null);
		}
		TextView name = (TextView) ViewHolder.get(convertView, R.id.name);
		final TextView num = (TextView) ViewHolder.get(convertView, R.id.n);
		TextView price = (TextView) ViewHolder.get(convertView, R.id.price);
		ImageView img = (ImageView) ViewHolder.get(convertView, R.id.img);
		final TextView ePay = (TextView) ViewHolder.get(convertView, R.id.e);
		final LinearLayout mView = (LinearLayout) ViewHolder.get(convertView, R.id.view_e);
		ImageView imgCar = (ImageView) ViewHolder.get(convertView, R.id.img_car);

		final Product g = mList.get(position);

		int eCash = g.getCash_pay();
		if(eCash == 0){
			mView.setBackgroundResource(R.color.cash_0);
		}else{
			mView.setBackgroundResource(R.color.red);
		}
		ePay.setText(PriceUtil.floatToString(eCash));

		g.getTitle();
//		String title = g.getTitle();
		name.setText(g.getName());
		price.setText("￥" + PriceUtil.floatToString(g.getPrice()));


//		x.image().bind(img, g.getCover_img(), AppContext.mImageOptions);
		AppContext.displayImage(img,g.getCover_img());
		String mPid = g.getProductId();

		//先判断数据库是否有数据
		List<Car> c = db.findAllByWhere(Car.class, "productId="+"'"+mPid+"'");
		//如果存在数据
		if(c.size()>0){
			//将已经存在的数据叠加
			num.setVisibility(View.VISIBLE);
			num.setText(c.get(0).getNum()+"");
		}else{
			num.setVisibility(View.GONE);
		}

		imgCar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addCar(g,num);
			}
		});

		return convertView;
	}

	private void addCar(Product p,TextView tv){
		String mPid = p.getProductId();

		int count = 1;
		FinalDb db = AppContext.mDb;
		//先判断数据库是否有数据
		List<Car> c = db.findAllByWhere(Car.class, "productId="+"'"+mPid+"'");
		//如果存在数据
		if(c.size()>0){
			//将已经存在的数据叠加
			count = count+c.get(0).getNum();
			//删除已经存在的数据
			db.deleteByWhere(Car.class, "productId="+"'"+mPid+"'");
		}
		Car car = new Car();
		car.setProductId(mPid);
		car.setPrice(p.getPrice() + "");
		car.setNum(count);
		car.setIs_norm(Constants.PRODUCT_TYPE1);
		tv.setVisibility(View.VISIBLE);
		tv.setText(count+"");
		//保存数据
		AppContext.mDb.save(car);
	}
}