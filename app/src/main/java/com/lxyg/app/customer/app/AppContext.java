package com.lxyg.app.customer.app;

import android.os.Build;

import com.baidu.mapapi.SDKInitializer;
import com.lxyg.app.customer.R;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.bean.User;
import com.lxyg.app.customer.utils.SharePreferencesUtil;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.tsz.afinal.FinalDb;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import dev.mirror.library.app.BaseAppContext;

/**
 * app全局配置类
 * @author 王沛栋
 *
 */
public class AppContext extends BaseAppContext {
	//我的位置信息
	public static double Longitude =0;
	public static double Latitude = 0;
	public static String Address;
	public static String LOC_CITY = "郑州市";
	public static String LOC_AREA = "金水区";

	//店铺信息
	public static String SHOP_ID;
	public static String SHOP_NAME;
	public static String SHOP_PHONE;
	public static String PEI_SONG;
	public static int SHOP_TYPE; //1正常店  2非标准店铺 已弃用
	//店铺切换是使用SwipeRefreshLayout
	public static int SHOP_CHANGE = 0;//0表示没有切换 1表示登录切换  1时 证明从未登录变化成已登录  此时  首页的数据需要重新加载

	//登陆信息
	public static String USER_ID;
	public static boolean IS_LOGIN = false;
	public static boolean WECHAT_LOGIN = false;
	public static IWXAPI WX_API;
	public static BaseResp RESP;

	//数据库
	public static FinalDb mDb;
	public static String SERVICE_PHONE;


	public static boolean IsImmersed = false; //根据系统版本是否能时候通知栏沉浸模式
	public static boolean IS_PAY_OK = false;

	public static ImageOptions mImageOptions;
	@Override
	public void onCreate() {
		super.onCreate();
		User user = SharePreferencesUtil.getUserInfo(getApplicationContext());
		if(user != null){
			USER_ID = SharePreferencesUtil.getUserInfo(getApplicationContext()).getUuid();
			IS_LOGIN = true;
		}
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		//初始化推送
		JPushInterface.init(this);

		//实例化afinaldb
		mDb = FinalDb.create(AppContext.this);


		WX_API =  WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID, false);//wx66772ad04800ff7d
		WX_API.registerApp(Constants.WECHAT_APP_ID);


		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
		JPushInterface.init(this);  //初始化极光推送

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			IsImmersed = true;
		}

		x.Ext.init(this);
		x.Ext.setDebug(false); // 是否输出debug日志

		mImageOptions = new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.ic_item_default)
				.setLoadingDrawableId(R.mipmap.ic_item_default).setUseMemCache(true).setCircular(false).build();
	}

}
