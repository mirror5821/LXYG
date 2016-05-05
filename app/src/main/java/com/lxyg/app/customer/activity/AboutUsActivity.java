package com.lxyg.app.customer.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lxyg.app.customer.R;

import java.util.HashMap;
import java.util.Map;


public class AboutUsActivity extends BaseActivity {
	private WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		setBack();
		setTitleText("关于我们");

		init();
	}

	private void init(){
		Map<String, String> header = new HashMap<>();
		header.put("Host","www.rabbitpre.com");
		header.put("User-Agent","Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
		header.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		header.put("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		header.put("Accept-Encoding","gzip,deflate");
		header.put("Cookie","Hm_lvt_de7d8515aad4ac1c242b76b728589f5d=1451270994; Hm_lpvt_de7d8515aad4ac1c242b76b728589f5d=1451270994; Hm_lvt_9ad3eedcbfcad678357018dda8c8c602=1451270994; Hm_lpvt_9ad3eedcbfcad678357018dda8c8c602=1451270996; rp=1fjTmwwT");
		header.put("If-None-Match","W/\"T4erDaThr+IKywWrdaBL4Q==\"");
		header.put("Cache-Control","max-age=0");

		mWebView = (WebView) findViewById(R.id.wv);
		//WebView加载web资源
		//http://lxyg8.b0.upaiyun.com/web/about_us.html
		//http://www.rabbitpre.com/m/aUnrqUBmf
		mWebView.loadUrl("http://lxyg8.b0.upaiyun.com/web/about_us.html");
		//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				//返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
	}
}
/*
蜂域致力于成为幸福生活的建设者，帮您解决问题，提供贴心服务：
蜂域快送——免费送货，30分钟到家，帮您购买早餐、夜宵、食品饮料等。
小区公告——随时了解小区物业和居委会发布的信息。
号码通——帮您快速找到维修、订水、家政服务。
小团购——带您团购优质实用的商品。
业主论坛——帮您认识小区和周边的邻居们，开心交流。
二手市场——帮您在家门口轻松转让闲置物品。
代收快递——让您不再因为等待快递而困在家里。
工具租借——向您提供修理、搬家等工具的租借。
到家服务——帮您精选了上门做饭、专业洗衣等服务。
一句话说明：“蜂域”是小区生活必备神器，你的邻居都在用！
*/