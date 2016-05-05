package com.lxyg.app.customer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Address;
import com.lxyg.app.customer.bean.Distribution;
import com.lxyg.app.customer.iface.AddrBase;
import com.lxyg.app.customer.utils.AppAjaxCallback;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;
import dev.mirror.library.utils.JsonUtils;

public class MapSelectActivity extends BaseActivity {
	private ListView mListView;
	private ImageView mImgLoc;
	private EditText mEtSearch;
	private ImageView mImgSearch;
	
	private MapView mMapView = null;
	private BaiduMap mBaiduMap;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	
	private static Context mContext;
	private double mLat,mLng;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_select);
		setBack();
//		setTitleText("选择位置");
		mContext = getApplicationContext();
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		mListView = (ListView)findViewById(R.id.list);
		mImgLoc = (ImageView)findViewById(R.id.loc);
		mEtSearch = (EditText)findViewById(R.id.et);
		mImgSearch = (ImageView)findViewById(R.id.search);

		mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if(EditorInfo.IME_ACTION_SEARCH == actionId){
					String addStr = mEtSearch.getText().toString().trim();
					if(TextUtils.isEmpty(addStr)){
						showToast("请输入您要搜索的位置");
					}else{
						getLatLngByAddressStr(addStr);
					}

				}
				return false;
			}
		});

		mImgLoc.setOnClickListener(this);
		mImgSearch.setOnClickListener(this);

		mBaiduMap = mMapView.getMap();

		//设置显示级别 3-18
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(18));
		
		//定位模式
		mCurrentMode = LocationMode.NORMAL;
		// 传入null则，恢复默认图标
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true,null));

		// 开启定位图层  暂时关闭后不会出现定位图层 
		//		mBaiduMap.setMyLocationEnabled(true);
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

		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

			@Override
			public void onMapStatusChangeStart(MapStatus status) {

			}

			@Override
			public void onMapStatusChangeFinish(MapStatus status) {
				LatLng center = status.target;

				mLat = center.latitude;
				mLng = center.longitude;

				getAddstrs(center);
			}

			@Override
			public void onMapStatusChange(MapStatus status) {

			}
		});

		loadDefaultArea();

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()){
			case R.id.loc:
				LatLng ll = new LatLng(AppContext.Latitude,
						AppContext.Longitude);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);

				break;
			case R.id.search:
				String addStr = mEtSearch.getText().toString().trim();
				if(TextUtils.isEmpty(addStr)){
					showToast("请输入您要搜索的位置");
					return;
				}
				getLatLngByAddressStr(addStr);

				break;
		}
	}

	MyLocationData locData;
	private boolean IS_FIRST_LOC = true;
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			if(IS_FIRST_LOC){
				locData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(100).latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();
				mBaiduMap.setMyLocationData(locData);

				AppContext.Latitude = location.getLatitude();
				AppContext.Longitude = location.getLongitude();

				AppContext.Address = location.getAddrStr();
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);

				mLocClient.stop();
			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	private String mCity;
	private String mCountry;
	private String mDistrict;
	private String mProvince;

	private void getAddstrs(LatLng center){
		FinalHttp fh = new FinalHttp();

		String url = "http://api.map.baidu.com/geocoder/v2/?ak=Vip0XHEvRRKbs9dPgMpNclzt&callback=renderReverse&location="+
				center.latitude+","+center.longitude+"&output=json&pois=1";
		fh.get(url, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				t = t.substring(t.indexOf("(")+1,t.lastIndexOf(")"));

				try{
					JSONObject jb = new JSONObject(t);
					String status = jb.getString("status");

					if(status.equals("0")){
						JSONObject result = jb.getJSONObject("result");
						JSONObject addressComponent = result.getJSONObject("addressComponent");
						mCity = addressComponent.getString("city");
						mCountry = addressComponent.getString("country");
						mDistrict = addressComponent.getString("district");
						mProvince = addressComponent.getString("province");

						showToast(mCountry+mProvince+mCity+mDistrict);
						
						String formatted_addStr= result.getString("formatted_address");
						final List<Address> lists = new ArrayList<Address>();
						//这是返回的推荐地址
						Address a = new Address();
						a.setAddr(formatted_addStr.replace(mProvince+mCity+mDistrict, ""));
						a.setName(" ");
						lists.add(a);
						//这是解析的其他列表地址
						lists.addAll(JsonUtils.parseList(result.getString("pois").replace(mProvince + mCity + mDistrict, ""), Address.class));
						
						mListView.setAdapter(new AddrAdapter<Address>(mContext, lists));
						mListView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								Intent i = new Intent(MapSelectActivity.this,AddressAddActivity.class);
								i.putExtra(LAT, mLat);
								i.putExtra(LNG,mLng );
								i.putExtra(ADDRESS, lists.get(position).getAddr());
								i.putExtra(COUNTRY,mCountry );
								i.putExtra(PROVINCE, mProvince);
								i.putExtra(CITY,mCity );
								i.putExtra(DISTRICT, mDistrict);
								
								setResult(RESULT_OK, i);
								finish();
							}
							
						});
					}else{

					}
				}catch(JSONException e){
					loadDefaultAreaForNoLoc();
//					showToast("e------"+e.getLocalizedMessage());
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				loadDefaultAreaForNoLoc();
			}
		});

	}
	
	public static class AddrAdapter<T extends AddrBase>extends DevListBaseAdapter<T> {

		public AddrAdapter(Context context, List<T> list) {
			super(context, list);
		}

		@Override
		public View initView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_selector, null);
			}
			TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
			tv.setText(getItem(position).getAddrName());
			return convertView;
		}

	}


	private void getLatLngByAddressStr(String str){
		String url = "http://api.map.baidu.com/geocoder/v2/?address="+str+"&output=json&ak=Vip0XHEvRRKbs9dPgMpNclzt&callback=showLocation";
		RequestParams rq = new RequestParams(url);
		x.http().post(rq, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				result = result.substring(result.indexOf("(")+1,result.lastIndexOf(")"));
				try {
					JSONObject jb = new JSONObject(result);
					String status = jb.getString("status");

					if (status.equals("0")) {
						JSONObject loc = new JSONObject(result);
						JSONObject jj = loc.getJSONObject("result");
						JSONObject ll = jj.getJSONObject("location");
						double lat = ll.getDouble("lat");
						double lng = ll.getDouble("lng");

						LatLng l = new LatLng(lat,
								lng);
						MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(l);
						mBaiduMap.animateMapStatus(u);

					}
				}catch (JSONException e){
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {

			}

			@Override
			public void onCancelled(CancelledException cex) {

			}

			@Override
			public void onFinished() {

			}
		});

	}


	private List<LatLng> mPts = new ArrayList<>();
	private List<Distribution> mList;
	private void loadDefaultArea(){
		JSONObject jb = new JSONObject();
		try{
			jb.put("s_uid",AppContext.SHOP_ID);
		}catch (JSONException e){

		}
		mPts.clear();
		mBaseHttpClient.postData1(SHOP_AREA, jb, new AppAjaxCallback.onResultListener() {
			@Override
			public void onResult(String data, String msg) {
				if (mList == null) {
					mList = new ArrayList<>();
				}
				try {
					JSONObject jb = new JSONObject(data);
					mList.clear();
					mList.addAll(JsonUtils.parseList(jb.getString("scope"), Distribution.class));


					for (Distribution d : mList) {
						LatLng ll = new LatLng(d.getLat(), d.getLng());
						mPts.add(ll);
					}

					initArea();
				} catch (JSONException e) {
					showToast(e.getLocalizedMessage());
				}


			}

			@Override
			public void onError(String error) {
				showToast(error);
			}
		});

	}

	private void initArea(){
		mBaiduMap.clear();
		int pts = mPts.size();


		if(pts == 2){
			OverlayOptions ooPolyline = new PolylineOptions().width(5).color(0xAA00FF00).points(mPts);
			//添加在地图中
			mBaiduMap.addOverlay(ooPolyline);
		}
		if(pts>2){
			OverlayOptions polygonOption = new PolygonOptions().points(mPts).
					stroke(new Stroke(5,0xAA00FF00)).fillColor(0xAA00FF00);
			mBaiduMap.addOverlay(polygonOption);
		}

		//地图中心转移到店铺第一个点
		LatLng ll = mPts.get(0);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.animateMapStatus(u);

		initDefaultAddress(ll);
	}


	private void loadDefaultAreaForNoLoc(){
		JSONObject jb = new JSONObject();
		try{
			jb.put("s_uid",AppContext.SHOP_ID);
		}catch (JSONException e){

		}
		mPts.clear();
		mBaseHttpClient.postData1(SHOP_AREA, jb, new AppAjaxCallback.onResultListener() {
			@Override
			public void onResult(String data, String msg) {
				if (mList == null) {
					mList = new ArrayList<>();
				}
				try {
					JSONObject jb = new JSONObject(data);
					mList.clear();
					mList.addAll(JsonUtils.parseList(jb.getString("scope"), Distribution.class));


					for (Distribution d : mList) {
						LatLng ll = new LatLng(d.getLat(), d.getLng());
						mPts.add(ll);
					}

					initDefaultAddress(mPts.get(0));
				} catch (JSONException e) {
					showToast(e.getLocalizedMessage());
				}


			}

			@Override
			public void onError(String error) {
				showToast(error);
			}
		});

	}

	private void initDefaultAddress(LatLng ll){
		mLat = ll.latitude;
		mLng = ll.longitude;
		mCountry = AppContext.SHOP_NAME;
		mProvince = AppContext.SHOP_NAME;
		mCity = AppContext.SHOP_NAME;
		mDistrict = AppContext.SHOP_NAME;

		Address a = new Address();
		a.setAddr(AppContext.SHOP_NAME);
		a.setName(" ");
		List<Address> lists = new ArrayList<>();
		lists.add(a);


		mListView.setAdapter(new AddrAdapter<Address>(mContext, lists));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
				Intent i = new Intent(MapSelectActivity.this,AddressAddActivity.class);
				i.putExtra(LAT, mLat);
				i.putExtra(LNG,mLng );
				i.putExtra(ADDRESS, AppContext.SHOP_NAME);
				i.putExtra(COUNTRY,mCountry );
				i.putExtra(PROVINCE, mProvince);
				i.putExtra(CITY,mCity );
				i.putExtra(DISTRICT, mDistrict);

				setResult(RESULT_OK, i);
				finish();
			}

		});
	}


//	private String getCity(LatLng latLng) {
//		String latLongString = "";
//		List<android.location.Address> addList = null;
//		Geocoder ge = new Geocoder(this);
//		try {
//			addList = ge.getFromLocation(latLng.latitude, latLng.longitude, 1);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		if (addList != null && addList.size() > 0) {
//			android.location.Address ad = addList.get(0);
//			latLongString = ad.getLocality();
//		}
//		return latLongString;
//	}

	@Override  
	protected void onDestroy() {  
		super.onDestroy();  
		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
		mMapView.onDestroy();  
	}  
	@Override  
	protected void onResume() {  
		super.onResume();  
		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
		mMapView.onResume();  
	}  
	
	@Override
	public void onPause() {  
		super.onPause();  
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
		mMapView.onPause();  
	}  
}
