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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.AreaSelectListActivity;
import com.lxyg.app.customer.activity.ProductListActivity;
import com.lxyg.app.customer.activity.SearchActivity;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Brands;
import com.lxyg.app.customer.bean.Constants;
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

/**
 * Created by 王沛栋 on 2015/11/2.
 */
public class CategoryFragment extends BaseFragment {
    private RelativeLayout mViewSearch;
    private ImageView mImgScan;
    private ListView mListView1;
    private GridView mGridView;

    @Override
    public int setLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //设置通知栏颜色
//        ImmersionModeUtils.setTranslucentStatus(getActivity(), R.color.main_red);

        mViewSearch = (RelativeLayout)view.findViewById(R.id.view_search);
        mViewSearch.setOnClickListener(this);

        mImgScan = (ImageView)view.findViewById(R.id.right_icon);
        mImgScan.setOnClickListener(this);

        mListView1 = (ListView)view.findViewById(R.id.listview1);

        mGridView = (GridView)view.findViewById(R.id.gridview);
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

    private AddrAdapter<Brands> mAdapterBrand;
    private List<Brands> mBrands = new ArrayList<Brands>();
    private void initBrand(String brandId){

        JSONObject jb2 = new JSONObject();
        try{
            jb2.put("s_uid", AppContext.SHOP_ID);
            jb2.put("type_id",brandId);
        }catch (JSONException e){

        }
        mBaseHttpClient.postData(PRODUCT_BRAND, jb2, new AppAjaxCallback.onRecevieDataListener<Brands>() {
            @Override
            public void onReceiverData(List<Brands> data, String msg) {
                mBrands.clear();
                mBrands.addAll(data);
                if(mAdapterBrand == null){
                    mAdapterBrand = new AddrAdapter<Brands>(getActivity(),mBrands);

                    mGridView.setAdapter(mAdapterBrand);

                }

                mAdapterBrand.notifyDataSetChanged();

                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(getActivity(), ProductListActivity.class).putExtra(
                                Constants.INTENT_ID, Integer.valueOf(mBrands.get(position).getP_brand_id())));
                    }
                });
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
