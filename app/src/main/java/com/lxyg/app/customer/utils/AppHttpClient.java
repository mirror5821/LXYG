package com.lxyg.app.customer.utils;

import android.text.TextUtils;

import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

/**
 * app请求封装类
 * @author 王沛栋
 *
 */

public class AppHttpClient{
	public AppHttpClient(){

	}

	public static final String SERVIER_HEADER = Constants.HOST_HEADER;
	public void postData1(String fName,final JSONObject json,AppAjaxCallback.onResultListener listener){
		StringBuilder sb = new StringBuilder();
		sb.append(SERVIER_HEADER);
		sb.append(fName);

		AppAjaxParam param = new AppAjaxParam(fName,sb.toString());
		if(!TextUtils.isEmpty(AppContext.USER_ID)){
			try {

				json.put("uid", AppContext.USER_ID);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		param.addParameter(Constants.JSONG_KEY, json.toString());
		x.http().post(param, new AppAjaxCallback<>(listener));

	}
	public <T> void postData(String fName,final JSONObject json,AppAjaxCallback.onRecevieDataListener<T> listener){
		StringBuilder sb = new StringBuilder();
		sb.append(SERVIER_HEADER);
		sb.append(fName);
		AppAjaxParam param = new AppAjaxParam(fName,sb.toString());
		if(!TextUtils.isEmpty(AppContext.USER_ID)){
			try {

				json.put("uid", AppContext.USER_ID);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		param.addParameter(Constants.JSONG_KEY, json.toString());
		x.http().post(param, new AppAjaxCallback<>(listener));
	}

	public void uploadImg(String imgData,AppAjaxCallback.onResultListener listener) {

		StringBuilder sb = new StringBuilder();
		sb.append(Constants.HOST_IMG_HEADER);
		sb.append(Constants.UPLOAD_IMG);
		AppAjaxParam param = new AppAjaxParam(sb.toString());

		param.addParameter("ImgData",imgData);
		x.http().post(param, new AppAjaxCallback<>(listener));
	}
}