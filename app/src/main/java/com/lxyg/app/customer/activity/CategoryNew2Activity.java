package com.lxyg.app.customer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.lxyg.app.customer.adapter.ProductAdapter;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Brands;
import com.lxyg.app.customer.bean.Car;
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
 * Created by 王沛栋 on 2015/12/7.
 */
public class CategoryNew2Activity extends BaseActivity{
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
    private RelativeLayout mViewCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_category_new);

        setBack();

        mTypeId = getIntent().getIntExtra(INTENT_ID,0);
        mViewSearch = (RelativeLayout)findViewById(R.id.view_search);
        mViewSearch.setOnClickListener(this);

        mImgScan = (ImageView)findViewById(R.id.right_icon);
        mImgScan.setOnClickListener(this);

        mListView1 = (ListView)findViewById(R.id.listview1);

//        mGridView = (GridView)view.findViewById(R.id.gridview);

        mHView = (HorizontalScrollView)findViewById(R.id.s_view);
        mRG = (RadioGroup)findViewById(R.id.view_select);
        mListProduct = (PullToRefreshGridView)findViewById(R.id.list);
        mViewLoading = (LinearLayout)findViewById(R.id.loading);
        mTvEmpty = (TextView)findViewById(R.id.tv);
        mTvCar = (TextView)findViewById(R.id.tv_car);
        mViewCar = (RelativeLayout)findViewById(R.id.view_car);
        mViewCar.setOnClickListener(this);

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

        initShoppingCar();

        initView();
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
            case R.id.view_car://添加到购物车
                startActivity(new Intent(getActivity(), ShoppingCarActivity.class));
                break;

        }
    }



    private AppHttpClient mHttpClientType;
    private AppHttpClient mHttpClientBrand;
    private List<Types> mTypes = new ArrayList<Types>();

    private AddrAdapter<Types> mAdapterType;

    //实例化界面
    private void initView(){
        if(mHttpClientType == null){
            mHttpClientType = new AppHttpClient(PRODUCT_TYPE);
            mHttpClientBrand = new AppHttpClient(PRODUCT_BRAND);
        }
        JSONObject jb = new JSONObject();
        try{
            jb.put("s_uid", AppContext.SHOP_ID);
        }catch (JSONException e){

        }

        mHttpClientType.postData(PRODUCT_TYPE, new AppAjaxParam(jb), new AppAjaxCallback.onRecevieDataListener<Types>() {
            @Override
            public void onReceiverData(final List<Types> data, String msg) {
                mTypes.clear();
                mTypes.addAll(data);

                if (mAdapterType == null) {
                    mAdapterType = new AddrAdapter<Types>(getActivity(), mTypes, 2);
                    mListView1.setAdapter(mAdapterType);
                }


                if (mTypes.size() == 0) {
                    return;
                }

                if(mTypeId == 0) {
                    mTypeId = Integer.valueOf(mTypes.get(0).getP_type_id());
                    mAdapterType.setSelectedItem(0);
                }else{
                    for (int i=0;i<mTypes.size();i++){
                        if(mTypeId == Integer.valueOf(mTypes.get(i).getP_type_id())){
                            mAdapterType.setSelectedItem(i);
                            break;
                        }
                    }
                }

                initBrand(mTypeId+"");

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
        mHttpClientBrand.postData(PRODUCT_BRAND, new AppAjaxParam(jb2), new AppAjaxCallback.onRecevieDataListener<Brands>() {
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
    private AppHttpClient mHttpClientProduct;
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



        if(mHttpClientProduct == null){

            mHttpClientProduct = new AppHttpClient(PRODUCT_LIST);
        }
        mHttpClientProduct.postData(PRODUCT_LIST, new AppAjaxParam(jb), new AppAjaxCallback.onRecevieDataListener<Product>() {

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

    public void initShoppingCar(){
        List<Car> userList = AppContext.mDb.findAll(Car.class);//查询所有的用户
        if(userList != null){
            int i =userList.size();
            if(i == 0){
                mTvCar.setVisibility(View.GONE);
            }else {
                mTvCar.setVisibility(View.VISIBLE);
                mTvCar.setText(userList.size() + "");
            }
        }else{
            mTvCar.setVisibility(View.GONE);
        }
    }
}
