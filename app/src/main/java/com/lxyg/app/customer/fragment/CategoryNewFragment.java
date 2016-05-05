package com.lxyg.app.customer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.AreaSelectListActivity;
import com.lxyg.app.customer.activity.ProductDetailsActivity;
import com.lxyg.app.customer.activity.SearchActivity;
import com.lxyg.app.customer.adapter.ProductAdapter;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Brands;
import com.lxyg.app.customer.bean.Product;
import com.lxyg.app.customer.bean.Types;
import com.lxyg.app.customer.iface.AddrBase;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;
import dev.mirror.library.utils.DpUtil;

/**
 * Created by 王沛栋 on 2015/11/2.
 */
public class CategoryNewFragment extends BaseFragment {
    private RelativeLayout mViewSearch;
    private ImageView mImgScan;
    private ListView mListView1;
//    private GridView mGridView;
    private HorizontalScrollView mHView;
    private RadioGroup mRG;
    private PullToRefreshGridView mListProduct;
    private LinearLayout mViewLoading;
    private TextView mTvEmpty;
    private TextView mTvCar;

    @Override
    public int setLayoutId() {
        return R.layout.fragment_category_new;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //设置通知栏颜色
//        ImmersionModeUtils.setTranslucentStatus(getActivity(), R.color.main_red);
        setBack();
        mViewSearch = (RelativeLayout)view.findViewById(R.id.view_search);
        mViewSearch.setOnClickListener(this);

        mImgScan = (ImageView)view.findViewById(R.id.right_icon);
        mImgScan.setOnClickListener(this);

        mListView1 = (ListView)view.findViewById(R.id.listview1);

//        mGridView = (GridView)view.findViewById(R.id.gridview);

        mHView = (HorizontalScrollView)view.findViewById(R.id.s_view);
        mRG = (RadioGroup)view.findViewById(R.id.view_select);
        mListProduct = (PullToRefreshGridView)view.findViewById(R.id.list);
        mViewLoading = (LinearLayout)view.findViewById(R.id.loading);
        mTvEmpty = (TextView)view.findViewById(R.id.tv);
        mTvCar = (TextView)view.findViewById(R.id.tv_car);

        mListProduct.setMode(PullToRefreshBase.Mode.BOTH);
        mListProduct.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                mPageNo = mPageDefault;
                initProduct(mTypeId);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                mPageNo++;
                initProduct(mTypeId);
            }
        });
    }

    private String mData;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mTypes.size() != 0){
            outState.putParcelableArrayList(INTENT_ID, (ArrayList<? extends Parcelable>) mTypes);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mTypes.size() == 0){
            initView();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.right_icon:
                showProgressDialog("");
                startActivityForResult((new Intent(getActivity(),CaptureActivity.class)), 2001);
                cancelProgressDialog();
                break;
            case R.id.area_selector:
                startActivity(new Intent(getActivity(), AreaSelectListActivity.class));
                break;
            case R.id.view_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;

        }
    }



    private List<Types> mTypes = new ArrayList<Types>();

    private AddrAdapter<Types> mAdapterType;

    //实例化界面
    private void initView(){
        JSONObject jb = new JSONObject();
        try{
            jb.put("s_uid", AppContext.SHOP_ID);
        }catch (JSONException e){

        }

        mBaseHttpClient.postData(PRODUCT_TYPE, jb, new AppAjaxCallback.onRecevieDataListener<Types>() {
            @Override
            public void onReceiverData(final List<Types> data, String msg) {
                mTypes.clear();
                mTypes.addAll(data);

                if(mAdapterType == null){
                    mAdapterType = new AddrAdapter<Types>(getActivity(),mTypes,2);
                    mListView1.setAdapter(mAdapterType);
                }


                if (mTypes.size() == 0){
                    return;
                }
                //设置默认选择的内容
                mAdapterType.setSelectedItem(0);
                initBrand(data.get(0).getP_type_id());


                mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mAdapterType.setSelectedItem(position);
                        mAdapterType.notifyDataSetChanged();

                        initBrand(data.get(position).getP_type_id());

                    }
                });

            }

            @Override
            public void onReceiverError(String msg) {
                showToast(msg);
            }

            @Override
            public Class<Types> dataTypeClass() {
                return Types.class;
            }
        });


    }
    private int mTypeId;

    private List<Brands> mBrands = new ArrayList<Brands>();
    private void initBrand(String brandId){

        JSONObject jb2 = new JSONObject();
        try{
            jb2.put("s_uid", AppContext.SHOP_ID);
            jb2.put("type_id",brandId);
        }catch (JSONException e){

        }
        mBaseHttpClient.postData(PRODUCT_BRAND,jb2, new AppAjaxCallback.onRecevieDataListener<Brands>() {
            @Override
            public void onReceiverData(List<Brands> data, String msg) {
                mBrands.clear();
                mBrands.addAll(data);

                int typeLength = mBrands.size();
                final RadioButton[] rbs = new RadioButton[typeLength];
                mRG.removeAllViews();

                for(int i=0; i<typeLength; i++){
                    View v = getActivity().getLayoutInflater().inflate(R.layout.view_tab_rb, null);

                    RadioButton tempButton =(RadioButton)v.findViewById(R.id.rb1);
                    tempButton.setText(mBrands.get(i).getP_brand_name());
                    tempButton.setId(Integer.valueOf(mBrands.get(i).getP_brand_id()));
                    rbs[i] = tempButton;


                    mRG.addView(tempButton, DpUtil.dip2px(getActivity(), 80),  LinearLayout.LayoutParams.MATCH_PARENT);
                }

                mRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        mTypeId = checkedId;
                        initProduct(checkedId);
                    }
                });

                rbs[0].setChecked(true);


            }

            @Override
            public void onReceiverError(String msg) {

            }

            @Override
            public Class<Brands> dataTypeClass() {
                return Brands.class;
            }
        } );

    }

    private int mPageNo = 1;
    private int mPageDefault = 1;
    private List<Product> mList = new ArrayList<>();
    private ProductAdapter mAdapter;
    private void initProduct(int brandId){
        int mTypeId = brandId;

        JSONObject jb = new JSONObject();
        try {
            jb.put("typeId", 0);
            jb.put("brandId", mTypeId);
            jb.put("pg", mPageNo);
            jb.put("lat", AppContext.Latitude);
            jb.put("lng", AppContext.Longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mBaseHttpClient.postData(PRODUCT_LIST, jb, new AppAjaxCallback.onRecevieDataListener<Product>() {

            @Override
            public void onReceiverData(List<Product> data, String msg) {
                if (mPageNo == mPageDefault) {
                    mList.clear();
                }

                mList.addAll(data);
                if (mAdapter == null) {
                    mAdapter = new ProductAdapter(getActivity(), mList,mTvCar);
                    mListProduct.setAdapter(mAdapter);

                } else {
                    mAdapter.notifyDataSetChanged();
                }

                mViewLoading.setVisibility(View.GONE);
                mTvEmpty.setVisibility(View.GONE);

                mListProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(getActivity(),ProductDetailsActivity.class).putExtra(INTENT_ID,
                                mList.get(position).getProductId()));
//                        startActivity(new Intent(getActivity(), ProductListActivity.class).putExtra(
//                                Constants.INTENT_ID, Integer.valueOf(mBrands.get(position).getP_brand_id())));
                    }
                });

                mListProduct.onRefreshComplete();

            }

            @Override
            public void onReceiverError(String msg) {
                mViewLoading.setVisibility(View.GONE);
                mTvEmpty.setVisibility(View.VISIBLE);
            }

            @Override
            public Class<Product> dataTypeClass() {
                return Product.class;
            }
        });
    }

    public static class AddrAdapter<T extends AddrBase>extends DevListBaseAdapter<T> {

        private int selectedItem = -1;
        private int mType = 0;
        private Context mContext;

        public AddrAdapter(Context context, List<T> list) {
            super(context, list);
            this.mContext = context;
        }


        public AddrAdapter(Context context, List<T> list,int type) {
            super(context, list);
            this.mType = type;
            this.mContext = context;
        }

        @Override
        public View initView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                if (mType == 2) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_tv1, null);
                }else{
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_tv2, null);
                }

            }
            TextView tv = (TextView)convertView.findViewById(android.R.id.text1);


            if(mType == 2){
                if(position == selectedItem){
                    tv.setText(Html.fromHtml("<font color='red'>" + getItem(position).getAddrName().trim() + "</font>"));
                    convertView.setBackgroundResource(R.mipmap.ic_category_list_s);
                }else {
                    tv.setText(getItem(position).getAddrName());
                    convertView.setBackgroundResource(R.mipmap.ic_category_list_n);
                }
            }else{
                tv.setText(getItem(position).getAddrName());
            }

            return convertView;
        }

        public void setSelectedItem(int selectedItem){
            this.selectedItem = selectedItem;
        }

    }
}
