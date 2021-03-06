package com.lxyg.app.customer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.bean.User;

import dev.mirror.library.utils.JsonUtils;

/**
 * 本地存储工具类
 * @author 王沛栋
 *
 */
public class SharePreferencesUtil implements Constants {
	private static  SharedPreferences mSPreferences;

	/**
	 * 单例 SharedPreferences
	 * @param context
	 * @return
	 */
	private static SharedPreferences getInstance(Context context){
		if(mSPreferences == null){
			mSPreferences = context.getSharedPreferences(USER_INFO, 0);
		}
		return mSPreferences;
	}

	/**
	 * 保存登录信息
	 * @param context
	 * @param phone
	 * @param password
	 */
	public static void saveLoginInfo(Context context,String phone,String password){
//		if(mSPreferences == null){
//			mSPreferences = context.getSharedPreferences(USER_INFO, 0);
//		}
		mSPreferences = getInstance(context);
		mSPreferences.edit().putString(USER_INFO_PHONE, phone).commit();
		mSPreferences.edit().putString(USER_INFO_PASS, password).commit();
	}

	/**
	 * 保存推送状态
	 * @param context
	 * @param status
	 */
	public static void savePushStatus(Context context,boolean status){
		mSPreferences = getInstance(context);
		mSPreferences.edit().putBoolean("PUSH_STATUS", status).commit();
	}


	/**
	 * 获取推送状态
	 * 开or关
	 * @param context
	 * @return
	 */
	public static boolean getPushStatus(Context context){
		mSPreferences = getInstance(context);
		//如果是空 怎表示从未设置过推送开关  默认是开着的
		return mSPreferences.getBoolean("PUSH_STATUS",true);
	}


	/**
	 * 保存店铺信息
	 * @param context
	 * @param msg
	 */
	public static void saveShopInfo1(Context context,String msg){
		mSPreferences = getInstance(context);
		mSPreferences.edit().putString(SHOP_INFO1, msg).commit();
	}


	private static  SharedPreferences mSPreferences2;
	private static SharedPreferences getInstance2(Context context){
		if(mSPreferences2 == null){
			mSPreferences2 = context.getSharedPreferences("SQLITE_VERSION", 0);
		}
		return mSPreferences2;
	}

	/**
	 * 获取登录信息
	 * @param context
	 * @return
	 */
	public static boolean isFirstLoad(Context context){
		mSPreferences2 = getInstance2(context);
		String versionName = AppUtil.getAppVersionName(context);
		if(!TextUtils.isEmpty(mSPreferences2.getString("versionName",""))){
			//如果版本号一致
			if(mSPreferences2.getString("versionName","").equals(versionName))
				return false;
			else
				return true;
		}else{
			mSPreferences2.edit().putString("versionName", versionName).commit();
			return true;
		}
//
//		if(!TextUtils.isEmpty(mSPreferences2.getString("isFristLoad", ""))){
//			return false;
//		}else{
//			AppUtil.getAppVersionName(context);
//			mSPreferences2.edit().putString("isFristLoad", "ok").commit();
//			return true;
//		}
	}

	/**
	 * 获取店铺id
	 * @param context
	 * @return
	 */
	public static String getShopId(Context context){
		mSPreferences2 = getInstance2(context);
		return mSPreferences2.getString("getShopIdForLXYG", "");
	}

	/**
	 * 存储店铺id
	 * @param context
	 * @param shopId
	 */
	public static void saveShopId(Context context,String shopId){
		mSPreferences2 = getInstance2(context);
		mSPreferences2.edit().putString("getShopIdForLXYG", shopId).commit();
	}

	/**
	 * 存储用户信息
	 * @param context
	 * @param msg
	 */
	public static void saveUserInfo(Context context,String msg){
		mSPreferences = getInstance(context);
		if(!TextUtils.isEmpty(msg)){
			mSPreferences.edit().putString(USER_INFO, msg).commit();
		}
	}

	/**
	 * 获取店铺信息
	 * @param context
	 * @return
	 */
	public static User getUserInfo(Context context){
		mSPreferences = getInstance(context);
		String str = mSPreferences.getString(USER_INFO, "");
		if(!TextUtils.isEmpty(str)){
			return JsonUtils.parse(str, User.class);
		}else{
			return null;
		}
	}


	/**
	 * 获取登录信息
	 * @param context
	 * @return
	 */
	public static String getFourmId(Context context,String id){
		mSPreferences2 = getInstance2(context);
		String fourmId = mSPreferences2.getString("FOURM_ID","");
		mSPreferences2.edit().putString("FOURM_ID", id).commit();
		return fourmId;
//		if(!TextUtils.isEmpty(mSPreferences2.getString("FOURM_ID",""))){
//
//			return mSPreferences2.getString("FOURM_ID","");
//
//		}else{
//			return null;
//		}
//
//		if(!TextUtils.isEmpty(mSPreferences2.getString("isFristLoad", ""))){
//			return false;
//		}else{
//			AppUtil.getAppVersionName(context);
//			mSPreferences2.edit().putString("isFristLoad", "ok").commit();
//			return true;
//		}
	}



	/**
	 * 删除所有登陆信息
	 * @param context
	 */
	public static void deleteInfo(Context context){
		mSPreferences = getInstance(context);
		mSPreferences.edit().clear().commit();
	}

}
