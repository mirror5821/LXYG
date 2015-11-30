package com.lxyg.app.customer.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lxyg.app.customer.R;


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
		mWebView = (WebView) findViewById(R.id.wv);
		//WebView加载web资源
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
