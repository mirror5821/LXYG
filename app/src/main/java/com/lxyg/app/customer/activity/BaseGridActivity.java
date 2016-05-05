package com.lxyg.app.customer.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lxyg.app.customer.R;

import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;

public abstract class BaseGridActivity<T> extends BaseActivity implements OnItemClickListener,OnRefreshListener2<GridView> {
	public List<T> mList;
	public int pageNo;
	public int mDefaultPage = 1;
	public DevListBaseAdapter<T> mAdapter;
	public View mLoadView;
	public View mEmptyView;
	public PullToRefreshGridView mListView;
	private TextView mTvDes,mTvBtn;
	private LinearLayout mViewBuy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(setLayoutId() == 0){
			setContentView(R.layout.base_fragment_grid);
		}else{
			setContentView(setLayoutId());
		}

		mLoadView = findViewById(R.id.loading);
		mEmptyView = findViewById(R.id.view_ep);
		mListView = (PullToRefreshGridView)findViewById(R.id.list);
		mTvDes = (TextView)findViewById(R.id.tv_dsc);
		mTvBtn = (TextView)findViewById(R.id.tv_play);
		mViewBuy = (LinearLayout)findViewById(R.id.view1);
		mTvBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});

		mListView.setEmptyView(mEmptyView);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setMode(setRefreshMode());

		if(mList == null||mList.isEmpty()){
			pageNo = mDefaultPage;
			loadData();
		}else{
			mAdapter =newAdapter();
			mListView.setAdapter(mAdapter);
			showList();
		}

	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DevListBaseAdapter<T> newAdapter(){
		return new DevListBaseAdapter(getActivity(),mList) {

			@Override
			public int getItemViewType(int position) {
				return BaseGridActivity.this.getItemViewType(position);
			}

			@Override
			public int getViewTypeCount() {
				return BaseGridActivity.this.getViewTypeCount();
			}

			@Override
			public int getCount() {
				return BaseGridActivity.this.getCount();
			}

			@Override
			public View initView(int position, View convertView,
					ViewGroup parent) {
				return BaseGridActivity.this.getView(position, convertView,
						parent);
			}
		};

	}

	public int getViewTypeCount(){
		return 1;
	}

	public int getItemViewType(int position){
		return 0;
	}

	public int getCount(){
		return mList.size();
	}


	public void setListAdapter(){
		if(mList.size()==0){
			setLoadingFailed();
			mListView.onRefreshComplete();
			mAdapter.notifyDataSetChanged();
			return;
		}
		if(mAdapter == null){
			mAdapter = newAdapter();
			mListView.setAdapter(mAdapter);
		}else{
			mAdapter.notifyDataSetChanged();
		}
		showList();
		mListView.onRefreshComplete();

//		if(mAdapter == null){
//			mAdapter = newAdapter();
//			mListView.setAdapter(mAdapter);
//		}else{
//			mAdapter.notifyDataSetChanged();
//		}
//		showList();
//		mListView.onRefreshComplete();
	}

	public void showList(){
		mLoadView.setVisibility(View.GONE);
		mEmptyView.setVisibility(View.GONE);
	}


	public void setLoadingFailed(){
		mLoadView.setVisibility(View.GONE);
		mEmptyView.setVisibility(View.VISIBLE);
		mViewBuy.setVisibility(View.GONE);
//		mTvDes.setText();

	}
	/**
	 * 可重写此方法设置刷新方式
	 * @return
	 */
	public PullToRefreshBase.Mode setRefreshMode(){
		return PullToRefreshBase.Mode.BOTH;
	}



	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		pageNo = mDefaultPage;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		pageNo+=1;
		loadData();
	}

	/**
	 * 如无需修改 则默认传递值-2
	 * 如需修改  请在layout中include base_fragment_list 以便实现id的对应
	 * @return
	 */
	public abstract int setLayoutId();

	public abstract View getView(int position, View convertView, ViewGroup parent);
	public abstract void loadData();

	@Override
	public abstract void onItemClick(AdapterView<?> paramAdapterView, View view, int position, long num);

}
