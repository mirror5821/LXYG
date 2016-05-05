package com.lxyg.app.customer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.ProductDetailsActivity;
import com.lxyg.app.customer.activity.ProductListActivity;
import com.lxyg.app.customer.adapter.CategoryGridAdapter;
import com.lxyg.app.customer.adapter.PageViewAdapter;
import com.lxyg.app.customer.adapter.ProductGridAdapter;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Activitys;
import com.lxyg.app.customer.bean.Category;
import com.lxyg.app.customer.bean.Product;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.view.NoScrollGridView;

public class CopyIndexFragment extends BaseFragment{
	@Override
	public int setLayoutId() {
		//e1a4a71e1c2946b48kakad9da251b371
		return R.layout.fragment_index;
	}

	//上边4个  白酒   进口干红  啤酒饮料   代送香烟     下边4个  零食   水果  下酒小菜  订单跟踪

	private ViewPager mViewPager;
	private NoScrollGridView mGridViewCategory,mGridViewCommed;
	private LinearLayout mViewDynamic;
	private LayoutInflater mInflater;

	private boolean isFirstLoad = true;

	private ImageView mImgScan;
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mViewPager = (ViewPager)view.findViewById(R.id.view_pager);
		mGridViewCategory = (NoScrollGridView)view.findViewById(R.id.gridview1);
		mGridViewCommed = (NoScrollGridView)view.findViewById(R.id.index_gridview);

		mViewDynamic = (LinearLayout)view.findViewById(R.id.view_dynamic);
		mInflater = LayoutInflater.from(getActivity());
		mViewPagerDot = (LinearLayout)view.findViewById(R.id.view_select);

		mImgScan = (ImageView)view.findViewById(R.id.right_icon);
		mImgScan.setImageResource(R.mipmap.ic_scan_new);
		mImgScan.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.right_icon:
			if(AppContext.IS_LOGIN){
				showProgressDialog("");
				startActivityForResult((new Intent(getActivity(),CaptureActivity.class)), 2001);
				cancelProgressDialog();
			}else{

			}
			break;

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode ==Activity.RESULT_OK){
			switch (requestCode) {
			case LOGIN_CODE1:
				AppContext.IS_LOGIN = true;
				break;
			case 2001:
				Uri uriData = data.getData();
				changeShop(uriData.toString());
				break;

			}
		}
	}

	//更换店铺
	private void changeShop(final String code){
		showProgressDialog("正在修改店铺信息");
		JSONObject jb = new JSONObject();
		try {
			jb.put("ewm_code", code);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		mBaseHttpClient.postData1(SCAN, jb, new AppAjaxCallback.onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				cancelProgressDialog();
//				AppContext.USER_ID = code;
				showToast(msg);

				postData();
			}

			@Override
			public void onError(String error) {
				cancelProgressDialog();
				showToast(error);
			}
		});
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//因为是动态加载数据  所以在首次加载数据  以后使用则不需要加载数据
		if(isFirstLoad){
			postData();
			isFirstLoad = false;
		}
//		if(savedInstanceState != null){
//			mData = savedInstanceState.getString(INTENT_ID);
//			loadDate(mData);
//		}else{
//			postData();
//		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(!TextUtils.isEmpty(mData)){
			outState.putString(INTENT_ID, mData);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		//为了购物车数据统一，就使用该方法刷新下界面
		if(!TextUtils.isEmpty(mData)){
//			mViewDynamic.removeAllViews();
			loadDate(mData);
		}
	}

	private String mData;
	private void postData(){
		JSONObject jb = new JSONObject();
		try{
			//			'lat':34.771849,lng:113.778239
			jb.put("lat", AppContext.Latitude);
			jb.put("lng", AppContext.Longitude);
		}catch(JSONException e){

		}

		mBaseHttpClient.postData1(INDEX, jb, new AppAjaxCallback.onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				mData = data;
				loadDate(mData);

			}

			@Override
			public void onError(String error) {
				AppContext.IS_LOGIN = false;
				AppContext.USER_ID = null;

				postData();
			}
		});
	}

	private List<Activitys> mListTopImg;
	private final boolean isRunning = true;
	private int mPage = 0;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			mPage = mViewPager.getCurrentItem();
			if(mPage == mListTopImg.size()-1){
				mPage = 0;
			}else{
				mPage = mPage+1;
			}
			mViewPager.setCurrentItem(mPage);
			selectDot(mPage);

			if (isRunning) {
				// 在发一个handler延时
				mHandler.sendEmptyMessageDelayed(0, 2000);
			}

		};
	};
	private ImageView [] mImgDots;
	private void selectDot(int page){
		for(int i=0;i<mListTopImg.size();i++){
			if(i ==page){
				mImgDots[i].setImageResource(R.mipmap.ic_dot_select);
			}else{
				mImgDots[i].setImageResource(R.mipmap.ic_dot_default);
			}
		}

	}

	private LinearLayout mViewPagerDot;
	private void addDot(){
		int l = mListTopImg.size();
		mImgDots = new ImageView[l];

		for(int i=0; i<l; i++)
		{
			View v = getActivity().getLayoutInflater().inflate(R.layout.view_dot, null);
			mImgDots[i] = (ImageView)v.findViewById(R.id.dot_img);
			mViewPagerDot.addView(v, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		}

	}

	//typeId
	private String mTitle;
	private PageViewAdapter mAdapter;
	private List<Category> mListG;

	private List<Product> cList ;
	private JSONArray ja;
	@SuppressWarnings("deprecation")
	private void loadDate(String data){
		try {
			JSONObject jb2 = new JSONObject(data);

			if(mAdapter == null){
				mListTopImg = new ArrayList<Activitys>();
				mListTopImg.clear();
				mListTopImg = JsonUtils.parseList(jb2.getString("shopActivits"), Activitys.class);
				addDot();
				if(mImgDots.length>0)
					mImgDots[0].setImageResource(R.mipmap.ic_dot_select);

				mAdapter = new PageViewAdapter(getActivity().getSupportFragmentManager(),
						mListTopImg);
				mViewPager.setAdapter(mAdapter);
				mHandler.sendEmptyMessageDelayed(0, 2000);
				mViewPager.setOnPageChangeListener(new MyPageChangeListener());
			}

			if(cList == null){
				cList = JsonUtils.parseList(jb2.getString("recommGoods"), Product.class);
				mGridViewCommed.setAdapter(new ProductGridAdapter(getActivity(), cList));

				mGridViewCommed.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {
						startActivity(new Intent(getActivity(), ProductDetailsActivity.class).putExtra(INTENT_ID, cList.get(position).getProductId()));
					}
				});
			}

			mTitle = jb2.getString("name");
			AppContext.SHOP_NAME = jb2.getString("name");
			setTitleText(mTitle);

			AppContext.SHOP_ID = jb2.getString("uuid");

			if(mListG == null){
				mListG = new ArrayList<Category>();
				mListG = JsonUtils.parseList(jb2.getString("category"), Category.class);
				mGridViewCategory.setAdapter(new CategoryGridAdapter(getActivity(), mListG));
			}


			if(ja == null){
				ja = new JSONArray(jb2.getString("types"));

				JSONArray ja = new JSONArray(jb2.getString("types"));

				for(int i =0;i<ja.length();i++){
					JSONObject jbType = (JSONObject) ja.get(i);
					View view = mInflater.inflate(R.layout.item_index_product, null);
					LinearLayout more = (LinearLayout)view.findViewById(R.id.view_more);
					TextView tv = (TextView)view.findViewById(R.id.p_name);

					JSONObject  t = new JSONObject(jbType.getString("type"));
					final String pName = t.getString("name");
					final int pId = t.getInt("typeId");


					//分类颜色
					View viewI = view.findViewById(R.id.view_item);

					switch (pId) {
						case 1:
							viewI.setBackgroundResource(R.drawable.g_d1);
							break;
						case 2:
							viewI.setBackgroundResource(R.drawable.g_d2);
							break;
						case 3:
							viewI.setBackgroundResource(R.drawable.g_d3);
							break;
						case 4:
							viewI.setBackgroundResource(R.drawable.g_d4);
							break;
						case 5:
							viewI.setBackgroundResource(R.drawable.g_d5);
							break;
						case 6:
							viewI.setBackgroundResource(R.drawable.g_d6);
							break;
						case 7:
							viewI.setBackgroundResource(R.drawable.g_d7);
							break;
						case 8:
							viewI.setBackgroundResource(R.drawable.g_d8);
							break;
						default:
							viewI.setBackgroundResource(R.drawable.g_d1);
							break;
					}

					tv.setText(pName);
					more.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							startActivity(new Intent(getActivity(),ProductListActivity.class).putExtra(INTENT_ID, pId));
						}
					});
					final List<Product> pList = JsonUtils.parseList(jbType.getString("products"), Product.class);
					NoScrollGridView gView = (NoScrollGridView)view.findViewById(R.id.p_gridview);
					gView.setAdapter(new ProductGridAdapter(getActivity(), pList));

					gView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
												int position, long id) {
							startActivity(new Intent(getActivity(), ProductDetailsActivity.class).putExtra(INTENT_ID, pList.get(position).getProductId()));
						}
					});
					mViewDynamic.addView(view);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			showToast(e.getMessage());
		}
	}

	public class MyPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int positon) {
			mPage = positon;
		}
	}
}
