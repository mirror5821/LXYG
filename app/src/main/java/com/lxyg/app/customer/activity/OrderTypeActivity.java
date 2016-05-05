package com.lxyg.app.customer.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.adapter.OrderPagerAdapter;
import com.lxyg.app.customer.bean.OrderNum;
import com.lxyg.app.customer.utils.AppAjaxCallback;

import org.json.JSONObject;

import dev.mirror.library.utils.DpUtil;
import dev.mirror.library.utils.JsonUtils;

public class OrderTypeActivity extends BaseActivity {
	private ViewPager mViewPager;
	private HorizontalScrollView mHView;
	private RadioGroup mRG;

	private String mOrderStrs [] = {"待付款","待发货","待收货","交易完成","拒收/退款"};
	private int [] mOrderStatus = {ORDER_STATUS1,ORDER_STATUS2,ORDER_STATUS3,ORDER_STATUS4,ORDER_STATUS5};
	private OrderPagerAdapter mAdapter;

	private int mTypeFrom = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		setBack();
		setTitleText("订单中心");

		mTypeFrom = getIntent().getExtras().getInt(INTENT_ID)-1;

		mHView = (HorizontalScrollView)findViewById(R.id.s_view);
		mRG = (RadioGroup)findViewById(R.id.view_select);
		mViewPager = (ViewPager)findViewById(R.id.view_pager);

		int mOrderStrLength = mOrderStrs.length;

		final RadioButton[] rbs = new RadioButton[mOrderStrLength];

		for(int i=0; i<mOrderStrLength; i++){
			View v = getLayoutInflater().inflate(R.layout.view_tab_rb, null);

			RadioButton tempButton =(RadioButton)v.findViewById(R.id.rb1);
			tempButton.setText(mOrderStrs[i]);
			tempButton.setId(i+30000);
			rbs[i] = tempButton;


			mRG.addView(tempButton, DpUtil.dip2px(getActivity(), 100),  LinearLayout.LayoutParams.MATCH_PARENT);
		}

		if(mAdapter == null){
			mAdapter = new OrderPagerAdapter(getSupportFragmentManager(),mOrderStatus);
			mViewPager.setAdapter(mAdapter);
		}


		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				rbs[position].setChecked(true);

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		mRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
					case 30000:
						mViewPager.setCurrentItem(0);
						break;
					case 30001:
						mViewPager.setCurrentItem(1);
						break;
					case 30002:
						mViewPager.setCurrentItem(2);
						break;
					case 30003:
						mViewPager.setCurrentItem(3);
						break;
					case 30004:
						mViewPager.setCurrentItem(4);
						break;
				}
			}
		});

		rbs[mTypeFrom].setChecked(true);
		mViewPager.setCurrentItem(mTypeFrom);

	}



	private OrderNum mOrderNum;
	private void loadOrderNum(){
		//先网络请求默认地址
		JSONObject jb = new JSONObject();
		mBaseHttpClient.postData1(ORDER_NUM, jb, new AppAjaxCallback.onResultListener() {
			@Override
			public void onResult(String data, String msg) {
				mOrderNum = JsonUtils.parse(data, OrderNum.class);

				int n1 = mOrderNum.getDfk();
				int n2 = mOrderNum.getDfh();
				int n3 = mOrderNum.getDsh();
				int n4 = mOrderNum.getYwc();


			}

			@Override
			public void onError(String error) {

			}
		});

	}


}
