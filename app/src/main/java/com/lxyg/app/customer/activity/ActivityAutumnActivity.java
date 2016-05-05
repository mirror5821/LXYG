package com.lxyg.app.customer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Autumn;
import com.lxyg.app.customer.utils.AppAjaxCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import dev.mirror.library.utils.JsonUtils;

/**
 * Created by mirror on 2015/9/7.
 * 活动默认的图片宽是1500 px
 */
public class ActivityAutumnActivity extends BaseActivity {
    private ImageView mView1,mView2,mView3,mView4,mView5,mView6,mView7,mView8;
    private TextView mTvNum;
    private Button mBtnCar;

    private int mScreenWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_autumn);
        setBack();
        setTitleText("中秋活动");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        mScreenWidth = dm.widthPixels;

        mTvNum = (TextView)findViewById(R.id.tv);
        mView1 = (ImageView)findViewById(R.id.view1);
        mView2 = (ImageView)findViewById(R.id.view2);
        mView3 = (ImageView)findViewById(R.id.view3);
        mView4 = (ImageView)findViewById(R.id.view4);
        mView5 = (ImageView)findViewById(R.id.view5);
        mView6 = (ImageView)findViewById(R.id.view6);
        mView7 = (ImageView)findViewById(R.id.view7);
        mView8 = (ImageView)findViewById(R.id.view8);
        mBtnCar = (Button)findViewById(R.id.btn_car);
        mBtnCar.setOnClickListener(this);

        mTvNum = (TextView)findViewById(R.id.tv);
        mTvNum.setVisibility(View.GONE);
        loadData();
    }
    private List<Autumn> mList = new ArrayList<Autumn>();;

    private List<Autumn> mListCar = new ArrayList<Autumn>();
    private void loadData(){
        JSONObject jb = new JSONObject();
        try {
            jb.put("activity_id", "92");
        }catch (JSONException e){

        }

        mBaseHttpClient.postData1(ACTIVITY_AUTUMN, jb, new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {
                mList.clear();
                mList.addAll(JsonUtils.parseList(data, Autumn.class));
                mView2.setOnClickListener(ActivityAutumnActivity.this);
                mView3.setOnClickListener(ActivityAutumnActivity.this);
                mView4.setOnClickListener(ActivityAutumnActivity.this);
                mView5.setOnClickListener(ActivityAutumnActivity.this);
                mView6.setOnClickListener(ActivityAutumnActivity.this);
                mView7.setOnClickListener(ActivityAutumnActivity.this);
                mView8.setOnClickListener(ActivityAutumnActivity.this);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private int mNum = 0;
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.view2:
                mListCar.add(mList.get(0));
                break;
            case R.id.view3:
                mListCar.add(mList.get(6));
                break;
            case R.id.view4:
                mListCar.add(mList.get(1));
                break;
            case R.id.view5:
                mListCar.add(mList.get(2));
                break;
            case R.id.view6:
                mListCar.add(mList.get(3));
                break;
            case R.id.view7:
                mListCar.add(mList.get(4));
                break;
            case R.id.view8:
                mListCar.add(mList.get(5));
                break;
            case R.id.btn_car:
                if(!AppContext.IS_LOGIN){
                    startActivityForResult(new Intent(ActivityAutumnActivity.this,
                            WechatAndPhoneLoginActivity.class), LOGIN_CODE1);
                    return;
                }

                //判断购物车是否为空
                if(removeDuplicateWithOrder(mListCar).size()>0)
                    startActivity(new Intent(ActivityAutumnActivity.this,
                            ActivityAutumnShoppingCarActivity.class).putExtra(INTENT_ID, JsonUtils.listToString(
                            removeDuplicateWithOrder(mListCar), Autumn.class)));
                else
                    showToast("您的购物车是空的");
                return;
        }
        mNum = removeDuplicateWithOrder(mListCar).size();
        mTvNum.setVisibility(View.VISIBLE);
        mTvNum.setText(mNum + "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode) {
                case LOGIN_CODE1:
                    //判断购物车是否为空
                    if(removeDuplicateWithOrder(mListCar).size()>0)
                        startActivity(new Intent(ActivityAutumnActivity.this, ActivityAutumnShoppingCarActivity.class).putExtra(INTENT_ID, JsonUtils.listToString(
                                removeDuplicateWithOrder(mListCar), Autumn.class)));
                    else
                        showToast("您的购物车是空的");

                    break;
            }
        }
    }

    /**
     * 删除重复的数据
     * @param list
     * @return
     */
    public static List removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        return newList;
    }
}
