package com.lxyg.app.customer.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.bean.User;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppAjaxCallback.onResultListener;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;
import com.lxyg.app.customer.utils.SharePreferencesUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import dev.mirror.library.utils.JsonUtils;

@SuppressWarnings("deprecation")
public class WechatAndPhoneLoginActivity  extends BaseActivity {
	public static IWXAPI WXapi;
	private String weixinCode;
	private static String get_access_token = "";
	// 获取第一步的code后，请求以下链接获取access_token
	public static String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	//获取用户个人信息
	public static String GetUserInfo="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";

	private TextView mTvPhone;
	private TextView mTvWechat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_phone_wechat);
		setBack();
		setTitleText("选择登陆方式");

		mTvPhone = (TextView)findViewById(R.id.phone);
		mTvWechat = (TextView)findViewById(R.id.wechat);

		mTvPhone.setOnClickListener(this);
		mTvWechat.setOnClickListener(this);

	}

	private static Handler handler=new Handler();
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.wechat:
			showProgressDialog("正在获取登录信息！", true);

			new Thread(new Runnable() {
				@Override
				public void run() {
					WXLogin();
					// tvMessage.setText("...");
					// 以上操作会报错，无法再子线程中访问UI组件，UI组件的属性必须在UI线程中访问
					// 使用post方式修改UI组件tvMessage的Text属性
					handler.post(new Runnable() {
						@Override
						public void run() {
							cancelProgressDialog();
						}
					});
				}
			}).start();

			break;

		case R.id.phone:
			startActivityForResult(new Intent(getActivity(),LoginActivity.class), LOGIN_CODE1);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode ==Activity.RESULT_OK){
			switch (requestCode) {
			case LOGIN_CODE1:
				Uri uri = Uri.parse("Login_scuess");
				Intent intent = new Intent(null,uri);
				setResult(RESULT_OK, intent);
				finish();
				break;
			}
		}
	}

	/**
	 * 登录微信
	 */
	private void WXLogin() {
		WXapi = AppContext.WX_API;
		//		WXapi = WXAPIFactory.createWXAPI(this,Constants.WECHAT_APP_ID, true);
		WXapi.registerApp(Constants.WECHAT_APP_ID);
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "wechat_sdk_demo";
		WXapi.sendReq(req);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(AppContext.RESP == null){
			return;
		}
		if(!AppContext.WECHAT_LOGIN){
			return;
		}
		showProgressDialog("正在提交登陆信息",true);
		/*
		 * resp是你保存在全局变量中的
		 */
		if (AppContext.RESP.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
			// code返回
			weixinCode = ((SendAuth.Resp)AppContext.RESP).code;
			/*
			 * 将你前面得到的AppID、AppSecret、code，拼接成URL
			 */
			get_access_token = getCodeRequest(weixinCode);
			Thread thread=new Thread(downloadRun);
			thread.start();
			try {
				thread.join();

			} catch (InterruptedException e) {
				e.printStackTrace();
				cancelProgressDialog();
			}
		}
	}
	/**
	 * 获取access_token的URL（微信）
	 * @param code 授权时，微信回调给的
	 * @return URL
	 */
	public static String getCodeRequest(String code) {
		String result = null;
		GetCodeRequest = GetCodeRequest.replace("APPID",
				urlEnodeUTF8(Constants.WECHAT_APP_ID));
		GetCodeRequest = GetCodeRequest.replace("SECRET",
				urlEnodeUTF8(Constants.WECHAT_SECRET));
		GetCodeRequest = GetCodeRequest.replace("CODE",urlEnodeUTF8( code));
		result = GetCodeRequest;
		return result;
	}
	/**
	 * 获取用户个人信息的URL（微信）
	 * @param access_token 获取access_token时给的
	 * @param openid 获取access_token时给的
	 * @return URL
	 */
	public static String getUserInfo(String access_token,String openid){
		String result = null;
		GetUserInfo = GetUserInfo.replace("ACCESS_TOKEN",
				urlEnodeUTF8(access_token));
		GetUserInfo = GetUserInfo.replace("OPENID",
				urlEnodeUTF8(openid));
		result = GetUserInfo;
		return result;
	}
	public static String urlEnodeUTF8(String str) {
		String result = str;
		try {
			result = URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public  Runnable downloadRun = new Runnable() {

		@Override
		public void run() {
			WXGetAccessToken();

		}
	};

	/**
	 * 获取access_token等等的信息(微信)
	 * 如微信登陆  请使用下面的方法修改  因6.0 没有apache的包直接引用
	 */
	private  void WXGetAccessToken(){
//		String access_token="";
//		String openid ="";

		FinalHttp finalHttp = new FinalHttp();
		finalHttp.post(get_access_token, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);

				String josn = t.toString();
				try{
					JSONObject json1 = new JSONObject(josn);
					String access_token = (String) json1.get("access_token");
					String openid = (String) json1.get("openid");
					openid = openid.replace("-","");

					mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, openid));

					String get_user_info_url=getUserInfo(access_token,openid);
					WXGetUserInfo(get_user_info_url);
				}catch (JSONException e){

				}

			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
			}
		});

//		HttpClient get_access_token_httpClient = new DefaultHttpClient();

//		try {
//			HttpPost postMethod = new HttpPost(get_access_token);
//			HttpResponse response = get_access_token_httpClient.execute(postMethod); // 执行POST方法
//			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				InputStream is = response.getEntity().getContent();
//				BufferedReader br = new BufferedReader(
//						new InputStreamReader(is));
//				String str = "";
//				StringBuffer sb = new StringBuffer();
//				while ((str = br.readLine()) != null) {
//					sb.append(str);
//				}
//				is.close();
//				String josn = sb.toString();
//				JSONObject json1 = new JSONObject(josn);
//				access_token = (String) json1.get("access_token");
//				openid = (String) json1.get("openid");
//				openid = openid.replace("-","");
//
//				mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, openid));
//			} else {
//			}
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}

//		String get_user_info_url=getUserInfo(access_token,openid);
//		WXGetUserInfo(get_user_info_url);
	}


	/**
	 * 以下内容为设置推送消息
	 */
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			switch (code) {
			case 0:
				break;

			case 6002:
				mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
				break;

			}

		}

	};

	private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			switch (code) {
			case 0:
				break;

			case 6002:
				mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
				break;

			}

		}

	};

	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;

	private final Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
				break;

			case MSG_SET_TAGS:
				JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
				break;
			}
		}
	};


	String openid="";
	String nickname="";
	String headimgurl="";
	/**
	 * 获取微信用户个人信息
	 * @param get_user_info_url 调用URL
	 */
	private  void WXGetUserInfo(String get_user_info_url){
		FinalHttp finalHttp = new FinalHttp();
		finalHttp.get(get_user_info_url,new AjaxCallBack<String>(){
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);

				String josn = t.toString();
				try{
					JSONObject json1 = new JSONObject(josn);
					openid = (String) json1.get("openid");
					openid = openid.replace("-", "");
					nickname = (String) json1.get("nickname");
					headimgurl = (String) json1.get("headimgurl");

					sub();
				}catch (JSONException e){

				}

			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
			}
		});

//		HttpClient get_access_token_httpClient = new DefaultHttpClient();
//
//		try {
//			HttpGet getMethod = new HttpGet(get_user_info_url);
//			HttpResponse response = get_access_token_httpClient.execute(getMethod); // 执行GET方法
//			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				InputStream is = response.getEntity().getContent();
//				BufferedReader br = new BufferedReader(
//						new InputStreamReader(is));
//				String str = "";
//				StringBuffer sb = new StringBuffer();
//				while ((str = br.readLine()) != null) {
//					sb.append(str);
//				}
//				is.close();
//				String josn = sb.toString();
//				JSONObject json1 = new JSONObject(josn);
//				openid = (String) json1.get("openid");
//				openid = openid.replace("-","");
//				nickname = (String) json1.get("nickname");
//				headimgurl=(String)json1.get("headimgurl");
//
//				//				wechatInfo();
//				sub();
//			} else {
//			}
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}

	}

	private void sub(){
		JSONObject jb = new JSONObject();
		try {
			jb.put("weName", nickname);
			jb.put("openId", openid);
			jb.put("lat", AppContext.Latitude);
			jb.put("lng", AppContext.Longitude);
			jb.put("headimgurl", headimgurl);
			jb.put("version", WECHAT_VALUE);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mBaseHttpClient.postData1(WECHAT_LOGIN, jb, new onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				SharePreferencesUtil.saveUserInfo(getApplicationContext(), data);
				showToast(msg);
				AppContext.USER_ID = JsonUtils.parse(data, User.class).getUuid();
				AppContext.IS_LOGIN = true;
				//告知首页登录成功  并切换登录绑定的店铺数据
				AppContext.SHOP_CHANGE = 1;

				Uri uri = Uri.parse("Login_scuess");
				Intent intent = new Intent(null,uri);
				setResult(RESULT_OK, intent);
				cancelProgressDialog();
				finish();

			}

			@Override
			public void onError(String error) {
				showToast(error);
				cancelProgressDialog();
				finish();
			}
		});
	}
}