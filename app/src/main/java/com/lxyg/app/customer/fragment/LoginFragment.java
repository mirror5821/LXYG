package com.lxyg.app.customer.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.User;
import com.lxyg.app.customer.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;
import com.lxyg.app.customer.utils.SharePreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.mirror.library.utils.JsonUtils;

public class LoginFragment extends BaseFragment{
	
	private EditText mETPhone,mETCode;
	private Button mButtonResend,mButtonSub;

	private BroadcastReceiver smsReceiver;
	private IntentFilter filter2;
	private Handler handler;
	private String strContent;
	private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";
	
	@Override
	public int setLayoutId() {
		return R.layout.fragment_login;
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		
		mETPhone = (EditText)view.findViewById(R.id.phone);
		mETCode = (EditText)view.findViewById(R.id.code);

		mButtonResend = (Button)view.findViewById(R.id.btn_code);
		mButtonResend.setOnClickListener(this);


		mButtonSub = (Button)view.findViewById(R.id.sub);
		mButtonSub.setOnClickListener(this);

		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				mETCode.setText(strContent);
				mCode = strContent;
				sub(phone);
			};
		};
		filter2 = new IntentFilter();
		filter2.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter2.setPriority(Integer.MAX_VALUE);
		smsReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Object[] objs = (Object[]) intent.getExtras().get("pdus");
				for (Object obj : objs) {
					byte[] pdu = (byte[]) obj;
					SmsMessage sms = SmsMessage.createFromPdu(pdu);
					// 短信的内容
					String message = sms.getMessageBody();
					// 短息的手机号。。+86开头？
					String from = sms.getOriginatingAddress();
					if (!TextUtils.isEmpty(from)) {
						String code = patternCode(message);
						if (!TextUtils.isEmpty(code)) {
							strContent = code;
							handler.sendEmptyMessage(1);
						}
					}
				}
			}
		};
		getActivity().registerReceiver(smsReceiver, filter2);
	}
	/**
	 * 匹配短信中间的6个数字（验证码等）
	 * 
	 * @param patternContent
	 * @return
	 */
	private String patternCode(String patternContent) {
		if (TextUtils.isEmpty(patternContent)) {
			return null;
		}
		Pattern p = Pattern.compile(patternCoder);
		Matcher matcher = p.matcher(patternContent);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}
	private String phone;
	@Override
	public void onClick(View v) {
		super.onClick(v);
		//监测手机号码
		phone = mETPhone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			showToast("请输入电话号码!");
			return;
		}

		//貌似有缓存 放到Appcontext死活不起作用
		Pattern p = Pattern.compile("^0?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$");
		Matcher m = p.matcher(phone);

		if(!m.matches()){
			showToast("请输入正确的手机号码");
			return;
		}

		switch (v.getId()) {
		case R.id.sub:
			if(TextUtils.isEmpty(mCode)){
				showToast("请先验证手机号码!");
				return;
			} 
			String code = mETCode.getText().toString();
			if(TextUtils.isEmpty(code)){
				showToast("请输入验证码!");
				return;
			}
			if(!code.equals(mCode)){
				showToast("验证码不正确!");
				return;
			}
			sub(phone);
			break;

		case R.id.btn_code:
			startCountDown();
			getCode(phone);
			break;
		}
	}

	private void sub(String phone){

		JSONObject jb = new JSONObject();
		try {
			jb.put("phone", phone);
			jb.put("code", mCode);
			jb.put("lat", AppContext.Latitude);
			jb.put("lng", AppContext.Longitude);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mBaseHttpClient.postData1(USER_LOGIN, jb, new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				SharePreferencesUtil.saveUserInfo(getActivity(), data);
				showToast(msg);
				AppContext.USER_ID = JsonUtils.parse(data, User.class).getUuid();
				AppContext.IS_LOGIN = true;
				
				Uri uri = Uri.parse("Login_scuess");
				Intent intent = new Intent(null,uri);
				setResult(getActivity().RESULT_OK, intent);
				getActivity().finish();
				
				/*Intent i;
				switch (mIntentId) {
				//MyFragment进入的
				case 0:
					startActivity(new Intent(LoginActivity.this,MainActivity.class));
					break;
				//ShopingCarFragment进入的
				case 1:
					i = new Intent(LoginActivity.this,ShopingCarFragment.class);
					
					setResult(RESULT_OK, i);
					break;
				}
				
				finish();*/
			}

			private void setResult(int result_OK, Intent intent) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(String error) {
				showToast(error);
			}
		});
	}

	private String mCode;
	private void getCode(String phone){
		JSONObject jb = new JSONObject();
		try {
			jb.put("phone", phone);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mBaseHttpClient.postData1(GET_CODE, jb, new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				JSONObject jb;
				try {
					jb = new JSONObject(data);
					mCode = jb.getString("code");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				showToast(msg);

			}

			@Override
			public void onError(String error) {
				showToast(error);
			}
		});
	}



	private int mSeconds = 60;
	private Handler mHandler = new Handler();
	private Timer mTimer;
	private TimerTask mTimerTask;

	private void startCountDown() {

		mSeconds = 60;
		stop();
		mButtonResend.setEnabled(false);
		mTimer = new Timer();
		mTimerTask = new TimerTask() {

			@Override
			public void run() {
				mSeconds--;
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						if (mSeconds > 0) {
							mButtonResend.setText(mSeconds + "秒\n后重发");
						} else {
							mButtonResend.setText("重发");
							mButtonResend.setEnabled(true);
							stop();
						}
					}
				});
			}
		};
		mTimer.schedule(mTimerTask, 0, 1000);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(smsReceiver);
		stop();
	}


	private void stop() {
		if (mTimer != null) {
			mTimer.cancel();
		}
		if (mTimerTask != null) {
			mTimerTask.cancel();
		}
	}
}
