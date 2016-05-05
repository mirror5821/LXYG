package com.lxyg.app.customer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.CategoryNewActivity;
import com.lxyg.app.customer.activity.LoginActivity;
import com.lxyg.app.customer.activity.ProductDetailsActivity;
import com.lxyg.app.customer.activity.ProductListActivity;
import com.lxyg.app.customer.activity.SearchActivity;
import com.lxyg.app.customer.activity.ShopSelectActivity;
import com.lxyg.app.customer.activity.ShoppingCarActivity;
import com.lxyg.app.customer.activity.ShoppingScanActivity;
import com.lxyg.app.customer.activity.SignActivity;
import com.lxyg.app.customer.adapter.CategoryGridAdapter;
import com.lxyg.app.customer.adapter.PageViewAdapter;
import com.lxyg.app.customer.adapter.ProductGridAdapter;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Activitys;
import com.lxyg.app.customer.bean.Car;
import com.lxyg.app.customer.bean.Category;
import com.lxyg.app.customer.bean.CategoryLoc;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.bean.Product;
import com.lxyg.app.customer.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;
import com.lxyg.app.customer.utils.ImmersionModeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.view.NoScrollGridView;

public class Index2Fragment extends BaseFragment {
	@Override
	public int setLayoutId() {
		return R.layout.fragment_index;
	}


	private ViewPager mViewPager;
	private NoScrollGridView mGridViewCategory,mGridViewCommed;
	private LinearLayout mViewDynamic;
	private LayoutInflater mInflater;
	private TextView mTvProductMyMore;
	private ImageView mImgCar;
	private RelativeLayout mViewCar;
	private TextView mTvCarNum;
	private Button mImgSign;
	private EditText mEtScanGun;


	private Button mBtnArea;
	private RelativeLayout mViewSearch;

	private Animation mAnimation;
	private PullToRefreshScrollView mPullView;

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ImmersionModeUtils.setTranslucentStatus(getActivity(), R.color.main_red);

//		mEtScanGun = (EditText)view.findViewById(R.id.et_sm);
//		mEtScanGun.addTextChangedListener(new TextWatcher() {
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				startActivity(new Intent(getActivity(), ShoppingScanActivity.class).
//						putExtra(INTENT_ID, s.toString()));
//			}
//		});


		mImgSign = (Button)view.findViewById(R.id.sign);
		mImgCar = (ImageView)view.findViewById(R.id.img_car);
		mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_car);
		mViewPager = (ViewPager)view.findViewById(R.id.view_pager);
		mGridViewCategory = (NoScrollGridView)view.findViewById(R.id.gridview1);
		mGridViewCommed = (NoScrollGridView)view.findViewById(R.id.index_gridview);

		mTvProductMyMore = (TextView)view.findViewById(R.id.product_my_more);

		mViewDynamic = (LinearLayout)view.findViewById(R.id.view_dynamic);
		mInflater = LayoutInflater.from(getActivity());
		mViewPagerDot = (LinearLayout)view.findViewById(R.id.view_select);
		mViewSearch = (RelativeLayout)view.findViewById(R.id.view_search);
		mViewSearch.setOnClickListener(this);

		mBtnArea = (Button)view.findViewById(R.id.area_selector);
		mBtnArea.setOnClickListener(this);
		mImgSign.setOnClickListener(this);
//		mImgScan.setImageResource(R.mipmap.ic_scan_new2);

		mPullView = (PullToRefreshScrollView)view.findViewById(R.id.p_scrollview);

		mPullView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				postData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

			}
		});

		mTvCarNum = (TextView)view.findViewById(R.id.tv_car);
		mViewCar = (RelativeLayout)view.findViewById(R.id.view_car);
		//进入购物车界面
		mViewCar.setOnClickListener(this);

		//强制隐藏软键盘
//		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

			case R.id.sign:
				if(!AppContext.IS_LOGIN)
					startActivityForResult(new Intent(getActivity(), LoginActivity.class).putExtra(INTENT_ID, 0), LOGIN_CODE1);
				else
					startActivity(new Intent(getActivity(), SignActivity.class));
				break;
			case R.id.right_icon:

				showProgressDialog("");
				startActivityForResult((new Intent(getActivity(),CaptureActivity.class)), 2001);
				cancelProgressDialog();
				break;
			case R.id.area_selector:
//				startActivityForResult(new Intent(getActivity(), AreaSelectListActivity.class).putExtra(INTENT_ID, 0), 3001);

				startActivityForResult(new Intent(getActivity(), ShopSelectActivity.class).putExtra(INTENT_ID, 0), 3001);
//				startActivity(new Intent(getActivity(), AreaSelectListActivity.class).putExtra(INTENT_ID, 0));
				break;
			case R.id.view_search:
				startActivity(new Intent(getActivity(), SearchActivity.class));
				break;
			case R.id.view_car:
				startActivity(new Intent(getActivity(), ShoppingCarActivity.class));
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
					startActivity(new Intent(getActivity(), SignActivity.class));
					break;
				case 2001:
					Uri uriData = data.getData();
//					uriData.toString()  // 二维码的字符串
					break;
				case 3001:
					//为了购物车数据统一，就使用该方法刷新下界面
					if(AppContext.SHOP_CHANGE == 1){
						AppContext.SHOP_CHANGE = 0;
						//重新加载店铺
						postData();
					}
					break;

			}
		}
	}



	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(!TextUtils.isEmpty(mData)){
			outState.putString(INTENT_ID, mData);
		}
	}

	private boolean isFirstResume = true;
	@Override
	public void onResume() {
		super.onResume();
		if(isFirstResume){
			isFirstResume = false;
			postData();
		}

		refreshShoppingCar();
	}

	private String mData;
	//如果是2  则表示需要清除后重新加载数据
	private void postData(){

		JSONObject mJB = new JSONObject();
		try{
			mJB.put("lat", AppContext.Latitude);
			mJB.put("lng", AppContext.Longitude);
			mJB.put("s_uid",AppContext.SHOP_ID);
		}catch(JSONException e){
			return;
		}
		mBaseHttpClient.postData1(INDEXT2, mJB, new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				mData = data;
				loadDate(mData);
			}

			@Override
			public void onError(String error) {
				showToast(error);
			}
		});
	}

	private List<Activitys> mListTopImg  = new ArrayList<Activitys>();;
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
		mViewPagerDot.removeAllViews();
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
	private List<Category> mListG = new ArrayList<Category>();;

	private boolean isPagerRole = false;
	private List<Product> cList ;
	private JSONArray ja;
	private CategoryGridAdapter mAdapterCategory;
	private ProductGridAdapter mAdapterCommed;
	private List<Product> rList = new ArrayList<Product>();
	@SuppressWarnings("deprecation")
	private void loadDate(String data){
		try {

			JSONObject jb2 = new JSONObject(data);

			mTitle = jb2.getString("name");
			AppContext.SHOP_NAME = mTitle;
			mBtnArea.setText(mTitle);

			AppContext.SHOP_TYPE = jb2.getInt("shop_type");

			mListTopImg.clear();
			mListTopImg.addAll(JsonUtils.parseList(jb2.getString("shopActivits"), Activitys.class));
			if(mAdapter == null){
				mAdapter = new PageViewAdapter(getActivity().getSupportFragmentManager(),
						mListTopImg);
				mViewPager.setAdapter(mAdapter);
				mViewPager.setOnPageChangeListener(new MyPageChangeListener());

				//试图滚动
				mHandler.sendEmptyMessageDelayed(0, 2000);
				isPagerRole = true;
			}else{
				mAdapter.notifyDataSetChanged();
			}

			//添加滚动视图下的数量按钮
			addDot();
			if(mImgDots.length>0)
				mImgDots[0].setImageResource(R.mipmap.ic_dot_select);

			//8个分类
			mListG.clear();
			mListG.addAll(CategoryLoc.getCategory());
//			mListG.addAll(JsonUtils.parseList(jb2.getString("category"), Category.class));
			if(mAdapterCategory == null){

				mAdapterCategory = new CategoryGridAdapter(getActivity(), mListG);
				mGridViewCategory.setAdapter(mAdapterCategory);
			}else{
				mAdapterCategory.notifyDataSetChanged();
			}

			//下面的视图
			rList.clear();
			List<Product> mListProductRecomm = JsonUtils.parseList(jb2.getString("recommGoods"), Product.class);
			if(mListProductRecomm != null){
				rList.addAll(JsonUtils.parseList(jb2.getString("recommGoods"), Product.class));

				if(mAdapterCommed == null){
					mAdapterCommed = new ProductGridAdapter(getActivity(), rList,mTvCarNum);
					mGridViewCommed.setAdapter(mAdapterCommed);

					mGridViewCommed.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
												int position, long id) {
							startActivity(new Intent(getActivity(), ProductDetailsActivity.class).putExtra(INTENT_ID, rList.get(position).getProductId()));
						}
					});

					//本店推荐更多按钮
					mTvProductMyMore.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							startActivity(new Intent(getActivity(), ProductListActivity.class).putExtra(INTENT_ID, -1));
						}
					});

				}else{
					mAdapterCommed.notifyDataSetChanged();
				}
			}

			mViewDynamic.removeAllViews();
			ja = new JSONArray(jb2.getString("types"));
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


				final List<Product> pList = JsonUtils.parseList(jbType.getString("products"), Product.class);

				tv.setText(pName);

				NoScrollGridView gView = (NoScrollGridView)view.findViewById(R.id.p_gridview);
				gView.setAdapter(new ProductGridAdapter(getActivity(), pList,mTvCarNum));

				gView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {
						startActivity(new Intent(getActivity(), ProductDetailsActivity.class).putExtra(INTENT_ID, pList.get(position).getProductId()));
					}
				});

				more.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(getActivity(), CategoryNewActivity.class).putExtra(Constants.INTENT_ID,
								pId));

//						startActivity(new Intent(getActivity(), ProductListActivity.class).putExtra(INTENT_ID, pId).putExtra("typeId", pId));
					}
				});
				mViewDynamic.addView(view);
			}


		} catch (JSONException e) {
			e.printStackTrace();
			showToast(e.getMessage());

			mPullView.onRefreshComplete();
		}

		mPullView.onRefreshComplete();
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


	public void refreshShoppingCar(){
		List<Car> userList = AppContext.mDb.findAll(Car.class);//查询所有的用户
		if(userList != null){
			int i =userList.size();
			if(i == 0){
				mTvCarNum.setVisibility(View.GONE);
			}else {
				mTvCarNum.setVisibility(View.VISIBLE);
				mTvCarNum.setText(userList.size() + "");
			}
		}else{
			mTvCarNum.setVisibility(View.GONE);
		}
	}
}
