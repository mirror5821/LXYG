package com.lxyg.app.customer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.adapter.PageViewAdapter;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Activitys;
import com.lxyg.app.customer.bean.Car;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.utils.PriceUtil;

public class FBProductDetailsActivity extends BaseActivity {
	private WebView mWebView;
	private ViewPager mViewPager;
	private LinearLayout mViewPagerDot;
	private ImageView [] mImgDots;
	private TextView mTvName,mTvDes,mTvCash;
	private TextView mTvPrice;
	private EditText mEtNun;
	private Button mBtnPlus,mBtnReduction;
	private ImageButton mBtnSub;
	private ImageView mImgBack;

	private String mPid;
	private String mHtml;
	private List<Activitys> mListTopImg;
	private String mPrice;
	private FinalDb mDb;
	private List<Car> mCar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_details_webview);
		mImgBack = (ImageView)findViewById(R.id.left_icon);
		mImgBack.setOnClickListener(this);
		//添加title 右侧分享
		rightIconRedShare();
		mListTopImg = new ArrayList<Activitys>();
		mPid = getIntent().getExtras().getString(INTENT_ID);

		mWebView = (WebView)findViewById(R.id.webview);
		mViewPager = (ViewPager)findViewById(R.id.view_pager);
		mViewPagerDot = (LinearLayout)findViewById(R.id.view_select);
		mTvName = (TextView)findViewById(R.id.name);
		mTvPrice = (TextView)findViewById(R.id.price);
		mEtNun = (EditText)findViewById(R.id.num);
		mBtnPlus = (Button)findViewById(R.id.add);
		mBtnReduction = (Button)findViewById(R.id.jian);
		mBtnSub = (ImageButton)findViewById(R.id.sub);
		mTvDes = (TextView)findViewById(R.id.des);
		mTvCash = (TextView)findViewById(R.id.cash);

		mBtnPlus.setOnClickListener(this);
		mBtnReduction.setOnClickListener(this);
		mBtnSub.setOnClickListener(this);

		mEtNun.addTextChangedListener(new EtKeyListener());

		//先查询购物车
		mDb = AppContext.mDb;

		//先判断数据库是否有数据
//		mCar = new ArrayList<Car>();
		mCar= mDb.findAllByWhere(Car.class, "productId="+"'"+mPid+"'");
		if(mCar != null){
			if(mCar.size() >0){
				mEtNun.setText(mCar.get(0).getNum()+"");
			}
		}


		loadData();
	}

	/**
	 * 数目监听
	 * @author 
	 *
	 */
	public class EtKeyListener implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if(TextUtils.isEmpty(s)){
				return;
			}
			int i = Integer.valueOf(s.toString());
			if(i<1){
				mEtNun.setText("1");
				showToast("单品数量不能小于1");
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			if(TextUtils.isEmpty(s)){
				mEtNun.setText("1");
			}
		}


	}


	@Override
	public void onClick(View v) {
		super.onClick(v);
		int num = Integer.valueOf( mEtNun.getText().toString().trim());
		switch (v.getId()) {
		case R.id.left_icon:
			finish();
			break;
		case R.id.jian:
			if(num == 1){
				finish();
//				showToast("单品数量不能小于1");
//				num =1;
			}else{
				num = num -1;
			}
			mEtNun.setText(""+num);
			break;

		case R.id.add:
			num = num+1;
			mEtNun.setText(""+num);
			break;
		case R.id.sub:
			Car car = new Car();
			car.setProductId(mPid);
			
			int count = Integer.valueOf(mEtNun.getText().toString().trim());
			
			
//			b.findAllByWhere(Car.class, "productId=" + "'" + mPid+"'");
			//如果存在数据
			if(mCar.size()>0){
				//删除已经存在的数据
				mDb.deleteByWhere(Car.class, "productId=" + "'" + mPid + "'");
			}
			car.setPrice(mPrice);
			car.setNum(count);
			car.setIs_norm(PRODUCT_TYPE2);
			//保存数据
			AppContext.mDb.save(car);	
			showToast("已添加到购物车!");
			finish();
	        
			break;
		}
	}

	/**
	 * 登录
	 */
	private void loadData(){
		JSONObject jb = new JSONObject();
		try {
			jb.put("productId", mPid);
		} catch (JSONException e) {
			e.printStackTrace();

		}
		mBaseHttpClient.postData1(FB_PRODUCT_DETAILS, jb, new AppAjaxCallback.onResultListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onResult(String data, String msg) {
				System.out.println(data);
				JSONObject jData;
				try {
					jData = new JSONObject(data);
					//获取商品名称
					String name = jData.getString("name");
					//其他参数
					mTvName.setText(name);
					//定义标题
//					setTitleText(name);

					//描述
					String des = jData.getString("title");
					mTvDes.setText(des);
					//红包
					float cash = jData.getInt("cash_pay") / 100;
					mTvCash.setText(getResources().getString(R.string.keyong_hongbao) + cash);


					mPrice = jData.getString("price");
					String unit = jData.getString("p_unit_name");
//					mTvPrice.setText("￥"+mPrice+"/"+unit);
					int p = Integer.valueOf(mPrice);
					mTvPrice.setText("￥" + PriceUtil.floatToString(p));//+" "+unit

					mHtml = jData.getString("descripation");
					mListTopImg = JsonUtils.parseList(jData.getString("productImgs"), Activitys.class);
					addDot();
					mImgDots[0].setImageResource(R.mipmap.ic_dot_select);
					mViewPager.setAdapter(new PageViewAdapter(getSupportFragmentManager(),
							mListTopImg));

					mHandler.sendEmptyMessageDelayed(0, 2000);
					mViewPager.setOnPageChangeListener(new MyPageChangeListener());
					showWebView();
				} catch (JSONException e) {
					e.printStackTrace();
				}


			}

			@Override
			public void onError(String error) {
				showToast(error);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}

	private void addDot(){
		int l = mListTopImg.size();
		mImgDots = new ImageView[l];

		for(int i=0; i<l; i++)  
		{  
			View v = getLayoutInflater().inflate(R.layout.view_dot, null);
			mImgDots[i] = (ImageView)v.findViewById(R.id.dot_img);
			mViewPagerDot.addView(v, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
		} 

	}

	private boolean isRunning = true;  
	private int mPage = 0;
	private Handler mHandler = new Handler() {  
		public void handleMessage(android.os.Message msg) {  
			mPage = mViewPager.getCurrentItem();
			if(mPage == mListTopImg.size()-1){
				mPage = 0;
			}else{
				mPage = mPage+1;
			}
			mViewPager.setCurrentItem(mPage); 
			selectDot(mPage);

			if (isRunning) {  
				// 在发一个handler延时  
				mHandler.sendEmptyMessageDelayed(0, 2000);  
			}  

		};  
	};  
	private void selectDot(int page){
		for(int i=0;i<mListTopImg.size();i++){
			if(i ==page){
				mImgDots[i].setImageResource(R.mipmap.ic_dot_select);
			}else{
				mImgDots[i].setImageResource(R.mipmap.ic_dot_default);
			}
		}

	}
	public class MyPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int positon) {
			mPage = positon;
		}


	}

	private void showWebView()  
	{  
		// 设置WevView要显示的网页  
		mWebView.loadDataWithBaseURL(null, mHtml, "text/html", "utf-8",  
				null);  
		mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);  
		mWebView.getSettings().setJavaScriptEnabled(true); //设置支持Javascript   
		mWebView.requestFocus(); //触摸焦点起作用.如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。  

		// 点击链接由自己处理，而不是新开Android的系统browser响应该链接。   
		mWebView.setWebViewClient(new WebViewClient()  
		{  
			@Override  
			public boolean shouldOverrideUrlLoading(WebView view, String url)  
			{  
				//设置点击网页里面的链接还是在当前的webview里跳转    
				view.loadUrl(url);  
				return true;  
			}  
		});  
	}  

	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event)  
	{  
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack())  
		{  
			mWebView.goBack();//返回webView的上一页面      
			return true;  
		}  
		return super.onKeyDown(keyCode, event);  
	}  
}
