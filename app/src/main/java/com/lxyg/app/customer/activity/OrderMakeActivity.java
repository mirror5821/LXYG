package com.lxyg.app.customer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Address;
import com.lxyg.app.customer.bean.Car;
import com.lxyg.app.customer.bean.Cash;
import com.lxyg.app.customer.bean.Order;
import com.lxyg.app.customer.bean.Product;
import com.lxyg.app.customer.bean.RedBag;
import com.lxyg.app.customer.bean.Service;
import com.lxyg.app.customer.bean.WxPay;
import com.lxyg.app.customer.iface.AliPayListener;
import com.lxyg.app.customer.utils.AliPayUtil;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.InstallUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import dev.mirror.library.utils.DateUtil;
import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.utils.PriceUtil;
import dev.mirror.library.view.SquareImageView;
import dev.mirror.library.view.WheelView;

/**
 * 订单确认
 * @author 王沛栋
 * 从购物车点击购买直接进入该界面
 *
 */
public class OrderMakeActivity extends BaseActivity implements OnCheckedChangeListener{
	private TextView mTvAddressInfo;
	private LinearLayout mViewAddressTv;
	private LinearLayout mViewProduct;
	private TextView mTvAddress,mTvDistrict,mTvNameAndPhone;
	private Button mBtnSub;
	private TextView mTvProudctTotal;

	private List<Product> mList;
	private List<Car> mCarList;
	private boolean hasDefaultAddress = false;
	private List<Address> mListAddress;
	private String mAddressId;


	private RadioGroup mRg1,mRg2;
	private TextView mTvTotal,mTvCase;
	private LinearLayout mViewTime;
	private TextView mTvTime,mTvECase,mTvERb;
	private RadioButton mRb11;
	private RadioButton mRb21,mRb22;
	private EditText mEtMarker;
	private TextView mTvPS;


	private Service mService;

	private int mPayTyep = 1;//1 微信支付 2支付宝支付 3货到付款
	private int mSendType = 1;
	private int mTotalPrice= 0;
	private String mSendDate,mSendHour;

	private boolean isPay = false;

	private int mHourNow,mHourStart,mHourEnd;
	private WheelView mWheelDate,mWheelTime;

	private final List<String> mListDate = new ArrayList<String>();

	private AlertDialog.Builder mTimeBuilder;
	private Dialog mTimeDialog;//
	private LinearLayout mViewCk,mViewRB;

	private String mOrderId;

	private View outerView;
	private CheckBox mCk,mCkRb;

	private int eCase = 0;
	private String mDataProduct;

	private Order mOrder;
	private int mPayFrom = 1;
	private String mProductId;
	private Bundle mBundle;
	private int mNum = 1;

	private double mLatShop = -1;//店铺精度
	private double mLngShop = -1;//店铺维度

	private double mLatAddress = -1;//配送地址精度
	private double mLngAddress = -1;//配送地址维度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_make_new);//activity_order_confirm
		ImageView iv = (ImageView)findViewById(R.id.left_icon);
		iv.setImageResource(R.mipmap.ic_back_w);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showNormalDialog("提示", "确定放弃本次购买？", "确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
			}
		});
		setRightTitle("配送范围");
		setTitleText("订单确认");

		mTvPS = (TextView)findViewById(R.id.peisong);
		mTvPS.setText(TextUtils.isEmpty(AppContext.PEI_SONG)?"":AppContext.PEI_SONG);

		mBundle = getIntent().getExtras();
		mPayFrom = mBundle.getInt(TYPE_ID);
		mListAddress = new ArrayList<Address>();

		mViewCk = (LinearLayout)findViewById(R.id.view_ck);
		mTvAddressInfo = initTextView(R.id.tv_address);
		mViewAddressTv = (LinearLayout)findViewById(R.id.view1);
		mViewAddressTv.setOnClickListener(this);
		mViewProduct = (LinearLayout)findViewById(R.id.view2);
		mTvAddress = initTextView(R.id.address);
		mTvDistrict = initTextView(R.id.district);
		mTvNameAndPhone = initTextView(R.id.name_phone);
		mBtnSub = initButton(R.id.sub);
		mEtMarker = initEditText(R.id.et_marker);

		mTvProudctTotal = initTextView(R.id.p_total);
		mList = new ArrayList<Product>();

		mCarList = new ArrayList<Car>();

		mRg1 = (RadioGroup)findViewById(R.id.rg1);
		mRg2 = (RadioGroup)findViewById(R.id.rg2);

		mRb11 = (RadioButton)findViewById(R.id.rb11);
		mRb21 = (RadioButton)findViewById(R.id.rb21);
		mRb22 = (RadioButton)findViewById(R.id.rb22);

		mViewRB = initLinearLayout(R.id.view_rb);
		mViewTime = initLinearLayout(R.id.view_time);
		mTvTime = initTextView(R.id.tv_time);
		mTvECase = initTextView(R.id.tv2);
		mTvERb = initTextView(R.id.tv_hb);
		mCk = (CheckBox)findViewById(R.id.ck_e);
		mCkRb = (CheckBox)findViewById(R.id.ck_hb);

		mCkRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mTvERb.setText(""+mRedBag);

					String t = PriceUtil.floatToString(mTotalPrice - (mRedBag*100));

					mTvTotal.setText(t);
					mBtnSub.setText("总计: " + t + "元");
				}else{
					mTvERb.setText("0");
					String t = PriceUtil.floatToString(mTotalPrice);
					mTvTotal.setText(t);
					mBtnSub.setText("总计: " + t + "元");
				}
			}
		});

		mCk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				//如果点击了
				if (arg1) {
					//如果电子现金余额不足
					if (mCash.getCash() < eCase) {
						mCk.setChecked(false);
						showToast("电子现金余额不足");
						return;
					}

					//设置电子现金显示金额
					mTvCase.setText(PriceUtil.floatToString(eCase));
					String t = PriceUtil.floatToString(mTotalPrice - eCase);

					mTvTotal.setText(t);
					mBtnSub.setText("总计: " + t + "元");

				} else {
					mTvECase.setText("0");
					String t = PriceUtil.floatToString(mTotalPrice);
					mTvTotal.setText(t);
					mBtnSub.setText("总计: " + t + "元");
				}
			}
		});

		initButton(R.id.sub);

		mViewTime.setVisibility(View.GONE);

		mTvTotal = initTextView(R.id.tv1);
		mTvCase = initTextView(R.id.tv2);

		mTvCase.setText("0");

		//默认选中
		mRb11.setChecked(true);


		mRb21.setChecked(true);

		//显示加载进度条
		showProgressDialog("正在加载数据...");
		//加载电子现金
		loadUserCash();
		//加载系统时间
		loadTime();
		//加载位置信息
		loadAddressData();

		//加载购物车
		loadShoppingCar();

		//获取店铺经纬度
		getShopCoordinte();
	}

	private Cash mCash;
	private void loadUserCash(){
		mBaseHttpClient.postData1(USER_CASH_TOTAL, new JSONObject(), new AppAjaxCallback.onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				mCash = JsonUtils.parse(data, Cash.class);
			}

			@Override
			public void onError(String error) {
			}
		});
	}

	/**
	 * 加载红包
	 */
	private List<RedBag> mListRB = new ArrayList<>();
	private void loadRedBag(){
		JSONObject jb = new JSONObject();
		try{
			jb.put("s_uid",AppContext.SHOP_ID);
		}catch (JSONException e){

		}
		mBaseHttpClient.postData1(RED_BAG, jb, new AppAjaxCallback.onResultListener() {
			@Override
			public void onResult(String data, String msg) {
				try {
					JSONObject j = new JSONObject(data);
					mListRB.addAll(JsonUtils.parseList(j.getString("rule"), RedBag.class));
					getRedBagTotal();

				} catch (JSONException e) {

				}

			}

			@Override
			public void onError(String error) {

			}
		});
	}

	private int mRedBag = 0;
	protected void getRedBagTotal(){
		int p = 0;
		int j = mListRB.size();
		float price = (float)mTotalPrice/100;
		for(int i=j-1;i>=0;i--){

			int a = mListRB.get(i).getLimit_price();
			if(price>=a){
				p = i+1;
				break;
			}
		}

		if(p == 0){
			mRedBag = 0;
		}else{
			mRedBag = mListRB.get(p-1).getReduce();
		}

		mTvERb.setText("" + mRedBag);

	}


	/*private int mRedBag = 0;
	protected void getRedBagTotal(){
		int p = 0;
		int j = mListRB.size();
		float price = (float)mTotalPrice/100;
		for(int i=j-1;i>=0;i--){

			int a = mListRB.get(i).getLimit_price();
			if(price>=a){
				p = i;
				break;
			}
		}

		mRedBag = mListRB.get(p).getReduce();
		mTvERb.setText(""+mRedBag);

	}*/


	private void getShopCoordinte(){
		JSONObject jb = new JSONObject();
		try {
			jb.put("s_uid",AppContext.SHOP_ID);
		}catch (JSONException e){

		}
		mBaseHttpClient.postData1(GET_SHOP_COORDINATE, jb, new AppAjaxCallback.onResultListener() {
			@Override
			public void onResult(String data, String msg) {
				try {
					JSONObject jb = new JSONObject(data);
					mLatShop = jb.getDouble("lat");
					mLngShop = jb.getDouble("lng");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String error) {

			}
		});
	}
	/**
	 * 加载购物车数据
	 */
	private void loadShoppingCar(){

		JSONObject jb = new JSONObject();

		switch (mPayFrom){

			case PAY_TYPE1:  //正常购物车进入
				mCarList = AppContext.mDb.findAll(Car.class);//查询所有的用户
				if(mCarList.size() == 0){
					showToast("您的购物车暂无商品");
					finish();
					return;
				}else{
					//先清空列表  再然后在加载
					mList.clear();

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
				}

				break;
			case PAY_TYPE2:
				//从订单列表进入的数据
				mOrder = JsonUtils.parse(mBundle.getString(INTENT_ID).toString(), Order.class);

				try {
					JSONArray ja = new JSONArray();
					//判断列表数据
					if(mOrder.getOrderActivityItems() != null){
						for (Order.orderItems car : mOrder.getOrderActivityItems()) {
							Product p = new Product();
							p.setNum(Integer.valueOf(car.getProduct_number()));
							p.setName(car.getName());
							p.setCash_pay(Integer.valueOf(car.getCash_pay()));
							p.setPrice(Integer.valueOf(car.getProduct_pay()));
							p.setProductId(car.getProduct_id());
							p.setCover_img(car.getCover_img());

							mList.add(p);
						}

						mTvProudctTotal.setText("共" + mList.size() + "件");

						for(int i=0; i<mList.size();i++) {

							View view = getLayoutInflater().inflate(R.layout.view_imageview_order_make, null);
							final SquareImageView img = (SquareImageView)view.findViewById(R.id.img_s);
							Product g = mList.get(i);
							AppContext.displayImage(img, g.getCover_img());
							mViewProduct.addView(view);


							switch (mPayFrom){
								case PAY_TYPE1:
									eCase = eCase + (g.getCash_pay()*mCarList.get(i).getNum());
									break;
								case PAY_TYPE2:
									eCase = eCase + g.getCash_pay();
									break;
								case PAY_TYPE3:
									mTvProudctTotal.setText("共" + mNum + "件");
									eCase = eCase + g.getCash_pay();
									break;
							}

						}

						mTvCase.setText(PriceUtil.floatToString(eCase));


						totalPirce();

						return;


					}else{
						for (Order.orderItems car : mOrder.getOrderItems()) {
							JSONObject j = new JSONObject();
							j.put("productId",Integer.valueOf(car.getProduct_id()));
							j.put("is_norm",Integer.valueOf(car.getIs_norm()));
							ja.put(j);
						}
					}


					jb.put("pids", ja.toString());
				} catch (JSONException e) {
					e.printStackTrace();
					showToast(e.getLocalizedMessage());
				}
				break;
			case 3://立即购
				mProductId = mBundle.getString(INTENT_ID);
				mNum = mBundle.getInt("NUMBER");
				try {
					JSONArray ja = new JSONArray();
					JSONObject j = new JSONObject();
					j.put("productId",Integer.valueOf(mProductId));
					j.put("is_norm",1);
					ja.put(j);
					jb.put("pids", ja.toString());
				} catch (JSONException e) {
					e.printStackTrace();
					showToast(e.getLocalizedMessage());
				}

				break;
		}

		mBaseHttpClient.postData1(SHOPPING_CAR_LIST2, jb,new AppAjaxCallback.onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				mDataProduct = data;
				mList.addAll(JsonUtils.parseList(data, Product.class));
				mTvProudctTotal.setText("共" + mList.size() + "件");

				for(int i=0; i<mList.size();i++) {

					View view = getLayoutInflater().inflate(R.layout.view_imageview_order_make, null);
					final SquareImageView img = (SquareImageView)view.findViewById(R.id.img_s);
					Product g = mList.get(i);
					AppContext.displayImage(img, g.getCover_img());
					mViewProduct.addView(view);


					switch (mPayFrom){
						case PAY_TYPE1:
							eCase = eCase + (g.getCash_pay()*mCarList.get(i).getNum());
							break;
						case PAY_TYPE2:
							eCase = eCase + g.getCash_pay();
							break;
						case PAY_TYPE3:
							mTvProudctTotal.setText("共" + mNum + "件");
							eCase = eCase + g.getCash_pay();
							break;
					}

				}

				mTvCase.setText(PriceUtil.floatToString(eCase));


				totalPirce();
			}

			@Override
			public void onError(String error) {
				showToast(error);
			}
		});
	}

	private List<Order.orderItems> mDataP = new ArrayList<>();
	private void totalPirce(){
		int price =0;

		switch (mPayFrom){
			case PAY_TYPE1:
				for(int i=0;i<mCarList.size();i++){
					Car c = mCarList.get(i);
					price = price+ mList.get(i).getPrice()*c.getNum();
				}

				mBtnSub.setText("总计: "+ PriceUtil.floatToString(price)+"元");


				break;
			case PAY_TYPE2:
				mDataP.clear();
				if(mOrder.getOrderActivityItems()!=null){
					mDataP = mOrder.getOrderActivityItems();
				}else{
					mDataP = mOrder.getOrderItems();
				}

				for(int i=0;i<mDataP.size();i++){
					Order.orderItems o = mDataP.get(i);
					price = price + o.getProduct_price()*Integer.valueOf(o.getProduct_number());
				}
				break;
			case PAY_TYPE3:
				price = mList.get(0).getPrice()*mNum;
				break;
		}


		mTotalPrice = price;
		String pay = PriceUtil.floatToString(mTotalPrice);
		mBtnSub.setText("总计: "+pay+"元");
		mTvTotal.setText(pay);

		loadRedBag();
	}

	private Address mAddress;

	/**
	 * 加载地址数据
	 */
	private void loadAddressData(){
		//先网络请求默认地址
		JSONObject jb = new JSONObject();

		mBaseHttpClient.postData(ADDRESS_LIST, jb,new AppAjaxCallback.onRecevieDataListener<Address>() {

			@Override
			public void onReceiverData(List<Address> data, String msg) {
				if(data.size()==0){
					emptyAddress();
				}else{
					hasDefaultAddress = true;
					mListAddress.addAll(data);
					mAddress = data.get(0);
					initAddress();
				}
			}

			@Override
			public void onReceiverError(String msg) {
				showToast(msg);
			}

			@Override
			public Class<Address> dataTypeClass() {
				return Address.class;
			}
		});
	}

	private  void initAddress(){
		mAddressId = mAddress.getAddressId();
		mTvAddress.setText(mAddress.getStreet());
		mTvDistrict.setText(mAddress.getProvince_name() + mAddress.getCity_name() + mAddress.getArea_name());
		mTvNameAndPhone.setText(mAddress.getName()+" "+mAddress.getPhone());

		mLatAddress = mAddress.getLat();
		mLngAddress = mAddress.getLng();

		mTvAddress.setOnClickListener(this);
		mTvDistrict.setOnClickListener(this);
		mTvNameAndPhone.setOnClickListener(this);
	}

	/**
	 * 设置地址为空时的现实
	 */
	private void emptyAddress(){
		mTvAddressInfo.setText("请添加收货地址");
		mViewAddressTv.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			//查看商品列表
			case R.id.p_total:
				if(mPayFrom == 2){
					startActivity(new Intent(OrderMakeActivity.this,OrderProductListActivity.class).putExtra(INTENT_ID, mBundle.getString(INTENT_ID).toString()).
							putExtra(TYPE_ID,mPayFrom).putExtra("NUMBER",mNum));
				}else{
					startActivity(new Intent(OrderMakeActivity.this,OrderProductListActivity.class).putExtra(INTENT_ID, mDataProduct).
							putExtra(TYPE_ID,mPayFrom).putExtra("NUMBER",mNum));
				}

				break;
			case R.id.tv_address:
				//如果有 则进入地址列表
				if(hasDefaultAddress){
					startActivityForResult(new Intent(OrderMakeActivity.this,AddressListActivity.class).
									putParcelableArrayListExtra(INTENT_ID, (ArrayList<? extends Parcelable>) mListAddress),
							ADDRESS_SELECT_REQUESTCODE);
				}else{//如果没有 则进入添加地址
					startActivityForResult(new Intent(OrderMakeActivity.this,AddressAddActivity.class).putExtra(INTENT_ID, 1),
							ADDRESS_ADD_REQUESTCODE);
				}
				break;
			case R.id.tv_time:
				showTimeDialog();
				break;
			case R.id.sub:
				if(TextUtils.isEmpty(mAddressId)){
					showToast("请添加收货地址!");
					return;
				}
				sub();
				break;
			//修改收货地址
			case R.id.view1:
				startActivityForResult(new Intent(OrderMakeActivity.this, AddressUpdateActivity.class).putExtra(INTENT_ID, 1).putExtra("bean", JsonUtils.beanToString(mAddress, Address.class)), ADDRESS_UPDATE);
				break;
			case R.id.address:
				startActivityForResult(new Intent(OrderMakeActivity.this, AddressUpdateActivity.class).putExtra(INTENT_ID, 1).putExtra("bean", JsonUtils.beanToString(mAddress, Address.class)), ADDRESS_UPDATE);
				break;
			case R.id.district:
				startActivityForResult(new Intent(OrderMakeActivity.this, AddressUpdateActivity.class).putExtra(INTENT_ID, 1).putExtra("bean", JsonUtils.beanToString(mAddress, Address.class)), ADDRESS_UPDATE);
				break;
			case R.id.name_phone:
				startActivityForResult(new Intent(OrderMakeActivity.this, AddressUpdateActivity.class).putExtra(INTENT_ID, 1).putExtra("bean", JsonUtils.beanToString(mAddress, Address.class)), ADDRESS_UPDATE);
				break;
			case R.id.right_text:
				startActivity(new Intent(OrderMakeActivity.this,DistributionRangeActivity.class));
				break;
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode ==Activity.RESULT_OK){
			switch (requestCode) {
				case ADDRESS_ADD_REQUESTCODE:
					mViewAddressTv.setVisibility(View.VISIBLE);
					Bundle mBundle = data.getExtras();

					mAddress = mBundle.getParcelable(ADR);
					initAddress();
					break;

				case ADDRESS_SELECT_REQUESTCODE:
					mViewAddressTv.setVisibility(View.VISIBLE);
					Bundle mBundle2 = data.getExtras();

					mAddress = mBundle2.getParcelable(ADR);
					initAddress();
					break;
				case ADDRESS_UPDATE:
					Bundle mBundle3 = data.getExtras();

					mAddress = mBundle3.getParcelable(ADR);
					initAddress();
					break;

			}

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(isPay){
			isPay = false;
			if(AppContext.IS_PAY_OK){
				showToast("支付完成");
				AppContext.mDb.deleteAll(Car.class);

				sendType();

//				finish();
			}else{
				showToast("支付失败");
			}
			mBtnSub.setEnabled(true);
		}

	}
	//在加载完时间数据后  绑定view的相应事件
	private void initView(){
		mRg1.setOnCheckedChangeListener(this);
		mRg2.setOnCheckedChangeListener(this);

		mTvTime.setOnClickListener(this);
	}


	@SuppressWarnings({ "deprecation", "static-access" })
	private void initSendTime(){

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		DateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateTime = new Date();
		for(int i=0;i<3;i++){
			try {
				dateTime = formatter.parse(mService.getCurrentTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}

			mHourNow = dateTime.getHours();
			//获取当前时间如果超过20点 则默认是明天送货
			if(mHourNow>23){
				i = i+1;
			}
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(dateTime);

			calendar.add(calendar.DATE,i);//把日期往后增加一天.整数往后推,负数往前移动
			dateTime=calendar.getTime(); //这个时间就是日期往后推一天的结果

			Service s = new Service();
			s.setDate(formatter.format(dateTime));

			mListDate.add(formatter.format(dateTime));

		}

		//设置默认日期
		mSendDate = mListDate.get(0);

		try {
			//获取现在服务器时间
			dateTime = formatTime.parse(mService.getCurrentTime());
			mHourNow = dateTime.getHours();



			//获取开始时间
			dateTime = formatTime.parse(mService.getStartTime());
			mHourStart = dateTime.getHours();

			//获取当前时间如果超过20点 则默认是明天送货 则默认使用明天的开始时间
			if(mHourNow>23){
				mHourNow = mHourStart;
			}else{
				mHourNow = mHourNow+2;
			}

			//获取结束时间
			dateTime = formatTime.parse(mService.getEndTime());
			mHourEnd = dateTime.getHours();

		} catch (ParseException e) {
			e.printStackTrace();
		}

		outerView = getLayoutInflater().inflate(R.layout.view_wheel, null);

		//日期wheel
		mWheelDate = (WheelView) outerView.findViewById(R.id.wheel1);
		mWheelDate.setItems(mListDate);

		//时间wheel
		mWheelTime= (WheelView) outerView.findViewById(R.id.wheel2);

		//默认加载第一次的时间
		List<String> t = new ArrayList<String>();
		for(int j=mHourNow;j<mHourEnd;j++){
			t.add(j+":00-"+(j+1)+":00");
		}

		//设置默认时间
		mSendHour = t.get(0);
		mWheelTime.setItems(t);

		mWheelDate.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				mSendDate = item;

				List<String> d =null;
				if(d == null){
					d = new ArrayList<String>();
				}
				d.clear();
				//判断下表
				if(selectedIndex == 1){
					for(int j=mHourNow;j<mHourEnd;j++){
						d.add(j+":00-"+(j+1)+":00");
					}
				}else{
					for(int j=mHourStart;j<mHourEnd;j++){
						d.add(j+":00-"+(j+1)+":00");
					}
				}
				mWheelTime.setItems(d);
			}
		});


		mWheelTime.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				mSendHour = item;
			}
		});

	}



	/**
	 * 显示设置网络的弹出框
	 */
	private void showTimeDialog(){
		if(mTimeBuilder == null){
			mTimeBuilder = new AlertDialog.Builder(getActivity());

			mTimeBuilder.setTitle("请选择配送时间!");
			mTimeBuilder.setView(outerView);
			mTimeBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					//打开设置网络界面
					String send = mSendDate+" "+mSendHour;
					send = send.substring(0, send.lastIndexOf("-"));
					mTvTime.setText(send+":00");
					return;

				}
			});
			mTimeBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

			mTimeDialog = mTimeBuilder.create();
		}

		if(mTimeDialog.isShowing()){
			mTimeDialog.dismiss();
			mTimeDialog.cancel();
		}else{
			mTimeDialog.show();
		}

	}


	/**
	 * 支付
	 */

	private void sub(){
//		if (mLatShop == -1 || mLatAddress == -1 || mLngAddress == -1 || mLngShop == -1){
//			showToast("对不起，暂无获取到位置信息！");
//			cancelProgressDialog();
//			return;
//		}

//		//判断距离是否大于1000
//		LatLng pt1 = new LatLng(mLatAddress, mLngAddress);
//		LatLng pt2 = new LatLng(mLatShop, mLngShop);


		showProgressDialog("正在提交订单");
		JSONObject jb = new JSONObject();
		try {
			jb.put("shopId", AppContext.SHOP_ID);
			jb.put("payType", mPayTyep);
			jb.put("addressId", mAddressId);

			jb.put("sendType", mSendType);

//			if(mCk.isChecked()){
//				jb.put("cashPay", eCase);
//				jb.put("price", (mTotalPrice-eCase));
//			}else{
//				jb.put("cashPay", 0);
//				jb.put("price", (mTotalPrice));
//			}

			if(mCkRb.isChecked()){
				jb.put("cashPay", mRedBag);
				jb.put("price", (mTotalPrice-(mRedBag*100)));
			}else{
				jb.put("cashPay", 0);
				jb.put("price", (mTotalPrice));
			}

			if(mSendType == 2){
				String send = mSendDate+" "+mSendHour;
				send = send.substring(0, send.lastIndexOf("-"));

				jb.put("sendTime", DateUtil.Time2Unix(send + ":00"));
			}

			jb.put("remark",mEtMarker.getText().toString());


			JSONArray ja = new JSONArray();

			switch (mPayFrom){
				case PAY_TYPE1://购物车进入
					mCarList = AppContext.mDb.findAll(Car.class);//查询所有的用户


					//如果购物车为空
					if(mCarList.size()==0){
						showToast("您的购物车是空的!");
						mBtnSub.setEnabled(true);

						cancelProgressDialog();
						return;
					}


					for(int i=0; i<mList.size();i++) {
						JSONObject jbCar = new JSONObject();

						Car car = mCarList.get(i);
						Product p = mList.get(i);
						jbCar.put("number", car.getNum());
						jbCar.put("p_type_id", "0");
						jbCar.put("hide", "0");
						jbCar.put("p_brand_id", "0");
						jbCar.put("price", p.getPrice());
						jbCar.put("index_show", "0");
						jbCar.put("productId", car.getProductId());
						jbCar.put("is_norm",car.getIs_norm());
						jbCar.put("activity_id",car.getActivity_id());
//						if(mCk.isChecked()){
//							jbCar.put("cashPay", p.getCash_pay());
//						}else{
//							jbCar.put("cashPay", 0);
//						}

						//此处默认传0即可
						jbCar.put("cashPay", 0);

						jbCar.put("server_id", "0");
						jbCar.put("productNum",car.getNum());
						jbCar.put("productPay",p.getPrice());//实付款

						ja.put(jbCar);

					}

					break;
				case PAY_TYPE2://订单进入
					mDataP = mOrder.getOrderItems();
					for(int i=0;i<mDataP.size();i++){
						Order.orderItems o = mDataP.get(i);

						JSONObject jbCar = new JSONObject();

						Product p = mList.get(i);
						jbCar.put("number", o.getProduct_number());
						jbCar.put("p_type_id", "0");
						jbCar.put("hide", "0");
						jbCar.put("p_brand_id", "0");
						jbCar.put("price", p.getPrice());
						jbCar.put("index_show", "0");
						jbCar.put("productId", o.getProduct_id());
						jbCar.put("is_norm",o.getIs_norm());
						jbCar.put("activity_id",o.getActivity_id());
						if(mCk.isChecked()){
							jbCar.put("cashPay", p.getCash_pay());
						}else{
							jbCar.put("cashPay", 0);
						}

						jbCar.put("server_id", "0");
						jbCar.put("productNum",o.getProduct_number());
						jbCar.put("productPay",p.getPrice());//实付款

						ja.put(jbCar);

					}

					break;
				case PAY_TYPE3://立即购
					Product product = mList.get(0);
					int price = product.getPrice();
					String pId = product.getProductId();
					int isNorm = product.getIs_norm();

					JSONObject jbCar = new JSONObject();

					jbCar.put("number", mNum);
					jbCar.put("p_type_id", "0");
					jbCar.put("hide", "0");
					jbCar.put("p_brand_id", "0");
					jbCar.put("price", price);
					jbCar.put("index_show", "0");
					jbCar.put("productId", pId);
					jbCar.put("is_norm",1);
					jbCar.put("activity_id",-1);
					if(mCk.isChecked()){
						jbCar.put("cashPay",eCase);
//						jbCar.put("cashPay", product.getCash_pay());
					}else{
						jbCar.put("cashPay", 0);
					}

					jbCar.put("server_id", "0");
					jbCar.put("productNum",mNum);
					jbCar.put("productPay",price);//实付款

					ja.put(jbCar);

					break;
			}

			jb.put("orderItems", ja);


			System.out.println("jb-------------"+jb.toString());


		} catch (JSONException e) {
			e.printStackTrace();
			mBtnSub.setEnabled(true);
		}

		mBaseHttpClient.postData1(ORDER_MARK, jb, new AppAjaxCallback.onResultListener() {

//		mBaseHttpClient.postData1(ORDER_MAKE2, jb, new AppAjaxCallback.onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				cancelProgressDialog();
				try {
					mOrderId = new JSONObject(data).getString("order_id");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				//如果选择的是到店自取
				if(mPayTyep == 3){
					showToast(msg);
					//清除购物车
					AppContext.mDb.deleteAll(Car.class);
					mBtnSub.setEnabled(true);
					finish();
					return;

				}

				if(mPayTyep == 1){
					if(InstallUtil.isWeixinAvilible(getApplicationContext())){
						wePay();
					}else{
						showToast("检测到您未安装微信!");
					}


					return;
				}
				int p = mTotalPrice;
				if(mCkRb.isChecked()){
					p = mTotalPrice-(mRedBag*100);
//					p = p-eCase;
				}

				new AliPayUtil().pay("商品支付",getActivity(), new AliPayListener() {

					@Override
					public void waitForComfrim(String msg) {
						showToast(msg);
					}

					@Override
					public void success(String msg) {
						showToast(msg);
						//清除购物车
						AppContext.mDb.deleteAll(Car.class);
						mBtnSub.setEnabled(true);

						sendType();
//						finish();
					}

					@Override
					public void error(String msg) {
						mBtnSub.setEnabled(true);
						showToast(msg);
					}
				},Float.valueOf(PriceUtil.floatToString(p)),mOrderId);


			}

			@Override
			public void onError(String error) {
				mBtnSub.setEnabled(true);
				showToast(error);
				cancelProgressDialog();
			}
		});
	}


	private PayReq mReq;
	private WxPay mWxPay;
	private void wePay(){
		JSONObject jb = new JSONObject();
		try {
			jb.put("orderId", mOrderId);
			jb.put("ip", "192.168.0.158");
			//			jb.put("ip", IpUtil.getLocalIpAddress());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mBaseHttpClient.postData1(WX_PAY, jb, new AppAjaxCallback.onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				mWxPay = JsonUtils.parse(data, WxPay.class);

				if(mReq == null){
					mReq = new PayReq();
				}

				mReq.appId = mWxPay.getAppid();
				mReq.partnerId = mWxPay.getPartnerid();// "1263785401";//mWxPay.MCH_ID;
				mReq.prepayId = mWxPay.getPrepayid();
				mReq.packageValue = "Sign=WXPay";
				mReq.nonceStr =  mWxPay.getNoncestr();
				mReq.timeStamp = mWxPay.getTimestamp();
				mReq.sign = mWxPay.getSign();

				IWXAPI WXapi= AppContext.WX_API;
				isPay = true;
				WXapi.sendReq(mReq);
			}

			@Override
			public void onError(String error) {
				mBtnSub.setEnabled(true);
			}
		});


	}

	private Dialog mDialog;
	private int mSend = 1;
	private boolean payOk = false;//用于设置键盘后退事件，如果支付成功，后退事件发生不用强调是否放弃支付
	private void sendType(){
		payOk = true;
		View view = LayoutInflater.from(OrderMakeActivity.this).inflate(R.layout.view_send_type,null);
		RadioButton rb = (RadioButton)view.findViewById(R.id.rb1);
		RadioGroup rg = (RadioGroup)view.findViewById(R.id.rg);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					//送货上门
					case R.id.rb1:
						mSend = 1;
						break;
					//到点自取
					case R.id.rb2:
						mSend = 2;
						break;
				}
			}
		});
		rb.setChecked(true);

		Button btn = (Button)view.findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showProgressDialog("正在提交数据!");
				JSONObject jb = new JSONObject();
				try{
					//order_id:"',send_type:""
					jb.put("order_id",mOrderId);
					jb.put("send_goods_type",mSend);
				}catch (JSONException e){

				}

				System.out.println("jbb----"+jb.toString());

				mBaseHttpClient.postData1(ORDER_SEND, jb, new AppAjaxCallback.onResultListener() {
					@Override
					public void onResult(String data, String msg) {
						cancelProgressDialog();
						showToast(msg);
						mDialog.dismiss();
						finish();
					}

					@Override
					public void onError(String msg) {
						cancelProgressDialog();
						showToast(msg);
					}
				});
			}
		});


		showNormalDialogByCustomerView(view, new DialogIface() {
			@Override
			public void d(Dialog dialog) {
				mDialog = dialog;
			}
		});




	}


	/**
	 * 加载服务器时间
	 */
	private void loadTime(){
////		RequestParams params = new RequestParams(Constants.HOST_HEADER+CURRENT_TIME);
//		x.http().post(new AppAjaxParam(CURRENT_TIME,Constants.HOST_HEADER+CURRENT_TIME),
//				new Callback.CommonCallback<String>() {
//			@Override
//			public void onSuccess(String result) {
//				cancelProgressDialog();
//				try {
//					System.out.println("starttime---------"+result);
//					JSONObject j = new JSONObject(result);
//					mService = new Service();
//					JSONObject jb = new JSONObject(j.getString("data"));
//					System.out.println("starttime---------"+jb.toString());
////					String data = jb.getString("data");
//					String str = jb.toString();
//
//
//					mService.setStartTime(str.substring(str.indexOf(":")+1,str.indexOf(",")).replace("\"", ""));
//					mService.setCurrentTime(str.substring(str.indexOf("currentTime") + 13, str.indexOf("endTime") - 2).replace("\"", ""));
//					mService.setEndTime(str.substring(str.indexOf("endTime") + 10, str.lastIndexOf("}") - 1).replace("\"", ""));
//
////					System.out.println(str.substring(str.indexOf(":") + 1, str.indexOf(",")).replace("\"", ""));
////					System.out.println(str.substring(str.indexOf("currentTime")+13,str.indexOf("endTime")-2).replace("\"", ""));
////					System.out.println(str.substring(str.indexOf("endTime")+10,str.lastIndexOf("}")-1).replace("\"", ""));
//////					mService.setStartTime(str.substring());
////					mService.setStartTime(jb.getString("startTime"));
////					mService.setCurrentTime(jb.getString("currentTime"));
////					mService.setEndTime(jb.getString("endTime"));
////					mService = JsonUtils.parse(jb.getString("data"), Service.class);
//
//
//
//					initView();
//					initSendTime();
//				}catch (JSONException e){
//				}
//
//			}
//
//			@Override
//			public void onError(Throwable ex, boolean isOnCallback) {
//				showToast(ex.getLocalizedMessage());
//				cancelProgressDialog();
//				finish();
//			}
//
//			@Override
//			public void onCancelled(CancelledException cex) {
//
//			}
//
//			@Override
//			public void onFinished() {
//
//			}
//		});


		//2016-01-22 08:00:00

		mBaseHttpClient.postData1(SYSTEM_TIME_UNIX, new JSONObject(), new AppAjaxCallback.onResultListener() {
			@Override
			public void onResult(String data, String msg) {


				cancelProgressDialog();
				try {
					JSONObject jb = new JSONObject(data);

					if(mService ==null){
						mService = new Service();
					}
					mService.setStartTime(DateUtil.TimeStamp2Date(jb.getString("startTime"), "yyyy-MM-dd HH:mm:ss"));
					mService.setCurrentTime(DateUtil.TimeStamp2Date(jb.getString("currentTime"), "yyyy-MM-dd HH:mm:ss"));
					mService.setEndTime(DateUtil.TimeStamp2Date(jb.getString("endTime"), "yyyy-MM-dd HH:mm:ss"));
				}catch(JSONException e){
				}
				initView();
				initSendTime();
			}

			@Override
			public void onError(String msg) {
				showToast(msg);
				cancelProgressDialog();
//				finish();
			}
		});

//		NetUtil.loadData(CURRENT_TIME, new JSONObject(), new PostDataListener() {
//
//			@Override
//			public void getDate(String data, String msg) {
//
//				cancelProgressDialog();
//				mService = JsonUtils.parse(data, Service.class);
//
//				initView();
//				initSendTime();
//			}
//
//			//一旦请求系统时间出错 则结束订单支付
//			@Override
//			public void error(String msg) {
//				showToast("操作失败，请重试!");
//				cancelProgressDialog();
//				finish();
//			}
//		});
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (group.getId()) {
			case R.id.rg1:
				switch (checkedId) {
					case R.id.rb11:
						mSendType = 1;
						mViewTime.setVisibility(View.GONE);
						break;

					case R.id.rb12:
						mSendType = 2;
						showTimeDialog();
						mViewTime.setVisibility(View.VISIBLE);
						break;
				}
				break;

			case R.id.rg2:
				switch (checkedId) {
					case R.id.rb21:
						mPayTyep = 1;
//						mViewCk.setVisibility(View.VISIBLE);
						mViewRB.setVisibility(View.VISIBLE);
						break;

					case R.id.rb22:
						mPayTyep = 2;
//						mViewCk.setVisibility(View.VISIBLE);
						mViewRB.setVisibility(View.VISIBLE);
						break;
					case R.id.rb23:
						showPayTypeDialog();

						mViewCk.setVisibility(View.GONE);
						mViewRB.setVisibility(View.GONE);
						//				mPayTyep = 3;
						break;
				}
				break;
		}
	}

	private AlertDialog.Builder mBuilderPay;
	private Dialog mDialogPay;
	/**
	 *
	 */
	private void showPayTypeDialog(){
		mCk.setChecked(false);
		mCkRb.setChecked(false);
//		mCk.setFocusable(false);
//		mCkRb.setFocusable(false);
		mPayTyep = 3;
//		if(mBuilderPay == null){
//			mBuilderPay = new AlertDialog.Builder(getActivity());
//
//			mBuilderPay.setTitle("提示!");
//			mBuilderPay.setMessage("吾皇，您的红包抵现不支持到店自取！建议选择第三方支付，保障您的权益，重新选择好吗？");
//			mBuilderPay.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					if(mPayTyep == 1){
//						mRg2.check(R.id.rb21);
//					}else{
//						mRg2.check(R.id.rb22);
//					}
//					return;
//
//				}
//			});
//			mBuilderPay.setNegativeButton("继续使用", new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.cancel();
//					//电子现金勾选去掉  货到付款不能使用电子现金
//					mCk.setChecked(false);
//					mPayTyep = 3;
//				}
//			});
//
//			mDialogPay = mBuilderPay.create();
//		}
//
//		if(mDialogPay.isShowing()){
//			mDialogPay.dismiss();
//			mDialogPay.cancel();
//		}else{
//			mDialogPay.show();
//		}

	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && !payOk){
			showNormalDialog("提示", "确定放弃本次购买？", "确定",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
		}

		return false;

	}

}
