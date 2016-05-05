package com.lxyg.app.customer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.AddressAddActivity;
import com.lxyg.app.customer.activity.AddressListActivity;
import com.lxyg.app.customer.activity.CashActivity;
import com.lxyg.app.customer.activity.MessageListActivity;
import com.lxyg.app.customer.activity.MyForumListActivity;
import com.lxyg.app.customer.activity.OrderTypeActivity;
import com.lxyg.app.customer.activity.SetActivity;
import com.lxyg.app.customer.activity.WechatAndPhoneLoginActivity;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Address;
import com.lxyg.app.customer.bean.Cash;
import com.lxyg.app.customer.bean.OrderNum;
import com.lxyg.app.customer.bean.Sign;
import com.lxyg.app.customer.bean.User;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppAjaxCallback.onRecevieDataListener;
import com.lxyg.app.customer.utils.ImmersionModeUtils;
import com.lxyg.app.customer.utils.SharePreferencesUtil;
import com.lxyg.app.customer.view.UiHelpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.view.CircleImageView;

public class MyNewFragment extends BaseFragment{
	private TextView mTvOrder1,mTvOrder2,mTvOrder3,mTvOrder4,mTvOrderNum,mTvCashNum,mTvJifenNum;
	private TextView mTvShopName;
	private LinearLayout mViewAddress,mViewLogin,mViewOrder,mViewCash,mViewForum,mViewMiGuan,mViewContact,mViewGonghuo,mViewMessage;
	private CircleImageView mImgUserHeader;
	private LinearLayout mViewSet,mViewKf;

	private TextView mTvNum1,mTvNum2,mTvNum3,mTvNum4,mTvMiGuan;
	@Override
	public int setLayoutId() {
		return R.layout.fragment_my_new;
	}

	private User mUser;
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ImmersionModeUtils.setTranslucentStatus(getActivity(), R.color.user_nav);

		mTvShopName = (TextView)view.findViewById(R.id.shop_name);
		mViewLogin = (LinearLayout)view.findViewById(R.id.view_login);


		mTvNum1 = (TextView)view.findViewById(R.id.n1);
		mTvNum2 = (TextView)view.findViewById(R.id.n2);
		mTvNum3 = (TextView)view.findViewById(R.id.n3);
		mTvNum4 = (TextView)view.findViewById(R.id.n4);
		mTvMiGuan = (TextView)view.findViewById(R.id.tv_miguan_num);
		mTvOrderNum = (TextView)view.findViewById(R.id.tv_order_num);
		mTvCashNum = (TextView)view.findViewById(R.id.tv_case_num);
		mTvJifenNum = (TextView)view.findViewById(R.id.tv_jf_num);

		mImgUserHeader = (CircleImageView)view.findViewById(R.id.c_img);
		mTvOrder1 = (TextView)view.findViewById(R.id.tv_order1);
		mTvOrder2 = (TextView)view.findViewById(R.id.tv_order2);
		mTvOrder3 = (TextView)view.findViewById(R.id.tv_order3);
		mTvOrder4 = (TextView)view.findViewById(R.id.tv_order4);
		mViewAddress = (LinearLayout)view.findViewById(R.id.view_address);
		mViewKf = (LinearLayout)view.findViewById(R.id.view_kf);
		mViewSet = (LinearLayout)view.findViewById(R.id.view_set);
		mViewOrder = (LinearLayout)view.findViewById(R.id.view_order);
		mViewCash = (LinearLayout)view.findViewById(R.id.view_cash);
		mViewForum = (LinearLayout)view.findViewById(R.id.view_forum);
		mViewMiGuan = (LinearLayout)view.findViewById(R.id.view_miguan);
		mViewContact = (LinearLayout)view.findViewById(R.id.view_contact);
		mViewGonghuo = (LinearLayout)view.findViewById(R.id.view_gh);
		mViewMessage = (LinearLayout)view.findViewById(R.id.view_msg);

		mViewContact.setOnClickListener(this);
		mViewGonghuo.setOnClickListener(this);

		mViewForum.setOnClickListener(this);

		mViewKf.setOnClickListener(this);
		mViewMessage.setOnClickListener(this);
		initView();


	}



	@Override
	public void onResume() {
		super.onResume();
		initView();
	}

	private void initView(){
		mTvShopName.setText("未登录");
		mImgUserHeader.setImageResource(R.mipmap.ic_default_zfx_w);
		mViewLogin.setOnClickListener(this);

		mTvOrder1.setOnClickListener(this);
		mTvOrder2.setOnClickListener(this);
		mTvOrder3.setOnClickListener(this);
		mTvOrder4.setOnClickListener(this);
		mViewAddress.setOnClickListener(this);
		mViewSet.setOnClickListener(this);
		mViewOrder.setOnClickListener(this);
		mViewCash.setOnClickListener(this);

		mTvNum1.setVisibility(View.GONE);
		mTvNum2.setVisibility(View.GONE);
		mTvNum3.setVisibility(View.GONE);
		mTvOrderNum.setText("");
		mTvCashNum.setText("");


		if(AppContext.IS_LOGIN) {
			mUser = SharePreferencesUtil.getUserInfo(getActivity());
			mTvShopName.setText(mUser.getName());
			AppContext.displayImage(mImgUserHeader, mUser.getHead_img());

			loadOrderNum();
			loadCaseData();
			loadJifen();
			loadSign();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode ==Activity.RESULT_OK){
			switch (requestCode) {
			case LOGIN_CODE1:

				initView();
				break;
			}
		}
	}


	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.view_msg:
				startActivity(new Intent(getActivity(), MessageListActivity.class));
				break;
			case R.id.view_login:
				if(AppContext.IS_LOGIN){
					return;
				}
				startActivityForResult(new Intent(getActivity(),WechatAndPhoneLoginActivity.class).putExtra(INTENT_ID, 0),LOGIN_CODE1);
				break;
			case R.id.tv_order1:
				if(!AppContext.IS_LOGIN){
					showToast("请先登录!");
					return;
				}
				startActivity(new Intent(getActivity(),OrderTypeActivity.class).putExtra(INTENT_ID, ORDER_STATUS1));
				break;
			case R.id.tv_order2:
				if(!AppContext.IS_LOGIN){
					showToast("请先登录!");
					return;
				}
				startActivity(new Intent(getActivity(),OrderTypeActivity.class).putExtra(INTENT_ID, ORDER_STATUS2));
				break;
			case R.id.tv_order3:
				if(!AppContext.IS_LOGIN){
					showToast("请先登录");
					return;
				}
				startActivity(new Intent(getActivity(),OrderTypeActivity.class).putExtra(INTENT_ID, ORDER_STATUS3));
				break;
			case R.id.tv_order4:
//				if(!AppContext.IS_LOGIN){
//					showToast("请先登录!");
//					return;
//				}

				startActivity(new Intent(getActivity(), MessageListActivity.class));
//				showToast("此功能正在建设中!");
	//			startActivity(new Intent(getActivity(),MapTrackActivity.class).putExtra(INTENT_ID, ORDER_STATUS1));
				break;

			case R.id.view_address:
				if(!AppContext.IS_LOGIN){
					showToast("请先登录!");
					return;
				}
				showProgressDialog("正在请求位置信息...");
				loadAddressData();
				break;
			case R.id.view_set:
	//			if(!AppContext.IS_LOGIN){
	//				showToast("请先登录!");
	//				return;
	//			}
				startActivity(new Intent(getActivity(),SetActivity.class));
				break;
			case R.id.view_kf:
				UiHelpers.makePhoneCallByView(getActivity(), getResources().getString(R.string.kf_phone_num));
				break;
			case R.id.view_order:
				if(!AppContext.IS_LOGIN){
					showToast("请先登录!");
					return;
				}
				startActivity(new Intent(getActivity(),OrderTypeActivity.class).putExtra(INTENT_ID,ORDER_STATUS_ALL));
				break;
			case R.id.view_cash:
				//请完善后开启
				if(!AppContext.IS_LOGIN){
					showToast("请先登录!");
					return;
				}
				startActivity(new Intent(getActivity(),CashActivity.class));
				break;
			case R.id.view_forum:
				//请完善后开启
				if(!AppContext.IS_LOGIN){
					showToast("请先登录!");
					return;
				}
				startActivity(new Intent(getActivity(),MyForumListActivity.class));
				break;
			case R.id.view_contact:
				UiHelpers.makePhoneCallByView(getActivity(), getString(R.string.kf_phone_num));
				break;
			case R.id.view_gh:
				UiHelpers.makePhoneCallByView(getActivity(), getString(R.string.gonghuo_phone));
				break;
		}
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

				if (n1 > 0) {
					mTvNum1.setText(n1 + "");
					mTvNum1.setVisibility(View.VISIBLE);
				}
				if (n2 > 0) {
					mTvNum2.setText(n2 + "");
					mTvNum2.setVisibility(View.VISIBLE);
				}
				if (n3 > 0) {
					mTvNum3.setText(n3 + "");
					mTvNum3.setVisibility(View.VISIBLE);
				}

				//已完成  而非订单完成 故省去
//				if (n4 > 0) {
//					mTvNum4.setText(n4 + "");
//					mTvNum4.setVisibility(View.VISIBLE);
//				}
				int totalNum = n1 + n2 + n3 + n4;
				if (totalNum > 0) {
					mTvOrderNum.setText(totalNum + "");
				}

			}

			@Override
			public void onError(String error) {

			}
		});

	}


	public void loadCaseData() {
		mBaseHttpClient.postData1(USER_CASH_LIST, new JSONObject(), new AppAjaxCallback.onResultListener() {
			@Override
			public void onResult(String data, String msg) {
				mTvCashNum.setText(JsonUtils.parseList(data, Cash.class).size() + "");
			}

			@Override
			public void onError(String error) {

			}
		});
	}

	/**
	 * 加载地址数据
	 */
	private void loadAddressData(){
		//先网络请求默认地址
		JSONObject jb = new JSONObject();
		mBaseHttpClient.postData(ADDRESS_LIST, jb, new onRecevieDataListener<Address>() {

			@Override
			public void onReceiverData(List<Address> data, String msg) {
				cancelProgressDialog();
				if (data.size() == 0) {
					startActivityForResult(new Intent(getActivity(), AddressAddActivity.class).putExtra(INTENT_ID, 1),
							ADDRESS_ADD_REQUESTCODE);

				} else {
					startActivityForResult(new Intent(getActivity(), AddressListActivity.class).
									putParcelableArrayListExtra(INTENT_ID, (ArrayList<? extends Parcelable>) data),
							ADDRESS_SELECT_REQUESTCODE);

				}
			}

			@Override
			public void onReceiverError(String msg) {
				cancelProgressDialog();
				showToast(msg);
			}

			@Override
			public Class<Address> dataTypeClass() {
				return Address.class;
			}
		});
	}

	private Sign mSign;
	private void loadSign(){
		JSONObject jb = new JSONObject();
		try{
			jb.put("u_id",AppContext.USER_ID);
		}catch (JSONException e){

		}
		mBaseHttpClient.postData1(SIGN_HISTORY, jb, new AppAjaxCallback.onResultListener() {
			@Override
			public void onResult(String data, String msg) {

				mSign = JsonUtils.parse(data, Sign.class);

				final Calendar nextYear = Calendar.getInstance();
				nextYear.add(Calendar.YEAR, 1);

				final Calendar lastYear = Calendar.getInstance();
				lastYear.add(Calendar.YEAR, -1);
				String html = "<font color='red'>" + mSign.getRecord().getJf_num() + " </font>个";
				mTvMiGuan.setText(Html.fromHtml(html));
				//mSign.getRecord().getJf_num()+" </font>个蜜罐
			}

			@Override
			public void onError(String error) {

			}
		});
	}

	private int mJiFen;
	private void loadJifen(){
		JSONObject jb = new JSONObject();
		try{
			jb.put("u_id",AppContext.USER_ID);
		}catch (JSONException e){

		}
		mBaseHttpClient.postData1(MY_JIFEN, jb, new AppAjaxCallback.onResultListener() {
			@Override
			public void onResult(String data, String msg) {
				try{
					JSONObject jb = new JSONObject(data);
					mJiFen = jb.getInt("jf");
					String html = "<font color='red'>"+mJiFen+"</font>";
					mTvJifenNum.setText(Html.fromHtml(html));

				}catch (JSONException e){
					String html = "<font color='red'>0</font>";
					mTvJifenNum.setText(Html.fromHtml(html));
				}
			}

			@Override
			public void onError(String error) {

			}
		});
	}



	private PopupWindow mPopPhone;
	private void showPhonePop(){
		if(mPopPhone == null){
			mPopPhone = new PopupWindow(getActivity());
		}
	}
}
