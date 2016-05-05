package com.lxyg.app.customer.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.SearchActivity;
import com.lxyg.app.customer.adapter.SearchRecommedAdapter;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.History;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 王沛栋 on 2015/10/30.
 */
public class SearchRecommedFragment extends BaseFragment {
    @Override
    public int setLayoutId() {
        return R.layout.fragment_search_recommed;
    }

    private RecyclerView mRecycleView;
    private LinearLayout mView;

    private SearchRecommedAdapter mAdapter;

    private SearchActivity mActivitySearch;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecycleView = (RecyclerView)view.findViewById(R.id.rView);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        mRecycleView.setLayoutManager(mLayoutManager);

        if(mActivitySearch == null){
            mActivitySearch = (SearchActivity) getActivity();
        }

        String [] datas = {"火腿","香蕉","百事","劲酒","旺旺仙贝","老白干","长城干红","红旗渠"};

        mAdapter = new SearchRecommedAdapter(getActivity(),datas);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new SearchRecommedAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {

                mActivitySearch.initSearchView(data.toString(),0);
            }
        });


        mView = (LinearLayout)view.findViewById(R.id.view_history);

        List<History> mHistorys = new ArrayList<History>();
        mHistorys = AppContext.mDb.findAll(History.class);

        if (mHistorys.size()>0){
            View dView = getActivity().getLayoutInflater().inflate(R.layout.view_search_history_tv,null);
            TextView dTv = (TextView)dView.findViewById(R.id.tv_history);

            dTv.setText("您的搜索记录:");

            mView.addView(dView);

            for (int i=0;i<mHistorys.size();i++){
                final String str = mHistorys.get(i).getName();
                View v = getActivity().getLayoutInflater().inflate(R.layout.view_search_history_tv,null);
                TextView tv = (TextView)v.findViewById(R.id.tv_history);

                tv.setText(str);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivitySearch.initSearchView(str,0);
                    }
                });

                mView.addView(v);
            }
        }else{
            View v = getActivity().getLayoutInflater().inflate(R.layout.view_search_history_tv,null);
            TextView tv = (TextView)v.findViewById(R.id.tv_history);

            tv.setText("暂无搜索记录!");

            mView.addView(v);
        }



    }
}
