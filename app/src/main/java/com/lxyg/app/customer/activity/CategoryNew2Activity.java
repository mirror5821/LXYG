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
import com.lxyg.app.customer.adapter.ProductForCategoryAdapter;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Car;
import com.lxyg.app.customer.bean.Categorys;
import com.lxyg.app.customer.bean.Product;
import com.lxyg.app.customer.iface.AddrBase;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.view.CHorizontalScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;
import dev.mirror.library.utils.DpUtil;

/**
 * Created by 王沛栋 on 2015/12/7.
 */
public class CategoryNew2Activity extends BaseActivity{//BaseSwipeBackActivity  支持滑动销毁的acitivity
    private RelativeLayout mViewSearch;
    private ImageView mImgScan;
    private ListView mListView1;
    private CHorizontalScrollView mHView,mHView2;
    private RadioGroup mRG,mRG2;
    private PullToRefreshGridView mListProduct;
    private LinearLayout mViewLoading;
    private TextView mTvEmpty;
    private TextView mTvCar;
    private TextView mTvClock;
    private RelativeLayout mViewCar;

    private int mTypeId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_new);

        setBack();

        mTypeId = getIntent().getIntExtra(INTENT_ID,0);
        mViewSearch = (RelativeLayout)findViewById(R.id.view_search);
        mViewSearch.setOnClickListener(this);

        mImgScan = (ImageView)findViewById(R.id.right_icon);
        mImgScan.setOnClickListener(this);

        mListView1 = (ListView)findViewById(R.id.listview1);
        mTvClock = (TextView)findViewById(R.id.tv_time);

        mHView = (CHorizontalScrollView)findViewById(R.id.s_view);
        mRG = (RadioGroup)findViewById(R.id.view_select);

        mHView2 = (CHorizontalScrollView)findViewById(R.id.s_view2);
        mRG2 = (RadioGroup)findViewById(R.id.view_select2);

        mListProduct = (PullToRefreshGridView)findViewById(R.id.list);
        mViewLoading = (LinearLayout)findViewById(R.id.loading);
        mTvEmpty = (TextView)findViewById(R.id.tv);
        mTvEmpty.setOnClickListener(this);
        mTvCar = (TextView)findViewById(R.id.tv_car);
        mViewCar = (RelativeLayout)findViewById(R.id.view_car);
        mViewCar.setOnClickListener(this);

        mListProduct.setMode(PullToRefreshBase.Mode.BOTH);
        mListProduct.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                mPageNo = mPageDefault;
                initProduct(mBrandId);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                mPageNo++;
                initProduct(mBrandId);
            }
        });
        initShoppingCar();

        bind();
        loadWorkTime();

    }

    private void loadWorkTime(){
        JSONObject jb = new JSONObject();
        try{
            jb.put("s_uid",AppContext.SHOP_ID);
        }catch (JSONException e){

        }
        mBaseHttpClient.postData1(WORK_TIME, jb, new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {
                mTvClock.setText(data+"    \n店铺电话:"+AppContext.SERVICE_PHONE);
            }

            @Override
            public void onError(String msg) {
                mTvClock.setVisibility(View.GONE);
            }
        });
    }


    private AddrAdapter<Categorys> mAdapterType;
    private List<Categorys> mLists = new ArrayList<>();
    private int mCategoryId;//此id为一级分类id
    private void bind(){
        JSONObject jb = new JSONObject();
        try{
            jb.put("s_uid",AppContext.SHOP_ID);
        }catch (JSONException e){

        }
        //CATEGORY_ALL
        mBaseHttpClient.postData(CNEW, jb, new AppAjaxCallback.onRecevieDataListener<Categorys>() {
            @Override
            public void onReceiverData(List<Categorys> data, String msg) {
                mLists.clear();
                mLists.addAll(data);
                if (mLists.size() == 0) {
                    return;
                }
                if (mAdapterType == null) {
                    mAdapterType = new AddrAdapter<Categorys>(getActivity(), mLists, 2);
                    mListView1.setAdapter(mAdapterType);
                } else {
                    mAdapterType.notifyDataSetChanged();
                }

                mCategoryId = mLists.get(mTypeId).getId();

//                mTypeId = 0;
                mAdapterType.setSelectedItem(mTypeId);

                mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        mAdapterType.setSelectedItem(position);
                        mAdapterType.notifyDataSetChanged();

                        mCategoryId = mLists.get(position).getId();
                        mTypeId = position;
                        initCategory(mTypeId);

                    }
                });
                initCategory(mTypeId);
            }

            @Override
            public void onReceiverError(String msg) {

            }

            @Override
            public Class<Categorys> dataTypeClass() {
                return Categorys.class;
            }
        });

    }

    private int mCateId;
    private List<Categorys.cate> mCategory = new ArrayList<>();
    private void initCategory(final int position){
        mCategory.clear();
        mCategory.addAll(mLists.get(position).getTypes());

        int typeLength = mCategory.size();
        final RadioButton[] rbs = new RadioButton[typeLength];
        mRG.removeAllViews();
        for(int i=0; i<typeLength; i++){
            Categorys.cate c = mCategory.get(i);
            View v = getActivity().getLayoutInflater().inflate(R.layout.view_rb_category, null);

            RadioButton tempButton =(RadioButton)v.findViewById(R.id.rb1);
            tempButton.setText(c.getName());

            int id = position*10+i;
            tempButton.setId(id);
//            tempButton.setId(i);
            rbs[i] = tempButton;


            mRG.addView(tempButton, DpUtil.dip2px(getActivity(), 80),  LinearLayout.LayoutParams.MATCH_PARENT);
        }
        mRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedId = checkedId - position*10;
                mCateId = checkedId;
                initBrand(checkedId);
            }
        });

//        if(rbs.length>1){
//            rbs[1].setChecked(true);
//            rbs[0].setChecked(true);
//        }else{
//            rbs[0].setBackgroundResource(R.drawable.rb_category_bg);
//            rbs[0].setChecked(true);
//        }
        rbs[0].setChecked(true);

        mHView.scrollTo(0, 0);
    }


    private int mBrandId;
    private List<Categorys.cate.brand> mBrand = new ArrayList<>();
    private void initBrand(final int position){
        mBrand.clear();
        mBrand.addAll(mLists.get(mTypeId).getTypes().get(position).getBrands());

        int typeLength = mBrand.size();
        final RadioButton[] rbs = new RadioButton[typeLength];
        mRG2.removeAllViews();

        for(int i=0; i<typeLength; i++){
            Categorys.cate.brand b = mBrand.get(i);
            View v = getActivity().getLayoutInflater().inflate(R.layout.view_rb_category, null);

            RadioButton tempButton =(RadioButton)v.findViewById(R.id.rb1);
            if(i == 0){
                tempButton.setText("全部");
            }else{

                tempButton.setText(b.getName());
            }

            int id = mTypeId*100+position*10+b.getId();
            tempButton.setId(id);
//            tempButton.setId(b.getId());
            rbs[i] = tempButton;


            mRG2.addView(tempButton, DpUtil.dip2px(getActivity(), 80),  LinearLayout.LayoutParams.MATCH_PARENT);
        }

        mRG2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mPageNo = mPageDefault;
                checkedId = checkedId - position*10 - mTypeId*100;
                mBrandId = checkedId;
//                mBrandId = checkedId;
                mViewLoading.setVisibility(View.VISIBLE);
                initProduct(mBrandId);
            }
        });

        rbs[0].setChecked(true);
//        if(rbs.length>1){
//            rbs[1].setChecked(true);
//            rbs[0].setChecked(true);
//        }else{
//            mViewLoading.setVisibility(View.VISIBLE);
//            mPageNo = mPageDefault;
//            initProduct(rbs[0].getId());
//        }

        mHView2.scrollTo(0, 0);
    }


    private int mPageNo = 1;
    private int mPageDefault = 1;
    private List<Product> mList = new ArrayList<>();
    private ProductForCategoryAdapter mAdapter;
    private void initProduct(int brandId){
        JSONObject jb = new JSONObject();
        try {
            if(mCateId == 0){
                jb.put("catId",mCategoryId);
                jb.put("typeId",0);
                jb.put("brandId", 1);
            }else{
                if(brandId == 1){
                    jb.put("typeId",mLists.get(mTypeId).getTypes().get(mCateId).getId());
                    jb.put("brandId", brandId);
                }else{
                    jb.put("typeId", 0);
                    jb.put("brandId", brandId);
                }

            }

            jb.put("pg", mPageNo);
            jb.put("lat", AppContext.Latitude);
            jb.put("lng", AppContext.Longitude);
            jb.put("shopId", AppContext.SHOP_ID);


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
                    mAdapter = new ProductForCategoryAdapter(getActivity(), mList, mTvCar);
                    mListProduct.setAdapter(mAdapter);

                } else {
                    mAdapter.notifyDataSetChanged();
                }

                mViewLoading.setVisibility(View.GONE);
                mTvEmpty.setVisibility(View.GONE);

                mListProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(getActivity(), ProductDetailsActivity.class).putExtra(INTENT_ID,
                                mList.get(position).getProductId()));
//                        startActivity(new Intent(getActivity(), ProductListActivity.class).putExtra(
//                                Constants.INTENT_ID, Integer.valueOf(mBrands.get(position).getP_brand_id())));
                    }
                });

                mListProduct.onRefreshComplete();


//                mListProduct.addView(LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_shop,null),
//                        mList.size());

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


    @Override
    protected void onResume() {
        super.onResume();
        initShoppingCar();
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
            case R.id.tv:
                bind();
                break;

        }
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
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_tv, null);
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
