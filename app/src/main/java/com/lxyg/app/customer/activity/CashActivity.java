package com.lxyg.app.customer.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lxyg.app.customer.R;
import com.lxyg.app.customer.bean.Cash;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.utils.AppAjaxCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;

public class CashActivity extends BaseListActivity<Cash>{
	@Override
	public int setLayoutId() {
		return R.layout.activity_cash_list;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = getActivity().getLayoutInflater().inflate(R.layout.item_cash_red, null);
		}

//		TextView name = (TextView) DevListBaseAdapter.ViewHolder.get(convertView, R.id.name);
//		TextView intro = (TextView) DevListBaseAdapter.ViewHolder.get(convertView, R.id.intro);
//		TextView use = (TextView) DevListBaseAdapter.ViewHolder.get(convertView, R.id.use);
//		TextView time = (TextView) DevListBaseAdapter.ViewHolder.get(convertView, R.id.time);
		final TextView ePay = (TextView) DevListBaseAdapter.ViewHolder.get(convertView, R.id.cash);

		final Cash c = mList.get(position);
//		name.setText(c.getCash_title());
//		intro.setText("限在线支付使用");
//		use.setText("暂无使用");
//		time.setText(c.getEnd_time().equals(null)?"永久有限":c.getEnd_time().substring(0,11));
		ePay.setText(c.getCash()/100+"");
//		ePay.setText("可使用"+c.getCash()/100+"");
		return convertView;
	}

	@Override
	public void loadData() {
		setBack();
		setTitleText("电子积分");

		mBaseHttpClient.postData(USER_CASH_LIST, new JSONObject(), new AppAjaxCallback.onRecevieDataListener<Cash>() {
			@Override
			public void onReceiverData(List<Cash> data, String msg) {
				if(mList == null){
					mList = new ArrayList<Cash>();
				}
				mList.addAll(data);
				setListAdapter();

				mListView.setMode(PullToRefreshBase.Mode.DISABLED);
			}

			@Override
			public void onReceiverError(String msg) {

			}

			@Override
			public Class<Cash> dataTypeClass() {
				return Cash.class;
			}
		});

	}


	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View view,
							int position, long num) {
		getActivity().startActivity(new Intent(getActivity(), CategoryNew2Activity.class).putExtra(Constants.INTENT_ID, 0));
		finish();
	}
}
