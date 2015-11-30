package dev.mirror.library.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import dev.mirror.library.R;
import dev.mirror.library.adapter.DevListBaseAdapter;

public abstract class DevBaseGridActivity<T> extends DevBaseActivity implements OnItemClickListener,OnRefreshListener2<GridView> {
	public List<T> mList;
	public int pageNo;
	public int mDefaultPage = 1;
	public DevListBaseAdapter<T> mAdapter;
	public View mLoadView;
	public View mEmptyView;
	public PullToRefreshGridView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(setLayoutId() == 0){
			setContentView(R.layout.base_fragment_grid);
		}else{
			setContentView(setLayoutId());
		}
		
		mLoadView = findViewById(R.id.loading);
		mEmptyView = findViewById(R.id.empty);
		mListView = (PullToRefreshGridView)findViewById(R.id.list);
		
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
				return DevBaseGridActivity.this.getItemViewType(position);
			}
			
			@Override
			public int getViewTypeCount() {
				return DevBaseGridActivity.this.getViewTypeCount();
			}
			
			@Override
			public int getCount() {
				return DevBaseGridActivity.this.getCount();
			}

			@Override
			public View initView(int position, View convertView,
					ViewGroup parent) {
				return DevBaseGridActivity.this.getView(position, convertView,
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
