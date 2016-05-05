package com.lxyg.app.customer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.lxyg.app.customer.bean.Activitys;
import com.lxyg.app.customer.fragment.GuidePagerViewFragment;
import com.lxyg.app.customer.fragment.PagerViewFragment;

import java.util.List;

public class GuidePageViewAdapter extends FragmentStatePagerAdapter {
	private int [] mList;
	public GuidePageViewAdapter(FragmentManager fm, int [] mList) {
		super(fm);
		this.mList = mList;
	}
	

	@Override
	public Fragment getItem(int arg0) {
		GuidePagerViewFragment commentFragment = new GuidePagerViewFragment();
		return commentFragment.newInstance(arg0,mList[arg0]);
	}
	public int getItemPosition(int position) {
		return position;
	};
	@Override
	public int getCount() {
		return mList.length;
	}

	//后来添加的
	@Override
	public int getItemPosition(Object object) {
		return GuidePageViewAdapter.POSITION_NONE;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}
}
