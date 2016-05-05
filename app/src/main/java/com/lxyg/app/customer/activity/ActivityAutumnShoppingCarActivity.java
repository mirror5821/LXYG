package com.lxyg.app.customer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Ac;
import com.lxyg.app.customer.bean.Address;
import com.lxyg.app.customer.bean.Car;
import com.lxyg.app.customer.bean.Service;
import com.lxyg.app.customer.bean.WxPay;
import com.lxyg.app.customer.iface.AliPayListener;
import com.lxyg.app.customer.utils.AliPayUtil;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;
import dev.mirror.library.utils.DateUtil;
import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.utils.PriceUtil;
import dev.mirror.library.view.NoScrollListView;
import dev.mirror.library.view.WheelView;

/**
 * Created by mirror on 2015/9/8.
 * 中秋节活动购物车
 */
public class ActivityAutumnShoppingCarActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer,Boolean> mIsSelected;
    //控制是否点击编辑购物车按钮
    private boolean isEdit = false;

    private List<Car> mCarList;
    private boolean hasDefaultAddress = false;
    private List<Address> mListAddress;
    private String mAddressId;

    private TextView mTvRight;
    private LinearLayout mView1,mView2;
    private CheckBox mCbAll;
    public View mLoadView;
    public View mEmptyView;
    private NoScrollListView mListView;
    private TextView mTvPriceAll;

    private TextView mTvAddressInfo;
    private LinearLayout mViewAddressTv;
    private TextView mTvAddress,mTvDistrict,mTvNameAndPhone;
    private RadioButton mRb21;
    private RadioGroup mRg2;
    private Button mBtnBuy;
    private EditText mEtMarker;
    private RadioGroup mRg1;
    private LinearLayout mViewTime;
    private TextView mTvTime;

    private int mPayTyep=1;//1 微信支付 2支付宝支付 3货到付款

    private List<Ac> mList = new ArrayList<Ac>();
    private ShoppingCarAdapter mAdapter;

    private int mPriceTotal;
    private String mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autumn_shopping_car);
//        mTvRight = initTextView(R.id.right_text);
//        mTvRight.setText("编辑");
//        setTitleText("购物车");
        setTitleText("订单确认");
        setBack();

        if(mIsSelected == null){
            mIsSelected = new HashMap<Integer,Boolean>();
        }
        //地址使用的view
        mListAddress = new ArrayList<Address>();
        mTvAddressInfo = initTextView(R.id.tv_address);
        mViewAddressTv = (LinearLayout)findViewById(R.id.view1);
        mViewAddressTv.setOnClickListener(this);
        mTvAddress = initTextView(R.id.address);
        mTvDistrict = initTextView(R.id.district);
        mTvNameAndPhone = initTextView(R.id.name_phone);
        loadAddressData();

        //购买

        //支付方式
        mRg2 = (RadioGroup)findViewById(R.id.rg2);
        mRg2.setOnCheckedChangeListener(this);
        mRb21 = (RadioButton)findViewById(R.id.rb21);
        //默认选中
        mRb21.setChecked(true);
        mBtnBuy = (Button)findViewById(R.id.btn_buy1);
        mBtnBuy.setOnClickListener(this);

        //购物车使用的view
        mIsSelected.clear();
        mList.clear();

        //选择配送时间
        mRg1 = (RadioGroup)findViewById(R.id.rg1);
        mViewTime = initLinearLayout(R.id.view_time);
        mRg1.setOnCheckedChangeListener(this);
        mTvTime = initTextView(R.id.tv_time);

        mIntent = getIntent().getStringExtra("activity_id");
        mList.addAll(JsonUtils.parseList(getIntent().getStringExtra(INTENT_ID), Ac.class));

        mView1 = initLinearLayout(R.id.view1);
        mView2 = initLinearLayout(R.id.view2);

        mCbAll = (CheckBox)findViewById(R.id.cb_all);
        initButton(R.id.btn_delete);
        initButton(R.id.btn_buy1);
        initButton(R.id.btn_buy2);
        mEtMarker = (EditText)findViewById(R.id.et_marker);

        mLoadView = findViewById(R.id.loading);
        mEmptyView = findViewById(R.id.empty);
        mListView = (NoScrollListView)findViewById(R.id.list);
        mTvPriceAll = initTextView(R.id.price_all);

        mCbAll.setOnClickListener(this);
        mAdapter = new ShoppingCarAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);
        loadTime();
        loadTotalPrice();
        showList();

    }


    private Address mAddress;

    /**
     * 加载地址数据
     */
    private void loadAddressData(){
        //先网络请求默认地址
        JSONObject jb = new JSONObject();

        mBaseHttpClient.postData(ADDRESS_LIST, jb,new AppAjaxCallback.onRecevieDataListener<Address>() {

            @Override
            public void onReceiverData(List<Address> data, String msg) {
                if(data.size()==0){
                    emptyAddress();
                }else{
                    hasDefaultAddress = true;
                    mListAddress.addAll(data);
                    mAddress = data.get(0);
                    initAddress();
                }
            }

            @Override
            public void onReceiverError(String msg) {
                showToast(msg);
            }

            @Override
            public Class<Address> dataTypeClass() {
                return Address.class;
            }
        });
    }


    private  void initAddress(){
        mAddressId = mAddress.getAddressId();
        mTvAddress.setText(mAddress.getStreet());
        mTvDistrict.setText(mAddress.getProvince_name() + mAddress.getCity_name() + mAddress.getArea_name());
        mTvNameAndPhone.setText(mAddress.getName()+" "+mAddress.getPhone());

        mTvAddress.setOnClickListener(this);
        mTvDistrict.setOnClickListener(this);
        mTvNameAndPhone.setOnClickListener(this);
    }

    /**
     * 设置地址为空时的现实
     */
    private void emptyAddress(){
        mTvAddressInfo.setText("请添加收货地址");
        mViewAddressTv.setVisibility(View.GONE);
    }

    private void loadTotalPrice(){
        int p = 0;
        for(Ac a:mList){
            p = p + a.getPrice()*a.getNum();
        }
        mPriceTotal = p;
        String html = "订单总额:<font color='red'>"+ PriceUtil.floatToString(mPriceTotal)+"</font>元";
        mTvPriceAll.setText(Html.fromHtml(html));


        //购物车为空 则退出
        if(mList.size()==0){
            finish();
        }
    }
    /**
     *
     * 适配器
     *
     */
    public class ShoppingCarAdapter extends DevListBaseAdapter<Ac> {

        private final LayoutInflater mInflater;
        private final List<Ac> mList;
        public ShoppingCarAdapter(Context context, List<Ac> list) {
            super(context, list);
            mInflater = LayoutInflater.from(context);
            mList = list;
        }

        @Override
        public View initView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.item_shopping_car, null);
            }

            TextView name = (TextView) ViewHolder.get(convertView, R.id.name);
            TextView price = (TextView) ViewHolder.get(convertView, R.id.price);
            ImageView img = (ImageView) ViewHolder.get(convertView, R.id.img);
            final CheckBox cb = (CheckBox) ViewHolder.get(convertView, R.id.cb);
            final EditText et = (EditText) ViewHolder.get(convertView, R.id.num);
            Button jian = (Button) ViewHolder.get(convertView, R.id.jian);
            Button jia = (Button) ViewHolder.get(convertView, R.id.add);

            cb.setChecked(mIsSelected.get(position)==null?false:mIsSelected.get(position));
            if(isEdit){
                cb.setVisibility(View.VISIBLE);
            }else{
                cb.setVisibility(View.GONE);
            }

            cb.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!cb.isChecked()) {
                        mCbAll.setChecked(false);
                    }
                    mIsSelected.put(position, cb.isChecked());
                }
            });

            final Ac g = mList.get(position);
            //名称
            name.setText(g.getName());
            //列表图
            AppContext.displayImage(img, g.getCover_img());
            //价格
            final int p = g.getPrice();

//            //显示价格
            price.setText("单价:￥"+ PriceUtil.floatToString(p));
            //数量默认是1
            et.setText(g.getNum()+"");

            jian.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int i = Integer.valueOf(et.getText().toString().trim());
                    if(i == 1){
                        mList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        //更新价格
                        loadTotalPrice();
                        return;
                    }else{
                        i = i -1;
                    }
                    et.setText(""+i);
                    mList.get(position).setNum(i);
                    loadTotalPrice();

                }
            });
            jia.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int i = Integer.valueOf(et.getText().toString().trim());
                    i = i+1;
                    et.setText(""+i);
                    g.setNum(i);
                    loadTotalPrice();

                }
            });

            return convertView;
        }
    }

    /**
     * 展示列表
     */
    public void showList(){
        mLoadView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_address:
                //如果有 则进入地址列表
                if(hasDefaultAddress){
                    startActivityForResult(new Intent(ActivityAutumnShoppingCarActivity.this,AddressListActivity.class).
                                    putParcelableArrayListExtra(INTENT_ID, (ArrayList<? extends Parcelable>) mListAddress),
                            ADDRESS_SELECT_REQUESTCODE);
                }else{//如果没有 则进入添加地址
                    startActivityForResult(new Intent(ActivityAutumnShoppingCarActivity.this,AddressAddActivity.class).putExtra(INTENT_ID, 1),
                            ADDRESS_ADD_REQUESTCODE);
                }
                break;
            //修改收货地址
            case R.id.view1:
                startActivityForResult(new Intent(ActivityAutumnShoppingCarActivity.this, AddressUpdateActivity.class).putExtra(INTENT_ID, 1).putExtra("bean", JsonUtils.beanToString(mAddress, Address.class)), ADDRESS_UPDATE);
                break;
            case R.id.address:
                startActivityForResult(new Intent(ActivityAutumnShoppingCarActivity.this, AddressUpdateActivity.class).putExtra(INTENT_ID, 1).putExtra("bean", JsonUtils.beanToString(mAddress, Address.class)), ADDRESS_UPDATE);
                break;
            case R.id.district:
                startActivityForResult(new Intent(ActivityAutumnShoppingCarActivity.this, AddressUpdateActivity.class).putExtra(INTENT_ID, 1).putExtra("bean", JsonUtils.beanToString(mAddress, Address.class)), ADDRESS_UPDATE);
                break;
            case R.id.name_phone:
                startActivityForResult(new Intent(ActivityAutumnShoppingCarActivity.this, AddressUpdateActivity.class).putExtra(INTENT_ID, 1).putExtra("bean", JsonUtils.beanToString(mAddress, Address.class)), ADDRESS_UPDATE);
                break;
            case R.id.btn_buy1:
                sub();
                break;
        }
    }

    private String mOrderId;
    //提交订单
    private void sub(){

        if(TextUtils.isEmpty(mAddressId)){
            showToast("请编辑收获地址");
            return;
        }

        showProgressDialog("正在提交数据");
        JSONObject jb = new JSONObject();
        try {
            jb.put("activity_id", mIntent);
            jb.put("price", mPriceTotal);
            jb.put("address_id", mAddressId);
            jb.put("pay_type",mPayTyep);
            jb.put("s_uid",AppContext.SHOP_ID);
            jb.put("remark",mEtMarker.getText().toString());


            JSONArray ja = new JSONArray();
            for(int i=0; i<mList.size();i++) {
                JSONObject jbCar = new JSONObject();

                Ac car = mList.get(i);
                jbCar.put("productId", car.getProductId());
                jbCar.put("productNum", car.getNum());
                jbCar.put("cashPay", "0");
                jbCar.put("p_brand_id", "0");
                jbCar.put("productPrice", car.getPrice());
                jbCar.put("productPay", car.getPrice());

                ja.put(jbCar);

            }

            jb.put("orderItems", ja);
            jb.put("sendType", mSendType);

            if(mSendType == 2){
                String send = mSendDate+" "+mSendHour;
                send = send.substring(0, send.lastIndexOf("-"));

                jb.put("sendTime", DateUtil.Time2Unix(send + ":00"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        mBaseHttpClient.postData1(ACTIVITY_AUTUMN_BUY, jb, new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {

                cancelProgressDialog();
                try {
                    mOrderId = new JSONObject(data).getString("order_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (mPayTyep == 3) {
                    showToast(msg);
                    //清除购物车
                    AppContext.mDb.deleteAll(Car.class);
                    finish();
                    return;

                }

                if (mPayTyep == 1) {
                    wePay();
                    return;
                }


                new AliPayUtil().pay("商品支付", getActivity(), new AliPayListener() {

                    @Override
                    public void waitForComfrim(String msg) {
                        showToast(msg);
                    }

                    @Override
                    public void success(String msg) {
                        showToast(msg);
                        finish();
//                        payOk();
                    }

                    @Override
                    public void error(String msg) {
                        showToast(msg);
                    }
                }, Float.valueOf(PriceUtil.floatToString(mPriceTotal)), mOrderId);
            }

            @Override
            public void onError(String error) {
                showToast(error);
                cancelProgressDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isPay){
            isPay = false;
            if(AppContext.IS_PAY_OK){
                showToast("支付成功!");
                finish();
//                payOk();
            }else{
                showToast("支付失败");
            }

        }

    }

    private boolean isPay = false;
    private PayReq mReq;
    private WxPay mWxPay;
    private void wePay(){
        JSONObject jb = new JSONObject();
        try {
            jb.put("orderId", mOrderId);
            jb.put("type",2);
            jb.put("ip", "192.168.0.158");
            //			jb.put("ip", IpUtil.getLocalIpAddress());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mBaseHttpClient.postData1(WX_PAY, jb, new AppAjaxCallback.onResultListener() {

            @Override
            public void onResult(String data, String msg) {
                mWxPay = JsonUtils.parse(data, WxPay.class);

                if(mReq == null){
                    mReq = new PayReq();
                }

                mReq.appId = mWxPay.getAppid();
                mReq.partnerId = mWxPay.getPartnerid();// "1263785401";//mWxPay.MCH_ID;
                mReq.prepayId = mWxPay.getPrepayid();
                mReq.packageValue = "Sign=WXPay";
                mReq.nonceStr =  mWxPay.getNoncestr();
                mReq.timeStamp = mWxPay.getTimestamp();
                mReq.sign = mWxPay.getSign();

                IWXAPI WXapi= AppContext.WX_API;
                isPay = true;
                WXapi.sendReq(mReq);
            }

            @Override
            public void onError(String error) {
            }
        });
    }
    private void payOk(){
        JSONObject jb = new JSONObject();
        try {
            jb.put("order_id", mOrderId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mBaseHttpClient.postData1(ACTIVITY_AUTUMN_BUY_OK, jb, new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {
                showToast(msg);
                finish();
            }

            @Override
            public void onError(String error) {
                showToast(error);
            }
        });
    }


    private int mSendType;
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {

            case R.id.rg2:
                switch (checkedId) {
                    case R.id.rb21:
                        mPayTyep = 1;
                        break;

                    case R.id.rb22:
                        mPayTyep = 2;
                        break;
                    case R.id.rb23:
                        mPayTyep = 3;
                        break;
                }
                break;
            case R.id.rg1:
                switch (checkedId) {
                    case R.id.rb11:
                        mSendType = 1;
                        mViewTime.setVisibility(View.GONE);
                        break;

                    case R.id.rb12:
                        mSendType = 2;
                        showTimeDialog();
                        mViewTime.setVisibility(View.VISIBLE);
                        break;
                }
                break;

        }
    }


    private Service mService;
    /**
     * 加载服务器时间
     */
    private void loadTime(){
//
//        x.http().post(new AppAjaxParam(CURRENT_TIME, Constants.HOST_HEADER+CURRENT_TIME),
//                new Callback.CommonCallback<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        cancelProgressDialog();
//                        try {
//                            System.out.println("starttime---------"+result);
//                            JSONObject j = new JSONObject(result);
//                            mService = new Service();
//                            JSONObject jb = new JSONObject(j.getString("data"));
//                            System.out.println("starttime---------"+jb.toString());
////					String data = jb.getString("data");
//                            String str = jb.toString();
//
//                            mService.setStartTime(str.substring(str.indexOf(":")+1,str.indexOf(",")).replace("\"", ""));
//                            mService.setCurrentTime(str.substring(str.indexOf("currentTime") + 13, str.indexOf("endTime") - 2).replace("\"", ""));
//                            mService.setEndTime(str.substring(str.indexOf("endTime") + 10, str.lastIndexOf("}") - 1).replace("\"", ""));
//
//                            initSendTime();
//                            initSendTime();
//
//                        }catch (JSONException e){
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable ex, boolean isOnCallback) {
//                        showToast(ex.getLocalizedMessage());
//                        cancelProgressDialog();
//                        finish();
//                    }
//
//                    @Override
//                    public void onCancelled(CancelledException cex) {
//
//                    }
//
//                    @Override
//                    public void onFinished() {
//
//                    }
//                });


        mBaseHttpClient.postData1(SYSTEM_TIME_UNIX, new JSONObject(), new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {


                cancelProgressDialog();
                try {
                    JSONObject jb = new JSONObject(data);

                    if (mService == null) {
                        mService = new Service();
                    }
                    mService.setStartTime(DateUtil.TimeStamp2Date(jb.getString("startTime"), "yyyy-MM-dd HH:mm:ss"));
                    mService.setCurrentTime(DateUtil.TimeStamp2Date(jb.getString("currentTime"), "yyyy-MM-dd HH:mm:ss"));
                    mService.setEndTime(DateUtil.TimeStamp2Date(jb.getString("endTime"), "yyyy-MM-dd HH:mm:ss"));
                } catch (JSONException e) {
                }
                initSendTime();
                initSendTime();
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
                cancelProgressDialog();
//				finish();
            }
        });

//        NetUtil.loadData(CURRENT_TIME, new JSONObject(), new PostDataListener() {
//
//            @Override
//            public void getDate(String data, String msg) {
//                cancelProgressDialog();
//                mService = JsonUtils.parse(data, Service.class);
//
//                initSendTime();
//                initSendTime();
//            }
//
//            //一旦请求系统时间出错 则结束订单支付
//            @Override
//            public void error(String msg) {
//                showToast("操作失败，请重试!");
//                cancelProgressDialog();
//                finish();
//            }
//        });
    }


    private String mSendDate,mSendHour;
    private final List<String> mListDate = new ArrayList<String>();
    private int mHourNow,mHourStart,mHourEnd;

    private View outerView;
    private WheelView wheelView,mWheelDate,mWheelTime;
    @SuppressWarnings({ "deprecation", "static-access" })
    private void initSendTime(){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        DateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = new Date();
        for(int i=0;i<3;i++){
            try {
                dateTime = formatter.parse(mService.getCurrentTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mHourNow = dateTime.getHours();
            //获取当前时间如果超过20点 则默认是明天送货
            if(mHourNow>20){
                i = i+1;
            }
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(dateTime);

            calendar.add(calendar.DATE,i);//把日期往后增加一天.整数往后推,负数往前移动
            dateTime=calendar.getTime(); //这个时间就是日期往后推一天的结果

            Service s = new Service();
            s.setDate(formatter.format(dateTime));

            mListDate.add(formatter.format(dateTime));

        }

        //设置默认日期
        mSendDate = mListDate.get(0);

        try {
            //获取现在服务器时间
            dateTime = formatTime.parse(mService.getCurrentTime());
            mHourNow = dateTime.getHours();



            //获取开始时间
            dateTime = formatTime.parse(mService.getStartTime());
            mHourStart = dateTime.getHours();

            //获取当前时间如果超过20点 则默认是明天送货 则默认使用明天的开始时间
            if(mHourNow>20){
                mHourNow = mHourStart;
            }else{
                mHourNow = mHourNow+2;
            }

            //获取结束时间
            dateTime = formatTime.parse(mService.getEndTime());
            mHourEnd = dateTime.getHours();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        outerView = getLayoutInflater().inflate(R.layout.view_wheel, null);

        //日期wheel
        mWheelDate = (WheelView) outerView.findViewById(R.id.wheel1);
        mWheelDate.setItems(mListDate);

        //时间wheel
        mWheelTime= (WheelView) outerView.findViewById(R.id.wheel2);

        //默认加载第一次的时间
        List<String> t = new ArrayList<String>();
        for(int j=mHourNow;j<mHourEnd;j++){
            t.add(j+":00-"+(j+1)+":00");
        }

        //设置默认时间
        mSendHour = t.get(0);
        mWheelTime.setItems(t);

        mWheelDate.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mSendDate = item;

                List<String> d =null;
                if(d == null){
                    d = new ArrayList<String>();
                }
                d.clear();
                //判断下表
                if(selectedIndex == 1){
                    for(int j=mHourNow;j<mHourEnd;j++){
                        d.add(j+":00-"+(j+1)+":00");
                    }
                }else{
                    for(int j=mHourStart;j<mHourEnd;j++){
                        d.add(j+":00-"+(j+1)+":00");
                    }
                }
                mWheelTime.setItems(d);
            }
        });


        mWheelTime.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mSendHour = item;
            }
        });

    }

    private AlertDialog.Builder mTimeBuilder;
    private Dialog mTimeDialog;
    /**
     * 显示设置网络的弹出框
     */
    private void showTimeDialog(){
        if(mTimeBuilder == null){
            mTimeBuilder = new AlertDialog.Builder(getActivity());

            mTimeBuilder.setTitle("请选择配送时间!");
            mTimeBuilder.setView(outerView);
            mTimeBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //打开设置网络界面
                    String send = mSendDate+" "+mSendHour;
                    send = send.substring(0, send.lastIndexOf("-"));
                    mTvTime.setText(send+":00");
                    return;

                }
            });
            mTimeBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            mTimeDialog = mTimeBuilder.create();
        }

        if(mTimeDialog.isShowing()){
            mTimeDialog.dismiss();
            mTimeDialog.cancel();
        }else{
            mTimeDialog.show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode) {
                case ADDRESS_ADD_REQUESTCODE:
                    mViewAddressTv.setVisibility(View.VISIBLE);
                    Bundle mBundle = data.getExtras();

                    mAddress = mBundle.getParcelable(ADR);
                    initAddress();
                    break;

                case ADDRESS_SELECT_REQUESTCODE:
                    mViewAddressTv.setVisibility(View.VISIBLE);
                    Bundle mBundle2 = data.getExtras();

                    mAddress = mBundle2.getParcelable(ADR);
                    initAddress();
                    break;
                case ADDRESS_UPDATE:
                    Bundle mBundle3 = data.getExtras();

                    mAddress = mBundle3.getParcelable(ADR);
                    initAddress();
                    break;

            }

        }
    }

}
