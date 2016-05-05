package com.lxyg.app.customer.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

public class FeedBackActivity extends BaseActivity {
	private EditText mEt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		setBack();
		setTitleText("意见反馈");
		mEt = initEditText(R.id.et);
		initButton(R.id.sub);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.sub:
			String msg = mEt.getText().toString();
			if(TextUtils.isEmpty(msg)){
				showToast("请留下您的宝贵建议!");
				return;
			}

			sub(msg);
			break;

		}
	}
	private void sub(String msg) {
		JSONObject jb = new JSONObject();
		try {
			jb.put("content", msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}


		mBaseHttpClient.postData1(FEED_BACK, jb, new AppAjaxCallback.onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				showToast(msg);
				finish();
			}

			@Override
			public void onError(String error) {
				showToast(error);

			}
		});
	}
}
