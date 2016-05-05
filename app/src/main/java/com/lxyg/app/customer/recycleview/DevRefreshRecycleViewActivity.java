package com.lxyg.app.customer.recycleview;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.BaseActivity;

import java.util.List;

/**
 * Created by 王沛栋 on 2015/11/23.
 */
public abstract class DevRefreshRecycleViewActivity<T> extends BaseActivity
        implements PullToRefreshBase.OnRefreshListener2<RecyclerView> {

    public List<T> mList;
    public int mPageNo; //页码数
    public int mPageDefault = 1; //默认页码
    public DevRecycleViewAdapter<T> mAdapter;

    public View mViewLoading;
    public View mViewEmpty;
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


    }

    /**
     * 设置界面layout id
     * @return
     */
    public abstract int setLayoutId();

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

    }
}
