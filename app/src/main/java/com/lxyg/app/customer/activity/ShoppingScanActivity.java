package com.lxyg.app.customer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.lxyg.app.customer.R;
import com.lxyg.app.customer.adapter.ShoppingScanAdapter;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Product;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王沛栋 on 2015/12/4.
 */
public class ShoppingScanActivity extends BaseActivity {
    private TextView mTvEmpty,mTvPrice;
    private ListView mListView;
    private EditText mETSM;

    private List<Product> mList = new ArrayList<>();
    private String mIntentStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_scan);
        setBack();
        setTitleText("扫描添加购物车");
//        setRightTitle("扫描");
        mTvEmpty = (TextView)findViewById(R.id.tv);
        mListView = (ListView)findViewById(R.id.list);
        mTvPrice = (TextView)findViewById(R.id.price_all);

        mIntentStr = getIntent().getExtras().getString(INTENT_ID);
        mIntentStr = "6917246013203";

        mETSM = (EditText)findViewById(R.id.et_sm);
        mETSM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("fuck--------------"+count);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                loadData(s.toString());
//                loadData("6917246013203");
            }
        });

        loadData(mIntentStr);

    }

    private ShoppingScanAdapter mAdapter;
    private void loadData(String txm){
        mTvEmpty.setVisibility(View.GONE);
        JSONObject jb = new JSONObject();
        try {
            jb.put("s_uid", AppContext.SHOP_ID);
            jb.put("txm_code",txm);



        }catch (JSONException e){

        }
        mBaseHttpClient.postData(SEARCH_LIST, jb, new AppAjaxCallback.onRecevieDataListener<Product>() {
            @Override
            public void onReceiverData(List<Product> data, String msg) {
                mList.addAll(data);

                if(mAdapter == null){
                    mAdapter = new ShoppingScanAdapter(getApplicationContext(),mList,mTvPrice);

                    mListView.setAdapter(mAdapter);
                }else{
                    mAdapter.notifyDataSetChanged();
                }

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
    private void initView(){

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.right_text:
                startActivityForResult((new Intent(getActivity(),CaptureActivity.class)), 2001);
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode) {

                case 2001:
                    Uri uriData = data.getData();
                    //获取扫描结果
                    loadData(uriData.toString());

                    break;

            }
        }
    }



}
