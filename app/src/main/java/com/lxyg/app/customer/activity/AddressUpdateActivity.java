package com.lxyg.app.customer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Address;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.mirror.library.utils.JsonUtils;

/**
 * Created by mirror on 2015/8/28.
 */
public class AddressUpdateActivity extends BaseActivity{
    private EditText mEtName,mEtPhone,mEtStreet;
    private TextView mTvMap;


    private String mAddressId;
    private String name;
    private String phone;
    private double lat;
    private double lng;
    private String street;
    private String provinceName;
    private String cityName;
    private String areaName;


    //通过此id判断入口来源并做返回使用
    private int mIntentId;
    private Address mAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);

        setBack();
        setTitleText("地址修改");

        mIntentId = getIntent().getExtras().getInt(INTENT_ID);
        mAddress = JsonUtils.parse(getIntent().getStringExtra("bean"), Address.class);

        mTvMap = initTextView(R.id.map);
        mEtName = initEditText(R.id.name);
        mEtPhone = initEditText(R.id.phone);
        mEtStreet = initEditText(R.id.street);

        mEtName.setText(mAddress.getName());

        mAddressId = mAddress.getAddressId();
        name = mAddress.getName();
        phone = mAddress.getPhone();
        lat = mAddress.getLat();
        lng = mAddress.getLng();
        street = mAddress.getStreet();
        provinceName = mAddress.getProvince_name();
        cityName = mAddress.getCity_name();
        areaName = mAddress.getArea_name();


        mEtStreet.setText(street);
        mTvMap.setText(mAddress.getProvince_name() + mAddress.getCity_name() + mAddress.getArea_name());
        mEtPhone.setText(phone);

        initButton(R.id.sub);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.map:
                startActivityForResult(new Intent(AddressUpdateActivity.this,MapSelectActivity.class), MAP_REQUESTCODE);
                break;

            case R.id.sub:

                name = mEtName.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    showToast("请输入收货人姓名!");
                    return;
                }

                //监测手机号码
                phone = mEtPhone.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    showToast("请输入电话号码!");
                    return;
                }

                //貌似有缓存 放到Appcontext死活不起作用
                Pattern p = Pattern.compile("^0?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$");
                Matcher m = p.matcher(phone);

                if(!m.matches()){
                    showToast("请输入正确的手机号码");
                    return;
                }

                if(TextUtils.isEmpty(provinceName)){
                    showToast("请在地图上选择店铺位置!");
                    return;
                }
                street = mEtStreet.getText().toString().trim();
                if(TextUtils.isEmpty(street)){
                    showToast("请输入店铺详细地址!");
                    return;
                }
                sub();
                break;
        }

    }

    private void sub(){
        JSONObject jb = new JSONObject();
        try {
            jb.put("name", name);
            jb.put("phone", phone);
            jb.put("fulladdress", provinceName+cityName+areaName+street);
            jb.put("street", street);
            jb.put("provinceName", provinceName);
            jb.put("cityName", cityName);
            jb.put("areaName", areaName);
            jb.put("uid", AppContext.USER_ID);
            jb.put("lat", lat);
            jb.put("lng", lng);
            jb.put("addressId",mAddressId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mBaseHttpClient.postData1(UPDATA_ADDRESS,jb, new AppAjaxCallback.onResultListener() {

            @Override
            public void onResult(String data, String msg) {
                showToast(msg);

                Intent i =null;

                //1 订单确认界面进入 2地址列表界面进入
                switch (mIntentId) {
                    case 1:
                        i = new Intent(AddressUpdateActivity.this,OrderMakeActivity.class);
                        break;
                    case 2:
                        i = new Intent(AddressUpdateActivity.this,AddressListActivity.class);

                }
                Address a = JsonUtils.parse(data, Address.class);
//				i.putExtra(ADDRESS_ID, a.getAddressId());
//				i.putExtra(NAME, a.getName());
//				i.putExtra(PHONE,a.getPhone());
//				i.putExtra(DISTRICT, a.getProvince_name()+a.getCity_name()+a.getArea_name());
//				i.putExtra(ADDRESS, a.getStreet());
                i.putExtra(ADR, a);

                setResult(RESULT_OK, i);
                finish();
            }

            @Override
            public void onError(String error) {
                showToast(error);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode) {
                case MAP_REQUESTCODE:
                    Bundle mBundle = data.getExtras();
                    provinceName = mBundle.getString(PROVINCE);
                    cityName = mBundle.getString(CITY);
                    areaName = mBundle.getString(DISTRICT);
                    street = mBundle.getString(ADDRESS);
                    lat = mBundle.getDouble(LAT);
                    lng = mBundle.getDouble(LNG);

                    mEtStreet.setText(street);
                    mTvMap.setText(provinceName+" "+cityName+" "+areaName);
                    break;
            }
        }
    }
}
