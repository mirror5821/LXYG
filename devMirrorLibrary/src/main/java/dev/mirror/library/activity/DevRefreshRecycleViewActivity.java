package dev.mirror.library.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.List;

import dev.mirror.library.R;
import dev.mirror.library.view.recycleview.DevRecycleViewHolder;
import dev.mirror.library.view.recycleview.DevPullRefreshRecycleView;
import dev.mirror.library.view.recycleview.DevRecycleViewAdapter;


/**
 * Created by 王沛栋 on 2015/11/23.
 */
public abstract class DevRefreshRecycleViewActivity<T> extends DevBaseActivity
        implements PullToRefreshBase.OnRefreshListener2<RecyclerView> {

    public List<T> mList;
    public int mPageNo; //页码数
    public int mPageDefault = 1; //默认页码
    public DevRecycleViewAdapter<T> mAdapter;

    public View mViewLoading;
    public View mViewEmpty;//可以给他一个点击事件 用于重新加载数据
    public DevPullRefreshRecycleView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果没有设置  则使用默认layout
        if(setLayoutId() == 0){
            setContentView(R.layout.base_pull_refresh_recycleview);
        }else{
            setContentView(setLayoutId());
        }

        mViewLoading = findViewById(R.id.loading);
        mViewEmpty = findViewById(R.id.empty);
        mListView = (DevPullRefreshRecycleView)findViewById(R.id.list);
        mListView.setMode(setRefreshMode());
        mListView.getRefreshableView().setLayoutManager(setLayoutManager());
        mListView.setOnRefreshListener(this);

        if(mList == null|| mList.isEmpty()){
            mPageNo = mPageDefault;
            loadData();
        }else{
            setListAdapter();
        }

        LoadCompletion();
    }

    /**
     * 加载完成
     * 用于实现加载完成后的操作
     */
    public void LoadCompletion(){

    }

    /**
     * 加载完数据后调用该方法
     */
    public void setListAdapter(){
        if(mList == null || mList.isEmpty()){
            setLoadingFailed("暂无内容!");
            mListView.onRefreshComplete();
            return;
        }
        if(mList.size() == 0){
            setLoadingFailed("暂无内容!");
            mListView.onRefreshComplete();
            return;
        }

        mViewEmpty.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);

        if(mAdapter == null){
            mAdapter = newAdapter();
            mListView.getRefreshableView().setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
        //完成刷新
        mListView.onRefreshComplete();
    }

    public void setLoadingFailed(CharSequence msg){
        mViewLoading.setVisibility(View.GONE);
        mViewEmpty.setVisibility(View.VISIBLE);
        if(mViewEmpty instanceof TextView)
            ((TextView)mViewEmpty).setText(msg);
    }

    /**
     * 重写这个方法  设置显示模式
     * @return
     */
    public RecyclerView.LayoutManager setLayoutManager(){
        return new LinearLayoutManager(this);
    }

    /**
     * 重新此方法 设置刷新模式
     * @return
     */
    public PullToRefreshBase.Mode setRefreshMode(){
        return PullToRefreshBase.Mode.BOTH;
    }

    public DevRecycleViewAdapter<T> newAdapter(){
        return new DevRecycleViewAdapter<T>(getActivity(),mList,setItemLayoutId()) {
            @Override
            public void convert(DevRecycleViewHolder holder, T item,int position) {
                getView(holder,item,position);
            }
        };
    }

    public abstract void getView(DevRecycleViewHolder holder, T item,int position);

    /**
     * 设置界面layout id
     * @return
     */
    public abstract int setLayoutId();

    /**
     * 设置列表layout id
     * @return
     */
    public abstract int setItemLayoutId();

    /**
     * 加载数据
     */
    public abstract void loadData();

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        mPageNo = mPageDefault;
        loadData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        mPageNo +=1;
        loadData();
    }
}
