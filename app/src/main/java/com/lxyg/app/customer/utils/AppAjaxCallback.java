package com.lxyg.app.customer.utils;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.List;

import dev.mirror.library.utils.JsonUtils;

/**
 * 
 * 请求返回结果封装类
 * @author 王沛栋
 *
 * @param <T>
 */
public class AppAjaxCallback<T> implements Callback.CommonCallback<String> {
	private onRecevieDataListener<T> mRecevieDataListener;
	private onResultListener mListener;

	//onRecevieDataListener
	public AppAjaxCallback(onRecevieDataListener<T> dataListener){
		mRecevieDataListener = dataListener;
	}

	public AppAjaxCallback(onResultListener dataListener){
		this.mListener = dataListener;
	}


	/**
	 * 定义普通数据请求listener
	 */
	public interface onResultListener{
		void onResult(String data,String msg);
		void onError(String msg);
	}

	/**
	 * 定义list请求listener
	 * @param <T>
	 */
	public interface onRecevieDataListener<T>{
		void onReceiverData(List<T> data,String msg);
		void onReceiverError(String msg);
		Class<T> dataTypeClass();
	}

	private String listNoData = "未查询到任何数据\n下拉刷新数据";
	private String mErrorMsg = "网络错误,请检查您的网络!";


	@Override
	public void onSuccess(String result) {
		String t =  result.toString();
		System.out.println("--------------------"+t);
		if(!TextUtils.isEmpty(t)){
			try{
				JSONObject jb = new JSONObject(t);
				String status = jb.getString("code");

				String msg = jb.getString("msg");
				//如果==10001，表示查询错误
				if(!status.equals("10001")){
					String data = jb.getString("data").toString();
					//表示请求是列表数据
					if(mRecevieDataListener !=null){
						List<T> list = JsonUtils.parseList(data, mRecevieDataListener.dataTypeClass());
						//如果数据不为空
						if(list!=null){
							mRecevieDataListener.onReceiverData(list, msg);
						}else{
							try{
								//判断分页list的返回
								JSONObject jb2 = new JSONObject(data);
								if(!jb2.getString("list").equals(null)){
									mRecevieDataListener.onReceiverData(JsonUtils.parseList(jb2.getString("list"),
											mRecevieDataListener.dataTypeClass()),msg);
								}else{
									mRecevieDataListener.onReceiverError(listNoData);
								}
							}catch(Exception e){
								mRecevieDataListener.onReceiverError(listNoData);
							}
//							mRecevieDataListener.onReceiverError("----"+msg);
						}
					}else{
						mListener.onResult(data,msg);
					}
				}else{//表示没有返回数据
					if(mRecevieDataListener!=null){
						mRecevieDataListener.onReceiverError(msg);
					}else{
						mListener.onError(msg);
					}
				}
			}catch(JSONException e){
//				if(mRecevieDataListener!=null){
//					mRecevieDataListener.onReceiverError(e.getMessage());
//				}else{
//					mListener.onError(e.getMessage());
//				}
				if(mRecevieDataListener!=null){
					mRecevieDataListener.onReceiverError(mErrorMsg);
				}else{
					mListener.onError(mErrorMsg);
				}
			}
		}else{
			if(mRecevieDataListener!=null){
				mRecevieDataListener.onReceiverError(mErrorMsg);
			}else{
				mListener.onError(mErrorMsg);
			}
		}
	}

	@Override
	public void onError(Throwable ex, boolean isOnCallback) {
		if(mRecevieDataListener!=null){
			mRecevieDataListener.onReceiverError(mErrorMsg);
		}else{
			mListener.onError(mErrorMsg);
		}
//		if(mRecevieDataListener!=null){
//			mRecevieDataListener.onReceiverError(ex.getLocalizedMessage());
//		}else{
//			mListener.onError(ex.getLocalizedMessage());
//		}

	}

	@Override
	public void onCancelled(CancelledException cex) {

	}

	@Override
	public void onFinished() {

	}
}