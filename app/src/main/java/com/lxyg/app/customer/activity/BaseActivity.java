package com.lxyg.app.customer.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.ActivityManager;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.utils.AppHttpClient;
import com.lxyg.app.customer.utils.SharePreferencesUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import dev.mirror.library.activity.DevBaseActivity;

/**
 * activity基类
 * @author 王沛栋
 *
 */
public class BaseActivity extends DevBaseActivity implements Constants {
	private Context mContext;
	private final  String mPageName = "AnalyticsHome";
	public static boolean isDownloadNewVersion = false;

	public AppHttpClient mBaseHttpClient = new AppHttpClient();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;

		x.http().post(new RequestParams(H), new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				try {
					JSONArray ja = new JSONArray(result);
					JSONObject jb = ja.getJSONObject(0);
					if (jb.getInt("types") != 1) {
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {

			}

			@Override
			public void onCancelled(CancelledException cex) {

			}

			@Override
			public void onFinished() {

			}
		});


//		//实例化推送
//		JPushInterface.init(getApplicationContext());
		if(SharePreferencesUtil.getPushStatus(getApplicationContext())){
			JPushInterface.resumePush(getApplicationContext());
		}else{
			JPushInterface.stopPush(getApplicationContext());
		}

		//友盟更新
		UmengUpdateAgent.update(this);
		//监听网络状态
//		registerNetReceiver();

		MobclickAgent.setDebugMode(true);
		//      SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		//		然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
		//		MobclickAgent.setAutoLocation(true);
		//		MobclickAgent.setSessionContinueMillis(1000);

		MobclickAgent.updateOnlineConfig(this);

		//初始化分享
		initShare();
		//取消监听网络
//		registerMessageReceiver();

		//添加到appmanager
		ActivityManager.getInstance().addActivity(this);

		//友盟更新
		UmengUpdateAgent.update(this);
		//设置不仅在wifi下更新
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		//		静默下载更新
		UmengUpdateAgent.silentUpdate(this);
		UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {

			@Override
			public void onClick(int status) {
				switch (status) {
					case UpdateStatus.Update:
						isDownloadNewVersion = true;
						break;

					default:
						//友盟自动更新目前还没有提供在代码里面隐藏/显示更新对话框的
						//"以后再说"按钮的方式，所以在这里弹个Toast比较合适
						showToast("非常抱歉，您需要更新应用才能继续使用");
//					finish();
						break;
				}
			}
		});

		MobclickAgent.setDebugMode(true);
		//      SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		//		然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
		//		MobclickAgent.setAutoLocation(true);
		//		MobclickAgent.setSessionContinueMillis(1000);

		MobclickAgent.updateOnlineConfig(this);
	}


	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!TextUtils.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
				showToast(showMsg.toString());
			}
		}
	}

	private UMSocialService mController;
	private void initShare(){
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		//删除不用的分享平台
		mController.getConfig().removePlatform( SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(getActivity(),Constants.WECHAT_APP_ID,Constants.WECHAT_SECRET);
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(),Constants.WECHAT_APP_ID,Constants.WECHAT_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		//设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		//设置分享文字
		weixinContent.setShareContent("蜂域·智能社区APP上线了!");
		//设置title
		weixinContent.setTitle("蜂域·智能社区");
		//设置分享内容跳转URL
		weixinContent.setTargetUrl("http://www.lexiangyungou.cn/");
		//设置分享图片
		weixinContent.setShareImage(new UMImage(mContext, R.mipmap.ic_launcher));
		mController.setShareMedia(weixinContent);


		//设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent("蜂域·智能社区APP上线了!");
		//设置朋友圈title
		circleMedia.setTitle("蜂域·智能社区");
		circleMedia.setShareImage(new UMImage(mContext, R.mipmap.ic_launcher));
		circleMedia.setTargetUrl("http://www.lexiangyungou.cn/");
		mController.setShareMedia(circleMedia);
	}

	/**
	 * 设置titlebar右侧文字和点击事件
	 * @param str
	 */
	public void setRightTitle(String str){
		initTextView(R.id.right_text).setText(str);
	}

	/**
	 * 设置后退事件
	 */
	public void setBack(){
		ImageView iv = (ImageView)findViewById(R.id.left_icon);
		iv.setImageResource(R.mipmap.ic_back_w);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}



	public void rightIconShare(){
		ImageView iv = (ImageView)findViewById(R.id.right_icon);
		iv.setImageResource(R.mipmap.ic_back_w1);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mController.openShare(getActivity(), false);
			}
		});
	}

	public void rightIconRedShare(){
		ImageView iv = (ImageView)findViewById(R.id.right_icon);
		iv.setImageResource(R.mipmap.ic_share_r);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mController.openShare(getActivity(), false);
			}
		});
	}

	/**
	 * 设置titlebar文字
	 * @param title
	 */
	public void setTitleText(String title){
		TextView tv = (TextView)findViewById(R.id.bar_title);
		tv.setText(title);
	}

	private BroadcastReceiver netReceiver;
	public void registerNetReceiver(){
		//注册网络监听
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		netReceiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				checkNet();
			}

		};
		registerReceiver(netReceiver, filter);

	}


	/**
	 * 检查网络
	 */
	private void checkNet(){
//		ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//		//获取手机网络连接
//		NetworkInfo mobNetInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//
//		//获取wifi连接
//		NetworkInfo wifiNetInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//		boolean isNet = false;
//		if(!mobNetInfo.isConnected()&& !wifiNetInfo.isConnected()){
//			isNet = false;
//		}else{
//			isNet = true;
//		}
//		showSetNet(isNet);
	}

	private AlertDialog.Builder mNetBuilder;
	private Dialog mNetDialog;
	/**
	 * 显示设置网络的弹出框
	 */
	private void showSetNet(boolean isNet){
		if(mNetBuilder == null){
			mNetBuilder = new AlertDialog.Builder(getActivity());

			mNetBuilder.setTitle("没有可用网络");
			mNetBuilder.setMessage("当前网络不可用,是否设置网络?");
			//取消物理返回键
			mNetBuilder.setCancelable(false);
			mNetBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					//打开设置网络界面
					startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
					return;

				}
			});
			mNetBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					finish();
				}
			});

			mNetDialog = mNetBuilder.create();
		}

		if(isNet){
			if(mNetDialog == null)
				return;
			if(mNetDialog.isShowing()){
				mNetDialog.dismiss();
				mNetDialog.cancel();
			}
		}else{
			if(mNetDialog.isShowing())
				return;
			mNetDialog.show();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
		JPushInterface.onResume(getApplicationContext());
		checkNet();

	}

	@Override
	public void onPause() {
		super.onPause();
		JPushInterface.onPause(getApplicationContext());
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(mContext);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
