package com.lxyg.app.customer.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lxyg.app.customer.R;
import com.lxyg.app.customer.bean.Address;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter.ViewHolder;
import dev.mirror.library.utils.JsonUtils;

public class AddressListActivity extends BaseListActivity<Address> implements Constants {
	@Override
	public int setLayoutId() {
		return R.layout.activity_address_list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mList = new ArrayList<Address>();
		mListView.getRefreshableView().setDividerHeight(30);
		mListView.setMode(Mode.DISABLED);

		initButton(R.id.btn);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn:
			startActivityForResult(new Intent(AddressListActivity.this,AddressAddActivity.class).putExtra(INTENT_ID, 2),
					ADDRESS_ADD_REQUESTCODE);
			break;
		}
	}

	private int mAddressPosition=0;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = getActivity().getLayoutInflater().inflate(R.layout.item_address, null);
		}

		ImageView select = (ImageView) ViewHolder.get(convertView, R.id.img_select);
		ImageView update = (ImageView)ViewHolder.get(convertView,R.id.img_update);
		TextView name = (TextView) ViewHolder.get(convertView, R.id.name);
		TextView phone = (TextView) ViewHolder.get(convertView, R.id.phone);
		TextView address = (TextView) ViewHolder.get(convertView, R.id.address);


		if(position == 0){
			select.setVisibility(View.VISIBLE);
		}else{
			select.setVisibility(View.GONE);
		}
		final Address g = mList.get(position);
		name.setText(g.getName());
		phone.setText(g.getPhone());
		address.setText(g.getProvince_name() + g.getCity_name() + g.getArea_name() + g.getStreet());

		update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(AddressListActivity.this,
						AddressUpdateActivity.class).putExtra(INTENT_ID, 2)
						.putExtra("bean", JsonUtils.beanToString(g, Address.class)), ADDRESS_UPDATE);

				mAddressPosition = position;

			}
		});

		return convertView;

	}

	@Override
	public void loadData() {
		setBack();
		setTitleText("地址列表");

		//先网络请求默认地址
		JSONObject jb = new JSONObject();
		mBaseHttpClient.postData(ADDRESS_LIST,jb, new AppAjaxCallback.onRecevieDataListener<Address>() {

			@Override
			public void onReceiverData(List<Address> data, String msg) {
				if (data.size() == 0) {
					showToast("暂无地址信息");
					finish();
					return;
				} else {
					mList.clear();
					mList.addAll(data);
					setListAdapter();
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
		//放弃传参  否则如果一旦修改地址  更新的地址传递不来
//		mList = getIntent().getExtras().getParcelableArrayList(INTENT_ID);
//		setListAdapter();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode ==Activity.RESULT_OK){
			switch (requestCode) {
				case ADDRESS_ADD_REQUESTCODE:
					Bundle mBundle = data.getExtras();

					mList.add(0, (Address) mBundle.getParcelable(ADR));

					mAdapter.notifyDataSetChanged();
					break;
				case ADDRESS_UPDATE:
					Bundle mBundle3 = data.getExtras();
					mList.remove(mAddressPosition);
					mList.add(mAddressPosition,(Address)mBundle3.getParcelable(ADR));

					mAdapter.notifyDataSetChanged();
					break;
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View view,
			final int position, long num) {
		showNormalDialogByTwoButton("选择地址", "确定将此地址设为收货地址？", "确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent(AddressListActivity.this, OrderMakeActivity.class);
				i.putExtra(ADR, mList.get(position - 1));

				setResult(RESULT_OK, i);
				finish();
			}
		}, "删除", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				delete(position-1);
			}
		});

//		showNormalDialog("选择地址", "确定将此地址设为收货地址？", "确定", new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Intent i = new Intent(AddressListActivity.this, OrderMakeActivity.class);
//				i.putExtra(ADR, mList.get(position - 1));
//
//				setResult(RESULT_OK, i);
//				finish();
//
//			}
//		});
	}

	private void delete(final int position){
		JSONObject jb = new JSONObject();
		try {
			jb.put("addressId",mList.get(position).getAddressId());
		}catch (JSONException e){

		}
		mBaseHttpClient.postData1(ADDRESS_DELETE, jb, new AppAjaxCallback.onResultListener() {
			@Override
			public void onResult(String data, String msg) {
				showToast(msg);
				mList.remove(position);
				if(mList.size() == 0){
					finish();
				}else{
					mAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onError(String error) {

			}
		});

	}



}
