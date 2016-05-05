package com.lxyg.app.customer.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Car;
import com.lxyg.app.customer.bean.Order;
import com.lxyg.app.customer.bean.Product;
import com.lxyg.app.customer.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.utils.PriceUtil;

public class OrderProductListActivity extends BaseActivity{
	private List<Car> mCarList;
	private LayoutInflater mInflater;
	private List<Product> mList;
	private LinearLayout mViewProduct;

	private Bundle mBundle;

	private int mNum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_product_list);
		setBack();
		setTitleText("商品详情");

		mBundle = getIntent().getExtras();

		mViewProduct = (LinearLayout)findViewById(R.id.view2);

		mInflater = getLayoutInflater();

		mList = new ArrayList<Product>();

		loadShoppingCar();
	}



	private int mPayFrom;
	/**
	 * 加载购物车数据
	 */
	private void loadShoppingCar(){
		mPayFrom = mBundle.getInt(TYPE_ID);
		switch (mPayFrom){
			case PAY_TYPE1:
				loadShopCar();
				break;
			case PAY_TYPE2:
				loadOrder();
				break;
			case PAY_TYPE3:
				loadData();
				break;
		}


	}


	private void loadData(){
		mNum = mBundle.getInt("NUMBER");
		mList = JsonUtils.parseList(mBundle.getString(INTENT_ID), Product.class);

		for(int i=0; i<mList.size();i++) {


			View view = mInflater.inflate(R.layout.item_order_confirm, null);

			TextView name = (TextView)view.findViewById(R.id.name);
			TextView price = (TextView)view.findViewById(R.id.price);
			ImageView img = (ImageView)view.findViewById( R.id.img);
			TextView num = (TextView)view.findViewById(R.id.num);
			TextView totlePrice = (TextView)view.findViewById(R.id.price_t);

			Product g = mList.get(i);
			int n = mNum;

			int p = g.getPrice();
			totlePrice.setText(PriceUtil.floatToString((n * p))+"元");
			name.setText(g.getName());
			price.setText("￥"+ PriceUtil.floatToString(p));
			AppContext.displayImage(img,g.getCover_img());
			num.setText("x "+n);
			mViewProduct.addView(view);

		}
	}


	private Order mOrder;
	private List<Order.orderItems> mDataP = new ArrayList<>();
	private void loadOrder(){
		mOrder = JsonUtils.parse(mBundle.getString(INTENT_ID).toString(), Order.class);
		mDataP.clear();
		mDataP = mOrder.getOrderItems();
		for(int i=0;i<mDataP.size();i++){
			Order.orderItems o = mDataP.get(i);

			View view = mInflater.inflate(R.layout.item_order_confirm, null);

			TextView name = (TextView)view.findViewById(R.id.name);
			TextView price = (TextView)view.findViewById(R.id.price);
			ImageView img = (ImageView)view.findViewById( R.id.img);
			TextView num = (TextView)view.findViewById(R.id.num);
			TextView totlePrice = (TextView)view.findViewById(R.id.price_t);



			int n = Integer.valueOf(o.getProduct_number());
			int p = o.getProduct_price();// g.getPrice();
			totlePrice.setText(PriceUtil.floatToString((n * p))+"元");
			name.setText(o.getName());
			price.setText("￥" + PriceUtil.floatToString(p));
			AppContext.displayImage(img, o.getCover_img());
			num.setText("x " + n);
			mViewProduct.addView(view);


		}
	}

	private void loadShopCar(){
		mCarList = AppContext.mDb.findAll(Car.class);//查询所有的用户
		//如果购物车为空
		if(mCarList.size()==0){
			showToast("您的购物车暂无商品");
			finish();

		}else{
			JSONObject jb = new JSONObject();
			try {
				JSONArray ja = new JSONArray();
				for (Car car : mCarList) {
					JSONObject j = new JSONObject();
					j.put("productId",car.getProductId());
					j.put("is_norm",car.getIs_norm());
					ja.put(j);
				}
				jb.put("pids", ja.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}

			mBaseHttpClient.postData1(SHOPPING_CAR_LIST2, jb,new onResultListener() {

				@Override
				public void onResult(String data, String msg) {
					mList.addAll(JsonUtils.parseList(data, Product.class));

					for(int i=0; i<mList.size();i++) {


						View view = mInflater.inflate(R.layout.item_order_confirm, null);

						TextView name = (TextView)view.findViewById(R.id.name);
						TextView price = (TextView)view.findViewById(R.id.price);
						ImageView img = (ImageView)view.findViewById( R.id.img);
						TextView num = (TextView)view.findViewById(R.id.num);
						TextView totlePrice = (TextView)view.findViewById(R.id.price_t);

						Product g = mList.get(i);
						int n = mCarList.get(i).getNum();
						int p = g.getPrice();
						totlePrice.setText(PriceUtil.floatToString((n * p))+"元");
						name.setText(g.getName());
						price.setText("￥"+ PriceUtil.floatToString(p));
						AppContext.displayImage(img,g.getCover_img());
						num.setText("x "+n);
						mViewProduct.addView(view);

					}

				}

				@Override
				public void onError(String error) {
					showToast(error);
				}
			});
		}
	}

}
