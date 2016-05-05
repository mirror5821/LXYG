package com.lxyg.app.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.utils.SharePreferencesUtil;

public class SplashActivity extends BaseActivity {
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_start);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		//需设置此参数，否则无地址信息
		option.setAddrType("all");
		mLocClient.setLocOption(option);
		mLocClient.start();

	}



	MyLocationData locData;
	private int mLocTime = 0;
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if(SharePreferencesUtil.isFirstLoad(getApplicationContext())){
				AppContext.mDb.dropDb();
			}

			// map view 销毁后不在处理新接收的位置
			mLocTime = mLocTime+1;
			if(mLocTime == 5){
				AppContext.Latitude = 34.788675;
				AppContext.Longitude = 113.68455;

				AppContext.Address = "未获取到位置信息，请打开GPS定位";

				mLocClient.stop();

				startActivity();
			}
			if (location == null){
				return;
			}
			if(String.valueOf(location.getLatitude()).equals("4.9E-324")){
				return;
			}

			locData = new MyLocationData.Builder()
			.accuracy(location.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
			.direction(100).latitude(location.getLatitude())
			.longitude(location.getLongitude()).build();



			AppContext.Latitude = location.getLatitude();
			AppContext.Longitude = location.getLongitude();

			AppContext.Address = location.getAddrStr();
			AppContext.LOC_CITY = location.getCity();
			AppContext.LOC_AREA = location.getDistrict();

			mLocClient.stop();

			startActivity();

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * 开启界面
	 */
	private void startActivity(){
		String uId = SharePreferencesUtil.getShopId(getApplicationContext());
		if(TextUtils.isEmpty(uId)){
			//传这个值 是为了取消第一次选择区域的回退按钮
//			startActivity(new Intent(SplashActivity.this,AreaSelectListActivity.class).putExtra(INTENT_ID,2001));
			startActivity(new Intent(SplashActivity.this,GuidePageActivity.class));
		}else {
			AppContext.SHOP_ID = uId;
			startActivity(new Intent(SplashActivity.this,MainActivity.class));
		}

		finish();

		return;
	}
}
