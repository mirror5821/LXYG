package com.lxyg.app.customer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;

import dev.mirror.library.view.ProgressWebView;


public class JoinUsActivity extends BaseActivity {
	private WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview_progress);
		setBack();
		setTitleText("我要开店");

		init();
	}

	private ProgressWebView webview;
	String url;
	private void init(){
		url = JION_US;
//        url = "https://www.facebook.com/login.php";

		//绑定控件
		webview = (ProgressWebView) findViewById(R.id.p_webview);

		//设置数据

		webview.getSettings().setJavaScriptEnabled(true);
		webview.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				if (url != null && url.startsWith("http://"))
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			}
		});

		webview.loadUrl(url);
	}
}
