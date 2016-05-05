package com.lxyg.app.customer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Area;
import com.lxyg.app.customer.iface.AddrBase;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.SharePreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;
import dev.mirror.library.utils.UIHelper;

/**
 * Created by 王沛栋 on 2015/11/25.
 */
public class ShopSelectActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private ListView mListView;
    private EditText mET;
    private Button mBtnOpenShop,mBtnGH;

    private InputMethodManager imm;

    private int intoType = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_select1);
        intoType = getIntent().getExtras().getInt(INTENT_ID,0);

        if(intoType==0)
            setBack();

        setTitleText("选择店铺");
        setRightTitle("手动选择");

        mListView = (ListView)findViewById(R.id.list);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(mAdapterShops == null){
            Area.District.Shops s = new Area.District.Shops();
            s.setName("暂无法定位到您的位置,请手动选择!");
            mListShop.add(s);

            mAdapterShops = new AddrAdapter<>(getApplicationContext(),mListShop);
        }

        mListView.setAdapter(mAdapterShops);
        mListView.setOnItemClickListener(this);

        mET = (EditText)findViewById(R.id.et);
        mET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (EditorInfo.IME_ACTION_SEARCH == actionId) {
                    String str = v.getText().toString().trim();
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                    searchShop(str);


                }
                return false;
            }
        });

        mBtnOpenShop = (Button)findViewById(R.id.open_shop);
        mBtnOpenShop.setOnClickListener(this);

        mBtnGH = (Button)findViewById(R.id.btn_gh);
        mBtnGH.setOnClickListener(this);

        searchShopByLatLng();
    }

    private void searchShopByLatLng(){
        //lat:"113.4",lng:"34.7"
        JSONObject jb = new JSONObject();
        try{
            jb.put("lat", AppContext.Latitude);
            jb.put("lng", AppContext.Longitude);
        }catch(JSONException e){

        }

        mBaseHttpClient.postData(SHOP_LIST_BY_LAT_LNG, jb,
                new AppAjaxCallback.onRecevieDataListener<Area.District.Shops>() {
                    @Override
                    public void onReceiverData(List<Area.District.Shops> data, String msg) {
                        mListShop.clear();
                        mListShop.addAll(data);
                        mAdapterShops.notifyDataSetChanged();
                    }

                    @Override
                    public void onReceiverError(String msg) {
                        showToast(msg);
                    }

                    @Override
                    public Class<Area.District.Shops> dataTypeClass() {
                        return Area.District.Shops.class;
                    }
                });

    }

    private void searchShop(String str){
        //"name":"12",lat:"113.4",lng:"34.7"
        JSONObject jb = new JSONObject();
        try{
            jb.put("lat", AppContext.Latitude);
            jb.put("lng", AppContext.Longitude);
            jb.put("name", str);
        }catch(JSONException e){

        }

        mBaseHttpClient.postData(SHOP_LIST_BY_SEARCH, jb,
                new AppAjaxCallback.onRecevieDataListener<Area.District.Shops>() {
            @Override
            public void onReceiverData(List<Area.District.Shops> data, String msg) {
                mListShop.clear();
                mListShop.addAll(data);

                if(mListShop.size() == 0){
                    Area.District.Shops s = new Area.District.Shops();
                    s.setName("暂无此店铺,请手动选择已有店铺!");
                    mListShop.add(s);
                }

                mAdapterShops.notifyDataSetChanged();


            }

            @Override
            public void onReceiverError(String msg) {
                showToast(msg);
            }

            @Override
            public Class<Area.District.Shops> dataTypeClass() {
                return Area.District.Shops.class;
            }
        });
    }


    private List<Area.District.Shops> mListShop = new ArrayList<>();
    private AddrAdapter<Area.District.Shops> mAdapterShops;




    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.right_text:
                startActivityForResult(new Intent(ShopSelectActivity.this, AreaSelectListActivity.class).putExtra(INTENT_ID, 0), 3001);
                break;
            case R.id.open_shop:
                startActivity(new Intent(ShopSelectActivity.this,JoinUsActivity.class));
                break;
            case R.id.btn_gh:
                UIHelper.makePhoneCall(ShopSelectActivity.this,getResources().getString(R.string.gonghuo_phone));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = mListShop.get(0).getName();
        if(name.equals("暂无法定位到您的位置,请手动选择!")||name.equals("暂无此店铺,请手动选择已有店铺!")){
            startActivityForResult(new Intent(ShopSelectActivity.this, AreaSelectListActivity.class).putExtra(INTENT_ID, 0), 3001);
        }else{
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
                Intent i = new Intent(ShopSelectActivity.this,MainActivity.class);
                setResult(RESULT_OK, i);
            }else{
                startActivity(new Intent(ShopSelectActivity.this, MainActivity.class));
            }
            finish();
        }
    }


    public static class AddrAdapter<T extends AddrBase>extends DevListBaseAdapter<T> {

        private LayoutInflater mInflater;

        public AddrAdapter(Context context, List<T> list) {
            super(context, list);

            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View initView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.item_shop_select, null);
            }

            final TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
            tv.setText(getItem(position).getAddrName());


            return convertView;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode) {

                case 3001:

                    //根据入口不同  选择不同的返回
                    if(intoType == 0){
                        Intent i = new Intent(ShopSelectActivity.this,MainActivity.class);
                        setResult(RESULT_OK, i);
                    }else{
                        startActivity(new Intent(ShopSelectActivity.this, MainActivity.class));
                    }
                    finish();
                    break;
            }
        }
    }


}
