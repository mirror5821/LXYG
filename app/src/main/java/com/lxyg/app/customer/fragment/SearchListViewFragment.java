package com.lxyg.app.customer.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.ProductDetailsActivity;
import com.lxyg.app.customer.adapter.SearchListAdapter;
import com.lxyg.app.customer.adapter.SearchRecommedAdapter;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Product;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;
import com.lxyg.app.customer.utils.DividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by 王沛栋 on 2015/10/30.
 */
public class SearchListViewFragment extends BaseFragment {
    @Override
    public int setLayoutId() {
        return R.layout.fragment_search_list;
    }

    private RecyclerView mRecycleView;
    private TextView mTvNoProduct;

    private SearchListAdapter mAdapter;


    private AnimationDrawable animationDrawable;

    public SearchListViewFragment newInstance(String searchStr){
        SearchListViewFragment fragment = new SearchListViewFragment();
        Bundle args = new Bundle();
        args.putString("data", searchStr);
//        args.putInt();
        fragment.setArguments(args);

        return fragment;
    }

    private String mSearchWord;
    private int mType=0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mSearchWord = getArguments().getString("data");
    }

    private ImageView mImgLoading;
    private RelativeLayout mViewLoading;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchWord = getArguments().getString("data");
        mType = getArguments().getInt("type");

        JSONObject jb = new JSONObject();
        try {
            jb.put("s_uid", AppContext.SHOP_ID);
            if(mType == 1){
                jb.put("txm_code",mSearchWord);
            }else{
                jb.put("p_name",mSearchWord);
            }


        }catch (JSONException e){

        }

        mTvNoProduct = (TextView)view.findViewById(R.id.tv_no_product);
        mViewLoading = (RelativeLayout)view.findViewById(R.id.layout_loading);
        mImgLoading = (ImageView) view.findViewById(R.id.img_loading);
        animationDrawable = (AnimationDrawable) mImgLoading.getDrawable();
        animationDrawable.start();

        mRecycleView = (RecyclerView)view.findViewById(R.id.rView);

        // 创建一个线性布局管理器
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(mLayoutManager);

        mRecycleView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mBaseHttpClient.postData(SEARCH_LIST, jb, new AppAjaxCallback.onRecevieDataListener<Product>() {
            @Override
            public void onReceiverData(List<Product> data, String msg) {

                mAdapter = new SearchListAdapter(getActivity(),data);
                mRecycleView.setAdapter(mAdapter);


                animationDrawable.stop();
                mViewLoading.setVisibility(View.GONE);

                mAdapter.setOnItemClickListener(new SearchListAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, String data) {
                        startActivity(new Intent(getActivity(), ProductDetailsActivity.class).putExtra(INTENT_ID,
                                data));
                    }
                });

                if(data.size() == 0){
                   mTvNoProduct.setVisibility(View.VISIBLE);
                }else
                    mTvNoProduct.setVisibility(View.GONE);
            }

            @Override
            public void onReceiverError(String msg) {

            }

            @Override
            public Class<Product> dataTypeClass() {
                return Product.class;
            }
        });

    }
}
