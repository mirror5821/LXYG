package com.lxyg.app.customer.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.History;
import com.lxyg.app.customer.fragment.SearchListViewNewFragment;
import com.lxyg.app.customer.fragment.SearchRecommedFragment;

/**
 * Created by 王沛栋 on 2015/10/30.
 */
public class SearchActivity extends BaseActivity{
    private ImageView mImgScan,mImgSearch;
    private EditText mET;
    private InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setBack();

        //扫描二维码
        mImgScan = (ImageView)findViewById(R.id.scan);
        mImgScan.setOnClickListener(this);
        mImgSearch = (ImageView)findViewById(R.id.search);
        mImgSearch.setOnClickListener(this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mET = (EditText)findViewById(R.id.et);
        mET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(EditorInfo.IME_ACTION_SEARCH == actionId){
                    bindSearch();
                }
                return false;
            }
        });

        initView();
    }

    private void bindSearch(){
        String str = mET.getText().toString().trim();
        imm.hideSoftInputFromWindow(mET.getWindowToken(), 0); //强制隐藏键盘
        initSearchView(str,0);

        bindHistoryRecord(str);
    }

    private void bindHistoryRecord(String search){

        //删除已经存在的数据
        AppContext.mDb.deleteByWhere(History.class, "name=" + "'" + search + "'");
        History h = new History();
        h.setName(search);

        //保存数据
        AppContext.mDb.save(h);
    }

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private void getInstance(){
        if(fragmentManager == null){
            fragmentManager = getSupportFragmentManager();
            transaction = fragmentManager.beginTransaction();
        }
    }
    private void initView(){
        getInstance();
        transaction.replace(R.id.fragment, new SearchRecommedFragment());
        transaction.commit();
    }

    public void initSearchView(String searchStr,int type){
        SearchListViewNewFragment fragment = new SearchListViewNewFragment();
//        SearchListViewFragment fragment = new SearchListViewFragment();
        Bundle args = new Bundle();
        args.putString("data", searchStr);
        args.putInt("type", type);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.scan:
                showProgressDialog("");
                startActivityForResult((new Intent(getActivity(),CaptureActivity.class)), 2001);
                cancelProgressDialog();
                break;
            case R.id.search:
                bindSearch();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode) {

                case 2001:
                    Uri uriData = data.getData();
                    initSearchView(uriData.toString(),1);
                    break;

            }
        }
    }
}
