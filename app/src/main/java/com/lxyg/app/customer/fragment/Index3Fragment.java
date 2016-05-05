package com.lxyg.app.customer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.LoginActivity;
import com.lxyg.app.customer.activity.ProductDetailsActivity;
import com.lxyg.app.customer.activity.ProductListActivity;
import com.lxyg.app.customer.activity.SearchActivity;
import com.lxyg.app.customer.activity.ShopSelectActivity;
import com.lxyg.app.customer.activity.ShoppingCarActivity;
import com.lxyg.app.customer.activity.SignActivity;
import com.lxyg.app.customer.adapter.ActivityGridAdapter;
import com.lxyg.app.customer.adapter.CategoryGridAdapter;
import com.lxyg.app.customer.adapter.IndexAdapter;
import com.lxyg.app.customer.adapter.PageViewAdapter;
import com.lxyg.app.customer.adapter.ProductGridAdapter;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Activitys;
import com.lxyg.app.customer.bean.Car;
import com.lxyg.app.customer.bean.Category;
import com.lxyg.app.customer.bean.CategoryLoc;
import com.lxyg.app.customer.bean.Home;
import com.lxyg.app.customer.bean.HomeProduct;
import com.lxyg.app.customer.bean.Product;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.ImmersionModeUtils;
import com.lxyg.app.customer.utils.SharePreferencesUtil;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.view.NoScrollGridView;

public class Index3Fragment extends BaseFragment {
	@Override
	public int setLayoutId() {
		return R.layout.fragment_index3;
	}


	private UltimateRecyclerView mRecycleView;
	private IndexAdapter mAdapter;

	private Button mBtnArea,mBtnUp;
	private RelativeLayout mViewSearch;
	private TextView mTvCarNum;
	private RelativeLayout mViewCar;
	private Button mImgSign;
	private RelativeLayout mViewLoading;
	@Override
	public void onViewCreated(View view,Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ImmersionModeUtils.setTranslucentStatus(getActivity(), R.color.main_red);
		mTvCarNum = (TextView)view.findViewById(R.id.tv_car);


		if(mAdapter == null){
			mRecycleView = (UltimateRecyclerView)view.findViewById(R.id.ultimate_recycler_view);
//			mRecycleView.setEmptyView(R.layout.view_empty);
			mBtnArea = (Button)view.findViewById(R.id.area_selector);
			mBtnArea.setOnClickListener(this);

			mBtnUp = (Button)view.findViewById(R.id.btn_up);
			mBtnUp.setOnClickListener(this);

			mTvCarNum = (TextView)view.findViewById(R.id.tv_car);
			mViewCar = (RelativeLayout)view.findViewById(R.id.view_car);
			//进入购物车界面
			mViewCar.setOnClickListener(this);

			mViewSearch = (RelativeLayout)view.findViewById(R.id.view_search);
			mViewSearch.setOnClickListener(this);

			mImgSign = (Button)view.findViewById(R.id.sign);
			mImgSign.setOnClickListener(this);
			mViewLoading = (RelativeLayout)view.findViewById(R.id.view_loading);

			postData();
		}

	}


	private String mData;
	//如果是2  则表示需要清除后重新加载数据
	private void postData(){

		JSONObject mJB = new JSONObject();
		try{
			mJB.put("lat", AppContext.Latitude);
			mJB.put("lng", AppContext.Longitude);
			if(TextUtils.isEmpty(AppContext.SHOP_ID)){
				AppContext.SHOP_ID = SharePreferencesUtil.getShopId(getActivity());
			}
			mJB.put("s_uid",AppContext.SHOP_ID);
		}catch(JSONException e){
			return;
		}
		mBaseHttpClient.postData1(INDEXT2, mJB, new AppAjaxCallback.onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				mData = data;
				loadDate(mData);
			}

			@Override
			public void onError(String error) {
				mRecycleView.setRefreshing(false);
				showToast(error);
			}
		});
	}

	private PageViewAdapter mAdapterPager;
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
				mHandler.sendEmptyMessageDelayed(0, 5000);
			}

		};
	};

	private ImageView[] mImgDots;
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

	private List<Category> mListG = new ArrayList<Category>();
	private CategoryGridAdapter mAdapterCategory;
	private ProductGridAdapter mAdapterCommed;

	private String mTitle;
	private Home mHome;
	private List<HomeProduct> mHomeProduct = new ArrayList<>();
	private void loadDate(String data){
		try {
			JSONObject jb = new JSONObject(data);
			AppContext.SERVICE_PHONE = jb.getString("service_phone");
		}catch (JSONException e){

		}


		mHome = JsonUtils.parse(data,Home.class);
		AppContext.SERVICE_PHONE = mHome.getService_phone();

		mTitle = mHome.getName();
		AppContext.SHOP_NAME = mTitle;
		AppContext.PEI_SONG = mHome.getTitle();

		mBtnArea.setText(mTitle);

		AppContext.SHOP_TYPE = mHome.getShop_type();

		if(mAdapter == null){
			mHomeProduct.addAll(mHome.getTypes());
			mListTopImg.addAll(mHome.getShopActivits());

			mAdapter = new IndexAdapter(mHomeProduct,getActivity(),mTvCarNum) {
				@Override
				public int setItemLayoutId() {
					return R.layout.item_index_product;
				}

				@Override
				public void setHeaderView(RecyclerView.ViewHolder holder, int position) {

				}
			};

			mRecycleView.setAdapter(mAdapter);
			mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
			mRecycleView.setHasFixedSize(true);
			mRecycleView.setSaveEnabled(true);
			mRecycleView.setClipToPadding(false);
			mRecycleView.enableLoadmore();
			mRecycleView.setNormalHeader(initViewHeader());

			mRecycleView.disableLoadmore();

			mViewLoading.setVisibility(View.GONE);
			mRecycleView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					postData();
				}
			});


			mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
				@Override
				public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
					super.onScrollStateChanged(recyclerView, newState);
					//当前RecyclerView显示出来的最后一个item的position
					int lastPosition = -1;
					switch (newState){
						case RecyclerView.SCROLL_STATE_IDLE://当屏幕停止滚动时
							RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
							if(layoutManager instanceof GridLayoutManager){
								//拿到当前位置
								lastPosition = ((GridLayoutManager)layoutManager).findLastVisibleItemPosition();
							}else if(layoutManager instanceof LinearLayoutManager){
								lastPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
							}else if(layoutManager instanceof StaggeredGridLayoutManager){
								//因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
								//得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
								int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
								((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
								lastPosition = findMax(lastPositions);
							}
							break;
					}
					if(lastPosition == recyclerView.getLayoutManager().getItemCount()-1){
						showToast("滑动到底部了");
					}

					if(lastPosition >0){
						mBtnUp.setVisibility(View.VISIBLE);
					}else{
						mBtnUp.setVisibility(View.GONE);
					}


				}

				@Override
				public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
					super.onScrolled(recyclerView, dx, dy);
//					//得到当前显示的最后一个item的view
//					View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount()-1);
//					//得到lastChildView的bottom坐标值
//					int lastChildBottom = lastChildView.getBottom();
//					//得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
//					int recyclerBottom =  recyclerView.getBottom()-recyclerView.getPaddingBottom();
//					//通过这个lastChildView得到这个view当前的position值
//					int lastPosition  = recyclerView.getLayoutManager().getPosition(lastChildView);
//
//					//判断lastChildView的bottom值跟recyclerBottom
//					//判断lastPosition是不是最后一个position
//					//如果两个条件都满足则说明是真正的滑动到了底部
//					if(lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount()-1 ){
//						showToast("滑动到底部了");
//					}
				}
			});


		}else{
			mHomeProduct.clear();
			mHomeProduct.addAll(mHome.getTypes());
			mAdapter.notifyDataSetChanged();
			initViewHeader();
		}
	}

	//找到数组中的最大值
	private int findMax(int[] lastPositions) {
		int max = lastPositions[0];
		for (int value : lastPositions) {
			if (value > max) {
				max = value;
			}
		}
		return max;
	}
	private ViewPager mViewPager;
	private ActivityGridAdapter mAdapterActivity;
	private NoScrollGridView mGridViewCategory,mGridViewCommed,mGridViewActivity;
	private TextView mTvProductMyMore;
	private List<Product> rList = new ArrayList<Product>();
	private RelativeLayout mViewActivity;
	private List<Activitys> aList = new ArrayList<>();
	private View initViewHeader(){
		View view = getActivity().getLayoutInflater().inflate(R.layout.item_home_header, null);

		mViewActivity = (RelativeLayout)view.findViewById(R.id.view_activity);
		mViewPager = (ViewPager)view.findViewById(R.id.view_pager);
		mGridViewCategory = (NoScrollGridView)view.findViewById(R.id.gridview1);
		mGridViewCommed = (NoScrollGridView)view.findViewById(R.id.index_gridview);
		mGridViewActivity = (NoScrollGridView)view.findViewById(R.id.gridview_activity);
		mTvProductMyMore = (TextView)view.findViewById(R.id.product_my_more);
		mViewPagerDot = (LinearLayout)view.findViewById(R.id.view_select);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				(getScreenWidth()/17*10));
		mViewActivity.setLayoutParams(params);

		//四分分类的内容
		aList.clear();
		aList.addAll(mHome.getActivitys());
		if(mAdapterActivity == null){
			mAdapterActivity = new ActivityGridAdapter(getActivity(),aList,getScreenWidth());
			mGridViewActivity.setAdapter(mAdapterActivity);
		}else{
			mAdapterActivity.notifyDataSetChanged();
		}

		//顶部滚动试图
		if(mAdapterPager == null){
			mAdapterPager = new PageViewAdapter(getActivity().getSupportFragmentManager(),
						mListTopImg);

			mViewPager.setAdapter(mAdapterPager);
			mViewPager.addOnPageChangeListener(new MyPageChangeListener());

			//试图滚动
			mHandler.sendEmptyMessageDelayed(0, 2000);
			addDot();
			if(mImgDots.length>0)
				mImgDots[0].setImageResource(R.mipmap.ic_dot_select);
		}else{
			mAdapterPager.notifyDataSetChanged();
		}
		//分类
//		mListG.clear();
//		mListG.addAll(mHome.getCategory());
		if(mAdapterCategory == null){
			mListG.addAll(CategoryLoc.getCategory());
			mAdapterCategory = new CategoryGridAdapter(getActivity(), mListG);
			mGridViewCategory.setAdapter(mAdapterCategory);
		}else{
			mAdapterCategory.notifyDataSetChanged();
		}

		//推荐产品
		rList.clear();
		final List<Product> mListProductRecomm = mHome.getRecommGoods();
		if(mListProductRecomm != null){
			rList.addAll(mListProductRecomm);

			if(mAdapterCommed == null){
				mAdapterCommed = new ProductGridAdapter(getActivity(), rList,mTvCarNum);
				mGridViewCommed.setAdapter(mAdapterCommed);

				mGridViewCommed.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {
						startActivity(new Intent(getActivity(), ProductDetailsActivity.class).putExtra(INTENT_ID, rList.get(position).getProductId()));
					}
				});




				//本店推荐更多按钮
				mTvProductMyMore.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
//						System.out.println("fuck---"+rList.size());
//						if(rList.size()==0){
//							showToast("fuck");
//							return;
//						}
//						startActivity(new Intent(getActivity(), ProductListActivity.class).putExtra(INTENT_ID, -1));
					}
				});

			}else{
				mAdapterCommed.notifyDataSetChanged();
			}
		}
		//本店推荐更多按钮
		mTvProductMyMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(rList.size()==0){
					return;
				}
				startActivity(new Intent(getActivity(), ProductListActivity.class).putExtra(INTENT_ID, -1));

//				startActivity(new Intent(getActivity(), ProductListActivity.class).putExtra(INTENT_ID, -1));
			}
		});

		return view;
	}

	private int mScreenWidth = 0;
	private int getScreenWidth(){
		if(mScreenWidth == 0) {
			DisplayMetrics metric = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
			mScreenWidth = metric.widthPixels;
		}
		return mScreenWidth;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.right_icon:

				showProgressDialog("");
				startActivityForResult((new Intent(getActivity(),CaptureActivity.class)), 2001);
				cancelProgressDialog();
				break;
			case R.id.area_selector:

				startActivityForResult(new Intent(getActivity(), ShopSelectActivity.class).putExtra(INTENT_ID, 0), 3001);
				break;
			case R.id.view_search:
				startActivity(new Intent(getActivity(), SearchActivity.class));
				break;
			case R.id.view_car:
				startActivity(new Intent(getActivity(), ShoppingCarActivity.class));
				break;
			case R.id.sign:
				if(!AppContext.IS_LOGIN)
					startActivityForResult(new Intent(getActivity(), LoginActivity.class).putExtra(INTENT_ID, 0), LOGIN_CODE1);
				else
					startActivity(new Intent(getActivity(), SignActivity.class));
				break;

			case R.id.btn_up:
				mRecycleView.scrollVerticallyTo(0);
				mBtnUp.setVisibility(View.GONE);
				break;

		}
	}

	public class MyPageChangeListener implements ViewPager.OnPageChangeListener {

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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
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
	public void onResume() {
		super.onResume();
		//刷新购物车
		refreshShoppingCar();
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
