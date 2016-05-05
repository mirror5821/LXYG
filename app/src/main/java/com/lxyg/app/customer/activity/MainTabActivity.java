package com.lxyg.app.customer.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.iface.OnSelectTab;
import com.lxyg.app.customer.iface.OnShoppingCar;

public abstract class MainTabActivity extends BaseActivity implements OnSelectTab,OnShoppingCar {
	public FragmentTabHost mFragmentTabHost;
	private String [] mTabTitles;
	private Class<? extends Fragment> [] mFragments;
	private int [] mTabIcons;
	public TextView [] mTvs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_activity_fragment_tab);

		setTabHost();
	}

	private void setTabHost(){
		mTabTitles = setTabTitles();
		mTabIcons = setTabIcons();
		mFragments = setFragment();
		mFragmentTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mFragmentTabHost.setup(this,getSupportFragmentManager(),android.R.id.tabcontent);
		int l = mTabTitles.length;
		
		mTvs = new TextView[l];
		for(int i = 0;i< l;i++){
			TabHost.TabSpec tab = mFragmentTabHost.newTabSpec(mTabTitles[i]);
			tab.setIndicator(getIndicatorView(i));
			mFragmentTabHost.addTab(tab,mFragments[i],setFragmentArgment(i));
		}
	}
	@SuppressLint("InflateParams") 
	public View getIndicatorView(int position){
		View v = getLayoutInflater().inflate(R.layout.tab_indicator, null);
		//dividing 可以设定分割线的颜色
		TextView tv = (TextView)v.findViewById(R.id.title);
		ImageView img = (ImageView)v.findViewById(R.id.icon);
		mTvs[position] = (TextView)v.findViewById(R.id.num);
		tv.setText(mTabTitles[position]);
		img.setImageResource(mTabIcons[position]);
		return v;
	}
	
	
	
	public abstract String [] setTabTitles();
	public abstract int [] setTabIcons();
	public abstract <T extends Fragment> Class<T> [] setFragment();
	public abstract Bundle setFragmentArgment(int position);

}
