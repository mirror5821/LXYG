package com.lxyg.app.customer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Area;
import com.lxyg.app.customer.bean.City;
import com.lxyg.app.customer.iface.AddrBase;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;
import com.lxyg.app.customer.utils.AreaUtil;
import com.lxyg.app.customer.utils.SharePreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;

/**
 * Created by 王沛栋 on 2015/10/29.
 */
public class AreaSelectListActivity extends BaseActivity{
    private static Context mContext;
    private ListView mListView1,mListView2,mListView3;
    private LinearLayout mViewLoading;
    private TextView mTvEmpty;

    //设置入口
    private int intoType = 0;

    private List<City> mCitys;

    private int mAreaId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_select);
        intoType = getIntent().getExtras().getInt(INTENT_ID,0);
        if(intoType==0)
            setBack();
        setTitleText("请选择便利店");

        mContext = getApplicationContext();
        mCitys = AreaUtil.getCityList(getApplicationContext());
        initView();
    }



    private AddrAdapter<City.Areas> mAdapterCity;
    private AddrAdapter<City.Areas.Direct> mAdapterArea;
    private List<City.Areas> mList1 = new ArrayList<>();
    private List<City.Areas.Direct> mList2 = new ArrayList<>();
    private List<Area.District.Shops> mListShop = new ArrayList<>();
    private AddrAdapter<Area.District.Shops> mAdapterShops;

    private void initView(){
        mListView1 = (ListView)findViewById(R.id.listview1);
        mListView2 = (ListView)findViewById(R.id.listview2);
        mListView3 = (ListView)findViewById(R.id.listview3);
        mViewLoading = (LinearLayout)findViewById(R.id.loading);
        mTvEmpty = (TextView)findViewById(R.id.tv_empty);

        if(mCitys == null){
            showToast("没有地址列表");
            return;
        }

        mList1 = sortCityList(mCitys.get(23));
        //河南省的code是70
        mAdapterCity = new AddrAdapter<City.Areas>(mContext,mList1);
        mListView1.setAdapter(mAdapterCity);

        mList2 = sortAreaList(mList1.get(0));

        mAdapterArea = new AddrAdapter<City.Areas.Direct>(mContext,mList2,2);
        mListView2.setAdapter(mAdapterArea);

        //设置默认区域id
        mAreaId = mList2.get(0).getCode();
        //加载默认数据
        getShopData();

        mAdapterCity.setSelectedItem(0);
        mListView1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //刷新城市列表
                mAdapterCity.setSelectedItem(position);
                mAdapterCity.notifyDataSetChanged();

                City.Areas a = mList1.get(position);

                //刷新小区列表
                mList2.clear();
                mList2.addAll(a.getArea());

                mAdapterArea.setSelectedItem(0);
                mAdapterArea.notifyDataSetChanged();

                //设置areaid
                mAreaId =a.getArea().get(0).getCode();
                getShopData();
            }
        });

        mAdapterArea.setSelectedItem(0);
        mListView2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //刷新小区列表
                mAdapterArea.setSelectedItem(position);
                mAdapterArea.notifyDataSetChanged();


                //设置areaid
                mAreaId =mList2.get(position).getCode();
                getShopData();

            }
        });

        mListView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String shopId = mListShop.get(position).getS_uid();

                //如果还是以前的店  则不做变更  如不是  则变更店面
                if(shopId.equals(AppContext.SHOP_ID)){
                    AppContext.SHOP_CHANGE = 0;
                }else{
                    AppContext.SHOP_CHANGE = 1;
                    AppContext.SHOP_ID = shopId;

                    SharePreferencesUtil.saveShopId(getApplicationContext(), AppContext.SHOP_ID);
                }

                //根据入口不同  选择不同的返回
                if(intoType == 0){
                    Intent i = new Intent(AreaSelectListActivity.this,MainActivity.class);
                    setResult(RESULT_OK, i);
                }else{
                    startActivity(new Intent(AreaSelectListActivity.this, MainActivity.class));
                }
                finish();
            }
        });



    }

    private void loading(){
        mViewLoading.setVisibility(View.VISIBLE);
        mTvEmpty.setVisibility(View.GONE);
    }

    private void loadOk(){
        mViewLoading.setVisibility(View.GONE);
        mTvEmpty.setVisibility(View.GONE);
    }
    private void loadError(){
        mViewLoading.setVisibility(View.GONE);
        mTvEmpty.setVisibility(View.VISIBLE);
    }


    private void getShopData(){
        loading();
        JSONObject jb = new JSONObject();
        try{
            jb.put("lat", AppContext.Latitude);
            jb.put("lng", AppContext.Longitude);
            jb.put("code",mAreaId);
            jb.put("pg",1);
        }catch(JSONException e){

        }

        mBaseHttpClient.postData(SHOP_LIST_BY_AREA, jb, new AppAjaxCallback.onRecevieDataListener<Area.District.Shops>() {
            @Override
            public void onReceiverData(List<Area.District.Shops> data, String msg) {
                mListShop.clear();
                mListShop.addAll(data);
                if(mAdapterShops == null){
                    mAdapterShops = new AddrAdapter<>(mContext,mListShop,3);
                    mListView3.setAdapter(mAdapterShops);
                }else{
                    mAdapterShops.notifyDataSetChanged();
                }
                if(mListShop.size() == 0){
                    loadError();
                }else{
                    loadOk();
                }
            }

            @Override
            public void onReceiverError(String msg) {
                loadError();
            }

            @Override
            public Class<Area.District.Shops> dataTypeClass() {
                return Area.District.Shops.class;
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
            if(convertView == null){
//                switch (mType){
//                    case 0:
//                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_tv1, null);
//                        break;
//                    case 2:
//                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_tv1, null);
//                        break;
//                    case 3:
//                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_tv2, null);
//                        break;
//                }

                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_tv1, null);
            }
            TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
            tv.setText(getItem(position).getAddrName());

            if(position == selectedItem){
                //convertView.setBackgroundColor(Color.BLUE);
                //convertView.setSelected(true);
//                convertView.setBackgroundResource(R.color.white);

                switch (mType){
                    case 0:
                        tv.setText(Html.fromHtml("<font color='red'>"+getItem(position).getAddrName()+"</font>"));
                        break;
                    case 2:
                        tv.setText(Html.fromHtml("<font color='red'>"+getItem(position).getAddrName()+"</font>"));
                        break;
                    case 3:
                        tv.setText(Html.fromHtml("<font color='blue'>"+getItem(position).getAddrName()+"</font>"));
                        break;
                }
            }else {
//                if (mType != 0) {
//                    convertView.setBackgroundResource(R.color.white);
//                }else{
//                    convertView.setBackgroundResource(R.color.main_bg);
//                }
            }

            return convertView;
        }

        public void setSelectedItem(int selectedItem){
            this.selectedItem = selectedItem;
        }

    }

    private List<City.Areas> mListSortCity;
    private List<City.Areas> sortCityList(City city){
        if(mListSortCity ==null) {
            mListSortCity = new ArrayList<>();
            for (int i = 0; i < city.getCity().size(); i++) {

                City.Areas a = city.getCity().get(i);
                if (a.getName().equals(AppContext.LOC_CITY)) {
                    mListSortCity.add(0, a);
                } else {
                    mListSortCity.add(a);
                }
            }
        }
        return mListSortCity;
    }


    private List<City.Areas.Direct> mListSortArea;
    private List<City.Areas.Direct> sortAreaList(City.Areas area){
        if(mListSortArea ==null) {
            mListSortArea = new ArrayList<>();
            for (int i = 0; i < area.getArea().size(); i++) {

                City.Areas.Direct a = area.getArea().get(i);
                if (a.getName().equals(AppContext.LOC_AREA)) {
                    mListSortArea.add(0, a);
                } else {
                    mListSortArea.add(a);
                }
            }
        }
        return mListSortArea;
    }
}
