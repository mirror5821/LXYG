package com.lxyg.app.customer.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;

public abstract class BaseListActivity<T> extends BaseActivity implements OnItemClickListener,OnRefreshListener2<ListView> {
	public List<T> mList;
	public int pageNo;
	public int mDefaultPage = 1;
	public DevListBaseAdapter<T> mAdapter;
	public View mLoadView;
	public View mEmptyView;
	public PullToRefreshListView mListView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(setLayoutId() == 0){
			setContentView(dev.mirror.library.R.layout.base_fragment_list);
		}else{
			setContentView(setLayoutId());
		}

		mLoadView = findViewById(dev.mirror.library.R.id.loading);
		mEmptyView = findViewById(dev.mirror.library.R.id.empty);
		mListView = (PullToRefreshListView)findViewById(dev.mirror.library.R.id.list);

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
		LoadCompletion();
	}


	public void LoadCompletion(){

	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DevListBaseAdapter<T> newAdapter(){
		return new DevListBaseAdapter(getActivity(),mList) {

			@Override
			public int getItemViewType(int position) {
				return BaseListActivity.this.getItemViewType(position);
			}

			@Override
			public int getViewTypeCount() {
				return BaseListActivity.this.getViewTypeCount();
			}

			@Override
			public int getCount() {
				return BaseListActivity.this.getCount();
			}

			@Override
			public View initView(int position, View convertView,
					ViewGroup parent) {
				return BaseListActivity.this.getView(position, convertView,
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
			setLoadingFailed("未查询到任何数据\n下拉刷新数据");
			mListView.onRefreshComplete();
//			mAdapter.notifyDataSetChanged();
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
	}

	public void setListAdapterByCustomerMsg(String msg,View view){
		if(mList == null){
			setLoadingFailed(msg);
			mListView.setEmptyView(view);
			mListView.onRefreshComplete();
			return;
		}
		if(mList.size()==0){
			setLoadingFailed(msg);
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
	}

	public void showList(){
		mLoadView.setVisibility(View.GONE);
		mEmptyView.setVisibility(View.GONE);
	}

	public void setLoadingFailed(CharSequence msg){
		mLoadView.setVisibility(View.GONE);
		mEmptyView.setVisibility(View.VISIBLE);
		if(mEmptyView instanceof TextView)
			((TextView)mEmptyView).setText(msg);
	}
	/**
	 * 可重写此方法设置刷新方式
	 * @return
	 */
	public PullToRefreshBase.Mode setRefreshMode(){
		return PullToRefreshBase.Mode.BOTH;
	}



	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNo = mDefaultPage;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
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
