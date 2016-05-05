package com.lxyg.app.customer.utils;


import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Car;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.bean.Order;
import com.lxyg.app.customer.bean.Product;
import com.lxyg.app.customer.iface.PostDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.utils.PriceUtil;


public abstract class NetUtil implements Constants {
	private static AppHttpClient mHttpClient;


	/**
	 * 不用传递参数的获取data
	 * @param
	 * @param postData
	 */
	public static void loadData(final String fName,JSONObject jb, final PostDataListener postData){
		if(mHttpClient == null){
			mHttpClient = new AppHttpClient();
		}

		if(jb == null){
			jb = new JSONObject();
		}

		mHttpClient.postData1(fName, jb, new AppAjaxCallback.onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				postData.getDate(data,msg);
			}

			@Override
			public void onError(String error) {
				postData.error(error);
			}
		});
	}


	/**
	 * 加载购物车数据
	 */
	public static void loadShoppingCarTotal(final PostDataListener postData){
		final List<Car> mCarList = AppContext.mDb.findAll(Car.class);//查询所有的用户
		//如果购物车为空
		if(mCarList.size()==0){
			postData.error("您的购物车是空的!");

		}else{
			//先清空列表  再然后在加载
			StringBuilder sb = new StringBuilder();
			for (Car car : mCarList) {
				sb.append(car.getProductId()+",");
			}

			String pId = sb.toString().substring(0, sb.toString().lastIndexOf(","));

			JSONObject jb = new JSONObject();
			try {
				jb.put("pids", pId);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			mHttpClient = new AppHttpClient();
			mHttpClient.postData1(SHOPPING_CAR_LIST, jb,new AppAjaxCallback.onResultListener() {

				@Override
				public void onResult(String data, String msg) {
					List<Product> mList = new ArrayList<Product>();
					mList.addAll(JsonUtils.parseList(data, Product.class));

					int  mPriceTotal = 0;
					for(int i=0;i<mCarList.size();i++){
						Car c = mCarList.get(i);
						mPriceTotal = mPriceTotal+ mList.get(i).getPrice()*c.getNum();
					}

					postData.getDate(PriceUtil.floatToString(mPriceTotal),msg);
				}

				@Override
				public void onError(String error) {
					postData.error(error);
				}
			});
		}
	}

	public static void loadOrderTotalPrice(final PostDataListener postData,final Order order){
		//先清空列表  再然后在加载
		StringBuilder sb = new StringBuilder();

		for (Order.orderItems car : order.getOrderItems()) {
			sb.append(car.getProduct_id()+",");
		}

		String pId = sb.toString().substring(0, sb.toString().lastIndexOf(","));

		JSONObject jb = new JSONObject();
		try {
			jb.put("pids", pId);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mHttpClient = new AppHttpClient();
		mHttpClient.postData1(SHOPPING_CAR_LIST, jb,new AppAjaxCallback.onResultListener() {

			@Override
			public void onResult(String data, String msg) {
				List<Product> mList = new ArrayList<Product>();
				mList.addAll(JsonUtils.parseList(data, Product.class));

				int  mPriceTotal = 0;
				for(int i=0;i<order.getOrderItems().size();i++){
					mPriceTotal = mPriceTotal+ mList.get(i).getPrice()*
							Integer.valueOf(order.getOrderItems().get(i).getProduct_number());;
				}

				postData.getDate(PriceUtil.floatToString(mPriceTotal),msg);
			}

			@Override
			public void onError(String error) {
				postData.error(error);
			}
		});
	}
}
