package com.lxyg.app.customer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.lxyg.app.customer.fragment.OrderListFragment;


public class OrderPagerAdapter extends FragmentStatePagerAdapter {
	private int [] mOrderStatus;
	public OrderPagerAdapter(FragmentManager fm, int [] orderStatus) {
		super(fm);
		mOrderStatus = orderStatus;
	}
	

	@Override
	public Fragment getItem(int arg0) {
		OrderListFragment commentFragment = new OrderListFragment();
		return commentFragment.newInstance(mOrderStatus[arg0]);
	}
	public int getItemPosition(int position) {
		return position;
	};
	@Override
	public int getCount() {
		return mOrderStatus.length;
	}

	//后来添加的
	@Override
	public int getItemPosition(Object object) {
		return OrderPagerAdapter.POSITION_NONE;
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
