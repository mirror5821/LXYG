package com.lxyg.app.customer.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.library.PayResult;
import com.alipay.sdk.pay.library.SignUtils;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.iface.AliPayListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AliPayUtil {
	//	//商户PID
	//	public static final String PARTNER = "2088111581933941";
	//	//商户收款账号
	//	public static final String SELLER = "service@caca9.com";
	//	//商户私钥，pkcs8格式
	//	public static final String RSA_PRIVATE ="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALSYQHDxFGNu9H1KFQqvJJvWRkwFATALATo+qDTl2uOS6FoeLm/aI1di16gjMd+2Ue87Nksq6Tj6LIcoDthE/+7/5Tl5pgeIKdsmEomaEezmT/TwHnb4A1qKqtxYE6ou3ctKRh+krBzlWSQ4hy7vgVcebSEduCipKAQr/h6t8YMNAgMBAAECgYAuAJJ5so9U/NB+kb5maFLU93IUrzmsOUFoyskiIGLvkV5qhkGh8FkMS3tPsxdjOxgH7ndYEmgyQVfSXdvaL0lZ9HshtjvK0hafLAXalI50WHS9nwRImCm3O66kZ0pYI2e4rOBREBa79OHSejdgu8JjevtxV1Mtk6yLHuifsyoaQQJBANpI48lVe9CGxz6nwfjPFSXHCF5YaXYOUQw4Tpx8poTZ/49c5jjydpHiBGohhPnB+ou45QjNfbA+/5PdpSFtP3UCQQDTzES89UmIsLTZQzzAUEr+r4k75mJ0s/7z6hJ45wpSwv1QWWo9oboFmS1sSXbBOUkLzfYsdond25KKwj+Gapo5AkAmpW8Gz0arhaEy+5qBBZDV7mYR5g9N4n0A79sjI4KL1NjFNgpgnLTXYS+s5qBXY7uLD0Ili9Qr5wtyz3QT9fadAkEAr0ksTnJ21EDiu7jw63zIRHZEjqWCMHHTfEzp0tPr8EjeWwRVcAKMMRvvwuId/ZaKhNGFfpDwUwPpCRvwdS9+yQJAKAc/+skkYvjfMZB0AbRMgqnLU5Pfrj4Du/VEabEbaIt7A4ihxm/kL0n1u3cn4bpO1XfRi/V6xES+UVv4ZxmfIA==";
	//	//支付宝公钥
	//	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	//2088021411321746
	//	pj@kaka51.com

	//商户PID 2088021993628295
//	public static final String PARTNER = "2088021411321746";
	public static final String PARTNER = "2088021993628295";
	//商户收款账号
	public static final String SELLER = "29424467@qq.com";
	//商户私钥，pkcs8格式
//	public static final String RSA_PRIVATE ="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALQcelyZ97hMVehCXx2yPJ1Jzek6h/ibMKBYZdQ0H0gJdV8oRM20eFr9bFEEpIzp665AdJtwXUh6k1tGUNbNN+KoghdGnSDHUkWL/k5+XmVzDc1TSo2CEa1dcUJpaG9EszLRExVxWbKey2Sb9dEaf4W6mmHJlzgltWy7GtNd8F2RAgMBAAECgYBr8f1r7mNHM4jxMIYtGBu38PnV5FyFpQ0ZrKEq8yokVHbh1xKVbqoLboMyc2PjiHbV8o9wcYGW5M/YDmZ4tnnc/fTJg2bCYL2IsqFTo7VzUh3YqEuEwoDXedSfF+6ktl0YrMVvSVyhY3rsoV/MGUFjhT17hnRkvJ0KQuDN4giNIQJBAOlimNgCruyNOjR7dchldndmEB32k4dMX57M4SPkyHQ3dgidfRS2YVBBkR3sBmwxsu5hXwH6QuxOMYCv8z1WZu0CQQDFkFtaVOcmO0csOIR/Ai6PFvopD4jLRdGXnSOtG37XdV6T8IEnY6yoSMeCzm61xsyCuSSKonN25GP2yJF0NPi1AkB6f70YDHiCQzKc33yYHeGLlC+7enQ/HPBHquQ1cd1hWscCiJ9hosT7Wh7wZLWOTpvLkaJLqG8+F2DWWgRDWrmVAkBmMWHPvBNFJ2yNZz7CI/LJeW01SB/OZfMgpdwKcyqNcNzXUZS9N0XcVqk89GM4C/lFtVc9jwKDAWjkAqOapLvxAkEA4GVzD/ydHasaxiAcBVm9jlItvQ4rvnF1B18jmlw3XsscFIC+o89ZuXd6q09jsimvP9LHlk1zLmhbYM70KhrGig==";
	//商户私钥  pkcs8格式
	public static final String RSA_PRIVATE ="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANfjl3nWbR0BH4nCA3Zr5FnXRMBLPdSWqraHa0SVDUUuNresE6KpLMSCzlP3yXXjBR5EjCW9huSoROVdy+Up0IQTwVJisjrnvvLc7xLEHdOoc52Ogq8lcHseKFaBntZknDYps6xBysvXtrQOCgwocHl8ngvAiciL2+ML150UeqUrAgMBAAECgYA6UClMKZUTbog8R4Fz7TSi57iUUD6FO6Uk5HMe9Tu+Yfs5pWswVo3XDpX4rEdoZketo0pPr1/OR31Ejw9R+PdvFmVI9cCgoN6APRoPWVn7MiiHr5AvWYhlLT+hmyZJk0By2ywJlNYUhCqGFCo1JESJFoxJubNq1u6qLti8XokIcQJBAPSQLM3AuozlbX9bRl6GFtlOkC2Y/nN16OwZGSZCbUVPctPLv2yRdbZ/212jHbG/FdkriUtlQRzDNdBpxWIpU8cCQQDh/CPGCDrF0hJX53Aj3BqxLyYT5FrpsFCS4gyUt6cyw89JT7hLq3K7NzgKB/yDc+haDI0SlaPjsX2/RuxUU1t9AkAuo3gH3OM2j2IsUWnACO4+jr7sEysjNa8vpzGmnDBecWJCha6Bs9Ow8/0PhFXbWcd+3NCX8j1SkN+oWSNtLthtAkEAzFq1/t5yR3EwJU2kmsjvWkrIpDRcAfbu5eSEe/eXutBXInR0s/jWR3YntuqB7l1iQAwZhjTLf5uBmvcHvzmiAQJASNGbhKQ8UeHwH9DZUSzX+y5JYHxZUVszD78ek+eJV+2+uxTRk3uOqUS/Un1JdeH42XhuZBg4XCKi8XMU5LoZEA==";



	/**
	 * call alipay sdk pay. 调用SDK支付
	 *
	 */
	public void pay(String name,final Activity activity,final AliPayListener listener,float price,String orderId) {

		final Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				PayResult payResult = new PayResult((String) msg.obj);


				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
//				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();


				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					listener.success("支付成功");
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						listener.waitForComfrim("支付结果确认中");
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						listener.error("支付失败");

					}
				}

			};
		};


		// 订单
		String orderInfo = getOrderInfo(name, "蜂域·智能社区商品", price+"",orderId);

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(activity);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.obj = result;
				mHandler.sendMessage(msg);


			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}




	/**
	 * create the order info. 创建订单信息
	 *
	 */
	public String getOrderInfo(String subject, String body, String price,String orderId) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
//		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
		orderInfo += "&out_trade_no=" + "\"" + orderId + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + Constants.ALIPAY_NOTIFY_URL
				+ "\"";

		//		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
		//				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 *
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;

	}

	/**
	 * sign the order info. 对订单信息进行签名
	 *
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 *
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
