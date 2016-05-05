package com.lxyg.app.customer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.ForumAddActivity;
import com.lxyg.app.customer.activity.MainActivity;
import com.lxyg.app.customer.activity.WechatAndPhoneLoginActivity;
import com.lxyg.app.customer.adapter.StaggeredLayoutRVAdapter;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Forum;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.ImmersionModeUtils;
import com.lxyg.app.customer.utils.SharePreferencesUtil;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 王沛栋 on 2015/10/30.
 */
public class ForumWaterfullFragment extends BaseFragment {
    @Override
    public int setLayoutId() {
        return R.layout.fragment_forum_new;
    }

    private UltimateRecyclerView mRecycleView;


    ArrayList<String> data = new ArrayList<>();


    private TextView mTvRight;
    private ImageView mImgLoading;
    private RelativeLayout mViewLoading;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionModeUtils.setTranslucentStatus(getActivity(), R.color.main_red);
        setTitleText("社区互动");
        mTvRight = initTextView(R.id.right_text);
        mTvRight.setText("发起话题");
        mViewLoading = (RelativeLayout)view.findViewById(R.id.view_loading);
        mRecycleView = (UltimateRecyclerView)view.findViewById(R.id.ultimate_recycler_view);
//        mRecycleView.setEmptyView(R.layout.view_empty);
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.onShopping(false);
        if(mAdapter == null){
            loadData();
        }
    }

    private StaggeredLayoutRVAdapter mAdapter;

    private AnimationDrawable animationDrawable;

    private List<Forum> mList;

    private int columns = 2;
    private int mPageNo = 1;

    private View mViewFooter;
    public void loadData() {
        JSONObject jb = new JSONObject();
        try {
            jb.put("pg",mPageNo);
        }catch (JSONException e){

        }
        mBaseHttpClient.postData(FORUM_LIST, jb, new AppAjaxCallback.onRecevieDataListener<Forum>() {
            @Override
            public void onReceiverData(List<Forum> data, String msg) {
                if(data.size() == 0){
                    mRecycleView.disableLoadmore();
                }else{
                    mRecycleView.enableLoadmore();
                }

                if (mList == null) {
                    mList = new ArrayList<Forum>();
                }



                if(mAdapter == null){
                    if(data.size()>0){
                        String id = data.get(0).getForm_id();
                        SharePreferencesUtil.getFourmId(getActivity(), id);

                    }

                    mList.addAll(data);

                    mAdapter = new StaggeredLayoutRVAdapter(mList,getActivity());
//                    mGridAdapter = new GridLayoutRVAdapter(mList);
//                    mGridLayoutManager = new BasicGridLayoutManager(getActivity(), columns, mGridAdapter);
//                    mRecycleView.setLayoutManager(mGridLayoutManager);

//                    animationDrawable = (AnimationDrawable) mImgLoading.getDrawable();
//                    animationDrawable.start();

                    mRecycleView.setLayoutManager(new StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL));
                    mRecycleView.setHasFixedSize(true);
                    mRecycleView.setSaveEnabled(true);
                    mRecycleView.setClipToPadding(false);
                    mRecycleView.setAdapter(mAdapter);
                    mRecycleView.enableLoadmore();
                    mViewFooter = LayoutInflater.from(getActivity()).inflate(R.layout.view_load_more, null);
                    mAdapter.setCustomLoadMoreView(mViewFooter);

                    mViewLoading.setVisibility(View.GONE);
                    // mGridAdapter.setCustomHeaderView(new UltimateRecyclerView.CustomRelativeWrapper(this));
//                    mRecycleView.setParallaxHeader(getActivity().getLayoutInflater().inflate(R.layout.view_load_header, mRecycleView, false));
                    mRecycleView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
                        @Override
                        public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                            mPageNo++;
                            loadData();
                        }
                    });

                    mRecycleView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            mPageNo = 1;
                            loadData();
                        }
                    });



                }else{
                    if(mPageNo == 1){
                        mList.clear();
                    }

                    mList.addAll(data);
                    mAdapter.notifyDataSetChanged();
                }


                mRecycleView.setRefreshing(false);
            }

            @Override
            public void onReceiverError(String msg) {
                mRecycleView.setRefreshing(false);
            }

            @Override
            public Class<Forum> dataTypeClass() {
                return Forum.class;
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.right_text:
                if(!AppContext.IS_LOGIN) {
                    startActivityForResult(new Intent(getActivity(), WechatAndPhoneLoginActivity.class), LOGIN_CODE1);
                }else{
                    startActivityForResult(new Intent(getActivity(), ForumAddActivity.class), FORUM_ADD_CODE);
                }

                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode) {
                case LOGIN_CODE1:
                    startActivity(new Intent(getActivity(), ForumAddActivity.class));
                    break;
                case FORUM_ADD_CODE:
                    //重新加载数据
                    mPageNo = 1;
                    loadData();
                    break;
            }
        }
    }
}
