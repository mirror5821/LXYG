package dev.mirror.library.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import dev.mirror.library.R;
import dev.mirror.library.adapter.DevListBaseAdapter;

public abstract class DevBaseListFragment<T extends Parcelable> extends DevBaseFragment
	implements OnItemClickListener,OnRefreshListener2<ListView>{
	
	public List<T> mList;
	public int pageNo;
	public int mDefaultPage = 1;
	public DevListBaseAdapter<T> mAdapter;
	public View mLoadView;
	public View mEmptyView;
	public PullToRefreshListView mListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pageNo = mDefaultPage;
	}
	
	
	@SuppressLint("InflateParams") 
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(setLayoutId() == 0){
			return inflater.inflate(R.layout.base_fragment_list,null);
		}else{
			return inflater.inflate(setLayoutId(),null);
		}
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mLoadView = view.findViewById(R.id.loading);
		mEmptyView = view.findViewById(R.id.empty);
		mListView = (PullToRefreshListView)view.findViewById(R.id.list);
		
		mListView.setEmptyView(mEmptyView);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setMode(setRefreshMode());
		
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState != null){
			mList = savedInstanceState.getParcelableArrayList("data");
			pageNo = savedInstanceState.getInt("page");
		}
		if(mList == null || mList.isEmpty()){
			pageNo = mDefaultPage;
			loadData();
		}else{
			mAdapter = newAdapter();
			mListView.setAdapter(mAdapter);
			showList();
		}
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DevListBaseAdapter<T> newAdapter(){
		return new DevListBaseAdapter(getActivity(),mList) {

			@Override
			public int getItemViewType(int position) {
				return DevBaseListFragment.this.getItemViewType(position);
			}
			
			@Override
			public int getViewTypeCount() {
				return DevBaseListFragment.this.getViewTypeCount();
			}
			@Override
			public int getCount() {
				return  DevBaseListFragment.this.getCount();
			}

			@Override
			public View initView(int position, View convertView,
					ViewGroup parent) {
				return DevBaseListFragment.this.getView(position, convertView,
						parent);
			}
		};
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(!isEmpty(mList)){
			outState.putParcelableArrayList("data", (ArrayList)mList);
		}
		outState.putInt("page", pageNo);
	}
	
	public static <T> boolean isEmpty(List<T> list){
		return (list == null) ||(list.isEmpty());
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
