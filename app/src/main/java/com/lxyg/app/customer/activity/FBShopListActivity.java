package com.lxyg.app.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.FBShop;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haha on 2015/9/15.
 */
public class FBShopListActivity extends BaseListActivity<FBShop> {
    @Override
    public int setLayoutId() {
        return R.layout.activity_order_list;
    }

    private LayoutInflater mInflater;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mListView.getRefreshableView().setDividerHeight(12);
//        mListView.getRefreshableView().setDivider(getResources().getDrawable(R.drawable.list_driver));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView =mInflater.inflate(R.layout.item_fb_shop, null);
        }

        ImageView img = (ImageView)convertView.findViewById(R.id.img);
        TextView name = (TextView)convertView.findViewById(R.id.name);
        TextView address = (TextView)convertView.findViewById(R.id.title);
        TextView title3 = (TextView)convertView.findViewById(R.id.price);


        FBShop s = mList.get(position);
        AppContext.displayImage(img, s.getCover_img());
        name.setText(s.getName());
        address.setText(s.getFull_address());
        title3.setText("配送数量:"+s.getSend_price());
        return convertView;
    }

    @Override
    public void loadData() {
        mId = getIntent().getStringExtra(INTENT_ID);
        if(mList == null) {
            mList = new ArrayList<>();
        }
        mInflater = getLayoutInflater();
        setBack();
        setTitleText("活动列表");

        JSONObject jb = new JSONObject();
        try{
            jb.put("s_uid",AppContext.SHOP_ID);
//            jb.put("lat", AppContext.Latitude);
//            jb.put("lng",AppContext.Longitude);
//            jb.put("pg",pageNo);
        }catch (JSONException e){
//            finish();
        }

        mBaseHttpClient.postData(FB_SHOP_LIST, jb, new AppAjaxCallback.onRecevieDataListener<FBShop>() {
            @Override
            public void onReceiverData(List<FBShop> data, String msg) {
                if(pageNo == mDefaultPage){
                    mList.clear();
                }

                mList.addAll(data);
                setListAdapter();
            }

            @Override
            public void onReceiverError(String msg) {
//                showToast(msg);
//                finish();
            }

            @Override
            public Class<FBShop> dataTypeClass() {
                return FBShop.class;
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> paramAdapterView, View view, int position, long num) {
        startActivity(new Intent(FBShopListActivity.this,FBShopProductListActivity.class).putExtra(INTENT_ID,mList.get(position-1).getFb_uid()));
    }
}
