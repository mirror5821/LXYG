package com.lxyg.app.customer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Car;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.bean.Product;
import com.lxyg.app.customer.utils.AppAjaxCallback.onRecevieDataListener;
import com.lxyg.app.customer.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.customer.utils.ImmersionModeUtils;

import net.tsz.afinal.FinalDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter.ViewHolder;
import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.utils.PriceUtil;

public class ProductListActivity extends BaseGridActivity<Product> implements Constants {
	private FinalDb mDb;
	private boolean isBrand = false;
	@Override
	public int setLayoutId() {
		//如果是0  则证明是从二级分类进入的  否则是从一级目录进入的
		if(getIntent().getExtras().getInt("typeId",0) == 0){
			isBrand = true;
		}

		mTypeId = getIntent().getExtras().getInt(INTENT_ID);

		return R.layout.activity_product_list;
	}

	private int mTypeId;
	private ImageView mImgBack;
	private TextView mTvTitle;
	private TextView mTvNumAndPrice;//总价和数量
	private Button mBtnBuy;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mList = new ArrayList<Product>();
		mDb = AppContext.mDb;
		mCarList = new ArrayList<Car>();
		setBack();

		ImmersionModeUtils.setTranslucentStatus(ProductListActivity.this,R.color.main_red);

		mTvNumAndPrice = (TextView)findViewById(R.id.tv_price_num);

		mTvTitle = (TextView)findViewById(R.id.bar_title);
		mTvTitle.setText("商品列表");

		mBtnBuy = (Button)findViewById(R.id.btn_buy1);
		mBtnBuy.setOnClickListener(this);
		//加载购物车总价
		loadPriceData();
	}




	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null){
			convertView = getActivity().getLayoutInflater().inflate(R.layout.item_product, null);
		}

		TextView name = (TextView) ViewHolder.get(convertView, R.id.name);
		TextView price = (TextView) ViewHolder.get(convertView, R.id.price);
		ImageView img = (ImageView) ViewHolder.get(convertView, R.id.img);
		final TextView ePay = (TextView) ViewHolder.get(convertView, R.id.e);
		final TextView num = (TextView) ViewHolder.get(convertView, R.id.n);
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

		name.setText(g.getName());
		price.setText("￥" + PriceUtil.floatToString(g.getPrice()));
		AppContext.displayImage(img,g.getCover_img());

		//直接购买的事件加载
		String mPid = g.getProductId();

		try{
			//先判断数据库是否有数据
			List<Car> c = mDb.findAllByWhere(Car.class, "productId="+"'"+mPid+"' and is_norm=1");

			if(c != null){
				//如果存在数据
				if(c.size()>0){
					//将已经存在的数据叠加
					num.setVisibility(View.VISIBLE);
					num.setText(c.get(0).getNum()+"");
				}
			}
		}catch (Exception e){

		}



		imgCar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addCar(g, num);
			}
		});

		return convertView;

	}

	private void addCar(Product p,TextView tv){
		String mPid = p.getProductId();

		int count = 1;
		FinalDb db = AppContext.mDb;

		try {
			//先判断数据库是否有数据
			List<Car> c = db.findAllByWhere(Car.class, "productId=" + "'" + mPid + "'");
			//如果存在数据
			if (c.size() > 0) {
				//将已经存在的数据叠加
				count = count + c.get(0).getNum();
				//删除已经存在的数据
				db.deleteByWhere(Car.class, "productId=" + "'" + mPid + "'");
			}
		}catch (Exception e){

		}
		Car car = new Car();
		car.setProductId(mPid);
		car.setPrice(p.getPrice() + "");
		car.setIs_norm(PRODUCT_TYPE1);
		car.setNum(count);
		car.setActivity_id(-1);
		tv.setVisibility(View.VISIBLE);
		tv.setText(count+"");
		//保存数据
		AppContext.mDb.save(car);


		mPriceTotal = mPriceTotal+p.getPrice();
		String html = "订单总额:<font color='red'>"+ PriceUtil.floatToString(mPriceTotal)+"</font>元";
		mTvNumAndPrice.setText(Html.fromHtml(html));

	}
	private String fName;
	@Override
	public void loadData() {

		JSONObject jb = new JSONObject();
		try {
			if(mTypeId == -1){
				fName = RECOMMEND_PRODUCT_LIST;
				jb.put("pg", pageNo+"");
				jb.put("lat", AppContext.Latitude);
				jb.put("lng", AppContext.Longitude);
			}else{
				fName = PRODUCT_LIST;

				jb.put("shopId", AppContext.SHOP_ID);
				//typeId":0,"brandId":4
				if(isBrand){
					jb.put("typeId", 0);
					jb.put("brandId", mTypeId);
				}else{
					jb.put("typeId", mTypeId);
					jb.put("brandId", 0);
				}


//				jb.put("typeId", mTypeId+"");
				jb.put("pg", pageNo+"");
				jb.put("lat", AppContext.Latitude);
				jb.put("lng", AppContext.Longitude);

			}


		} catch (JSONException e) {
			e.printStackTrace();
		}
		mBaseHttpClient.postData(fName, jb, new onRecevieDataListener<Product>() {

			@Override
			public void onReceiverData(List<Product> data, String msg) {
				if(pageNo == mDefaultPage){
					mList.clear();
				}

				mList.addAll(data);
				setListAdapter();
			}

			@Override
			public void onReceiverError(String msg) {
//				showToast(msg);
				setLoadingFailed();
			}

			@Override
			public Class<Product> dataTypeClass() {
				return Product.class;
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_buy1:
			buy();
			break;
		}
	}


	/**
	 * 购买
	 */
	private void buy(){
		if(!AppContext.IS_LOGIN){
			startActivityForResult(new Intent(getActivity(),WechatAndPhoneLoginActivity.class), LOGIN_CODE1);
		}else{
			startActivity(new Intent(getActivity(),OrderMakeActivity.class).putExtra(TYPE_ID, PAY_TYPE1));
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode ==Activity.RESULT_OK){
			switch (requestCode) {
			case LOGIN_CODE1:
				startActivity(new Intent(getActivity(),OrderMakeActivity.class).putExtra(TYPE_ID,PAY_TYPE1));
				break;
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View view,
			int position, long num) {
		startActivity(new Intent(getActivity(),ProductDetailsActivity.class).putExtra(INTENT_ID,
				mList.get(position).getProductId()));

	}

	@Override
	protected void onResume() {
		super.onResume();
		loadPriceData();
	}

	private List<Car> mCarList;
	private int mPriceTotal;
	private List<Product> mCarProduct;
	///////总价参数处理
	public void loadPriceData() {
		mCarList = AppContext.mDb.findAll(Car.class);//查询所有的用户
//		mCarList = AppContext.mDb.findAllByWhere(Car.class, "is_norm=1");

		if(mCarProduct ==  null){
			mCarProduct = new ArrayList<Product>();
		}

		//如果购物车为空
		if(mCarList == null){
			String html = "订单总额:<font color='red'>0</font>元";
			mTvNumAndPrice.setText(Html.fromHtml(html));
		}else{
			JSONObject jb = new JSONObject();
			try {
				JSONArray ja = new JSONArray();
				for (Car car : mCarList) {
					JSONObject j = new JSONObject();
					j.put("productId",car.getProductId());
					j.put("is_norm",car.getIs_norm());
					j.put("activity_id",car.getActivity_id());
					ja.put(j);
				}
				jb.put("pids", ja.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}

			mBaseHttpClient.postData1(SHOPPING_CAR_LIST2,jb,new onResultListener() {

				@Override
				public void onResult(String data, String msg) {
					mCarProduct.clear();
					mCarProduct.addAll(JsonUtils.parseList(data, Product.class));

					mPriceTotal = 0;
					for(int i=0; i<mCarProduct.size();i++) {

						Product p = mCarProduct.get(i);
						Car c = mCarList.get(i);
						int n = c.getNum();
						int pp = p.getPrice();

						mPriceTotal = mPriceTotal+(n*pp);
					}

//					if(mPriceTotal<3000){
//						mBtnBuy.setEnabled(false);
//					}
					String html = "订单总额:<font color='red'>"+ PriceUtil.floatToString(mPriceTotal)+"</font>元";
					mTvNumAndPrice.setText(Html.fromHtml(html));
				}

				@Override
				public void onError(String error) {
					String html = "订单总额:<font color='red'>0</font>元";
					mTvNumAndPrice.setText(Html.fromHtml(html));
				}
			});
		}

	}

}
