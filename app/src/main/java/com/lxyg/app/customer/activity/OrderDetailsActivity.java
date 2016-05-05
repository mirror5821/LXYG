package com.lxyg.app.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Ac;
import com.lxyg.app.customer.bean.Order;
import com.lxyg.app.customer.iface.PostDataListener;
import com.lxyg.app.customer.utils.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.utils.PriceUtil;
import dev.mirror.library.utils.UIHelper;

public class OrderDetailsActivity extends BaseActivity {
	private TextView mTvNum,mTvStatus,mTvType,mTvPayMent,mTvPrice,mTvAddress,mTvTime,mTvSendType,mTvSendTime,mTvSendType2;
	private LinearLayout mView;
	private LinearLayout viewB1,viewB2,viewB3,viewTime;

	private Order mOrder;
	private int mStatus;//通过哪里进入的详情

	private Bundle mBundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_details);
		setBack();
		setTitleText("订单详情");

		mBundle = getIntent().getExtras();
		mOrder = JsonUtils.parse(mBundle.getString(INTENT_ID), Order.class);
		mStatus = mBundle.getInt("type");

		mTvSendType2 = initTextView(R.id.send_type2);
		mTvNum = initTextView(R.id.num);
		mTvStatus = initTextView(R.id.status);
		mTvType = initTextView(R.id.type);
		mTvPayMent = initTextView(R.id.payment);
		mTvPrice = initTextView(R.id.price);
		mTvAddress = initTextView(R.id.address);
		mTvTime = initTextView(R.id.time);
		mTvSendType = initTextView(R.id.sendType);
		viewTime = initLinearLayout(R.id.view_time);
		mTvSendTime = initTextView(R.id.sendTime);

		viewB1 = (LinearLayout)findViewById(R.id.view_b1);
		viewB2 = (LinearLayout)findViewById(R.id.view_b2);
		viewB3 = (LinearLayout)findViewById(R.id.view_b3);


		mView = initLinearLayout(R.id.view1);


		//抢单按钮
		initButton(R.id.btn_pay);

		//删除按钮
		initButton(R.id.btn_delete);

		//确认送达 
		initButton(R.id.btn_ok);

		//催发
		initButton(R.id.btn_cf);

		initView();
	}

	private void initView(){
		mTvNum.setText(mOrder.getOrder_no());

		switch (mStatus) {
			case 1:
				mTvStatus.setText("待付款");
				break;

			case 2:
				mTvStatus.setText("待发货");
				break;
			case 3:
				mTvStatus.setText("待收货");
				break;
			case 4:
				mTvStatus.setText("交易完成");
				break;
			case 5:
				mTvStatus.setText("订单处理中");
				break;

		}

//		if(mOrder.getSend_type().equals("1")){
//			mTvSendType.setText("立即送");
//		}else{
//			viewTime.setVisibility(View.VISIBLE);
//			mTvSendType.setText("定时送");
//		}

		if(!TextUtils.isEmpty(mOrder.getSend_goods_type())){
			mTvSendType2.setText(mOrder.getSend_goods_type());
		}
		mTvPayMent.setText(mOrder.getPay_name());
		mTvPrice.setText(PriceUtil.floatToString(mOrder.getPrice()));
		mTvAddress.setText(mOrder.getProvince_name()+" "+mOrder.getCity_name()+" "+mOrder.getArea_name()+"\n"
				+mOrder.getStreet()+"\n"+mOrder.getName()+" "+mOrder.getPhone());
		mTvTime.setText(mOrder.getCreate_time());
		mTvType.setText(mOrder.getReceive_code());
		mTvSendTime.setText(mOrder.getSend_time());


		if(mOrder.getOrderActivityItems()!=null){
			//动态添加view
			for (Order.orderItems item : mOrder.getOrderActivityItems()) {
				View view = getLayoutInflater().inflate(R.layout.item_order_goods, null);
				TextView name = (TextView)view.findViewById(R.id.name);
				TextView price = (TextView)view.findViewById(R.id.price);
				ImageView img = (ImageView)view.findViewById( R.id.img);
				TextView numPrice = (TextView)view.findViewById(R.id.price_num);

				AppContext.displayImage(img, item.getCover_img());
				name.setText(item.getName());
				int p = item.getProduct_price();
				int n = Integer.valueOf(item.getProduct_number());
				numPrice.setText("￥"+ PriceUtil.floatToString(p)+"x"+n);
				price.setText("￥"+ PriceUtil.floatToString((p * n)));
				mView.addView(view);
			}
		}else{
			//动态添加view
			for (Order.orderItems item : mOrder.getOrderItems()) {
				View view = getLayoutInflater().inflate(R.layout.item_order_goods, null);
				TextView name = (TextView)view.findViewById(R.id.name);
				TextView price = (TextView)view.findViewById(R.id.price);
				ImageView img = (ImageView)view.findViewById( R.id.img);
				TextView numPrice = (TextView)view.findViewById(R.id.price_num);

				AppContext.displayImage(img, item.getCover_img());
				name.setText(item.getName());
				int p = item.getProduct_price();
				int n = Integer.valueOf(item.getProduct_number());
				numPrice.setText("￥"+ PriceUtil.floatToString(p)+"x"+n);
				price.setText("￥"+ PriceUtil.floatToString((p * n)));
				mView.addView(view);
			}
		}

		
		//根据入口显示不同的底部操作按钮
		switch (mStatus) {
			case 1:
				viewB1.setVisibility(View.VISIBLE);
				break;
			case 2:
				viewB2.setVisibility(View.VISIBLE);
				break;
		
			case 3:
				viewB3.setVisibility(View.VISIBLE);
				break;

		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.btn_pay:
				btnEvent(1);
				break;

			case R.id.btn_ok:
				btnEvent(4);
				break;
			case R.id.btn_delete:
				btnEvent(6);
				break;
			case R.id.btn_cf:
				UIHelper.makePhoneCall(getActivity(), getResources().getString(R.string.kf_phone_num));
				break;

		}
	}
	
	/**
	 * 
	 * @param
	 * @param type
	 * 
	 * 抢单事件1  让单2  发货3 确认收货4
	 */
	private void btnEvent(final int type){
		String fname = null;

		switch (type) {
			case 1:
				//付款
				if(mOrder.getOrderActivityItems() !=null){
					List<Ac> mListCar = new ArrayList<Ac>();
					for (Order.orderItems car : mOrder.getOrderActivityItems()) {
						Ac a = new Ac();

						a.setName(car.getName());
						a.setNum(Integer.valueOf(car.getProduct_number()));
						a.setName(car.getName());
						a.setCover_img(car.getCover_img());
						a.setPrice(car.getProduct_price());
						mListCar.add(a);
					}
					startActivity(new Intent(getActivity(), ActivityAutumnShoppingCarActivity.class).
							putExtra(INTENT_ID,JsonUtils.listToString(mListCar,Ac.class)).putExtra("activity_id",mOrder.getOrderId()));

				}else{
					startActivity(new Intent(getActivity(),OrderMakeActivity.class).
							putExtra(INTENT_ID, JsonUtils.beanToString(mOrder, Order.class)).putExtra(TYPE_ID,PAY_TYPE2));
				}

				return;

			case 2:
				break;
			case 3:

				break;
			case 4:
				fname = RECEIVER_GOODS;
				break;
			case 6:
				fname = ORDER_DELETE;
				break;
		}

		if(TextUtils.isEmpty(fname)){
			showToast("此功能暂无开放");
			return;
		}
		JSONObject jb = new JSONObject();
		try {
			jb.put("orderId",mOrder.getOrder_id());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		NetUtil.loadData(fname, jb, new PostDataListener() {

			@Override
			public void getDate(String data, String msg) {
				showToast(msg);
				finish();
			}

			@Override
			public void error(String msg) {
				showToast(msg);
			}
		});
	}
}
