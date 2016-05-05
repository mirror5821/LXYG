package com.lxyg.app.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.bean.Sign;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import dev.mirror.library.utils.DateUtil;
import dev.mirror.library.utils.JsonUtils;

/**
 * Created by 王沛栋 on 2015/12/3.
 */
public class SignActivity extends BaseActivity {
    private CalendarPickerView mPicker;

    private TextView mTv;
    private Button mBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        setBack();
        setTitleText("每日签到");
        setRightTitle("兑换规则");

        mTv = (TextView)findViewById(R.id.tv);
        mBtn = (Button)findViewById(R.id.btn_sign);
        mBtn.setOnClickListener(this);

        mPicker = (CalendarPickerView) findViewById(R.id.calendar_view);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);
        //为啥必须实例化 我擦
        mPicker.init(lastYear.getTime(), nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.SINGLE).withSelectedDate(new Date());

        loadData();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_sign:
                sign();
                break;
            case R.id.right_text:
                startActivity(new Intent(SignActivity.this,ActivityImgActivity.class));
                break;
        }
    }

    private void sign(){
        mBaseHttpClient.postData1(SIGN, new JSONObject(), new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {
                showToast("签到成功!");
                finish();
            }

            @Override
            public void onError(String error) {
                showToast(error);
            }
        });
    }

    private List<Date> mDate;
    private Sign mSign;
    private void loadData(){
        mBaseHttpClient.postData1(SIGN_HISTORY, new JSONObject(), new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {

                mSign = JsonUtils.parse(data,Sign.class);

                final Calendar nextYear = Calendar.getInstance();
                nextYear.add(Calendar.YEAR, 1);

                final Calendar lastYear = Calendar.getInstance();
                lastYear.add(Calendar.YEAR, -1);



                mDate = new ArrayList<Date>();

                for (int i =0;i<mSign.getTimes().size();i++){
                    mDate.add(DateUtil.strToDateLong(mSign.getTimes().get(i).getcTime()));
                }

                mPicker.setDecorators(Collections.<CalendarCellDecorator>emptyList());

                mPicker.init(lastYear.getTime(), nextYear.getTime())
                        .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
                        .withSelectedDates(mDate).displayOnly();
                mPicker.selectDate(mDate.get(mDate.size() - 1));


                String html = "累计签到: <font color='red'>" +mSign.getRecord().getZqd_num()+"</font> 天, " +
                        "连续签到: <font color='red'>"+mSign.getRecord().getLxqd_num()+"</font> 天,<font color='red'>共 "
                        +mSign.getRecord().getJf_num()+" </font>个蜜罐";
                mTv.setText(Html.fromHtml(html));

                List<Sign.Times> timesList = mSign.getTimes();
                if(!timesList.isEmpty()|| timesList != null){
                    int s = timesList.size();
                    if(s>0){
                        if((timesList.get(s-1).getcTime()).substring(0,10).equals(mSign.getDate().substring(0,10))){
                            mBtn.setText("已经签到");
                            mBtn.setEnabled(false);
                        }
                    }
                }

                mPicker.setScrollY(0);

            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
