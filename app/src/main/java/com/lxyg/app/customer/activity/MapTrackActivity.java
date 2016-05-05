package com.lxyg.app.customer.activity;

import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Track;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.JsonUtils;

public class MapTrackActivity extends BaseActivity{

	private MapView mMapView = null;
	private BaiduMap mBaiduMap;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_track);
		setBack();
		setTitleText("订单追踪");

		mMapView = (MapView) findViewById(R.id.bmapView);
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

		loadData();
	}

	private List<Track> mTracks;
	private void loadData(){
		JSONObject jb = new JSONObject();
		mBaseHttpClient.postData1(TRACK, jb, new AppAjaxCallback.onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				mTracks = new ArrayList<Track>();
				mTracks = JsonUtils.parseList(data, Track.class);

				if(mTracks.size()==0){
					showToast("暂无信息");
					finish();
					return;
				}

				addTrack();
			}

			@Override
			public void onError(String error) {

			}
		});
	}

	private void addTrack(){
		//构造纹理资源
		BitmapDescriptor custom1 = BitmapDescriptorFactory
						.fromResource(R.mipmap.ic_map_line);
		// 定义点
		LatLng pt1 = new LatLng(mTracks.get(0).getPos().get(0).getLat(), mTracks.get(0).getPos().get(0).getLng());

		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(pt1);
		mBaiduMap.animateMapStatus(u);


		//构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
		    .fromResource(R.mipmap.icon_track_navi_end);
		//构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions()
		    .position(pt1)
		    .icon(bitmap);
		//在地图上添加Marker，并显示
		mBaiduMap.addOverlay(option);



		//点数小于2 或者大于10000 程序会报错
		if(mTracks.size()<2||mTracks.size()>10000)
			return;

		for (int i = 0; i < mTracks.size(); i++) {
			List<LatLng> points = new ArrayList<LatLng>();
			List<Track.pos> datas = mTracks.get(i).getPos();
			for(int j=0;j<datas.size();j++){
				LatLng pt = new LatLng(datas.get(j).getLat(), datas.get(j).getLng());
				points.add(pt);
			}

			OverlayOptions lines = new PolylineOptions().width(5).color(0xAAFF0000).points(points).customTexture(custom1);
			//添加到地图
			mBaiduMap.addOverlay(lines);

//			LatLng pt = new LatLng(mTracks.get(0).getPos().get(0).getLat(), mTracks.get(0).getPos().get(0).getLng());
		}



	}


	MyLocationData locData;
	private final boolean IS_FIRST_LOC = true;
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
				//定位位置
//				LatLng ll = new LatLng(location.getLatitude(),
//						location.getLongitude());
//				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//				mBaiduMap.animateMapStatus(u);

				mLocClient.stop();
			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
		finish();
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
