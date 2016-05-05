package com.lxyg.app.customer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Car;
import com.lxyg.app.customer.bean.Constants;
import com.lxyg.app.customer.bean.FBProduct;
import com.lxyg.app.customer.bean.FBShop;
import com.lxyg.app.customer.bean.Product;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.AppAjaxParam;
import com.lxyg.app.customer.utils.AppHttpClient;

import net.tsz.afinal.FinalDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;
import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.utils.PriceUtil;

/**
 * Created by haha on 2015/9/15.
 */
public class FBShopProductListActivity extends BaseListActivity<FBShop> {
    @Override
    public int setLayoutId() {
        return R.layout.activity_fb_product_list;
    }

    private LayoutInflater mInflater;
    private AppHttpClient mHttpClient;
    private String mId;
    private FinalDb db;

    private TextView mTvNumAndPrice;//总价和数量
    private Button mBtnBuy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppContext.mDb;

        mTvNumAndPrice = (TextView)findViewById(R.id.tv_price_num);
        mBtnBuy = (Button)findViewById(R.id.btn_buy1);
        mBtnBuy.setOnClickListener(this);
        //加载价格
        loadPriceData();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView =mInflater.inflate(R.layout.item_fb_product, null);
        }

        ImageView img = (ImageView)convertView.findViewById(R.id.img);
        TextView name = (TextView)convertView.findViewById(R.id.name);
        TextView price = (TextView)convertView.findViewById(R.id.price);
        TextView title = (TextView)convertView.findViewById(R.id.title);
        ImageView imgCar = (ImageView) DevListBaseAdapter.ViewHolder.get(convertView, R.id.img_car);
        final TextView num = (TextView) DevListBaseAdapter.ViewHolder.get(convertView, R.id.n);
        TextView cash = (TextView)convertView.findViewById(R.id.cash);

        final FBShop s = mList.get(position);
        AppContext.displayImage(img, s.getCover_img());
        name.setText(s.getName());
        price.setText("售价: ￥"+ s.getDelivery_price());
        title.setText(s.getFull_address());
        cash.setText("配送价: ￥"+ s.getSend_price());

        String mPid = s.getFb_uid();
        try {
            //先判断数据库是否有数据
            List<Car> c = db.findAllByWhere(Car.class, "productId=" + "'" + mPid + "'");
            if (c != null) {
                //如果存在数据
                if (c.size() > 0) {
                    //将已经存在的数据叠加
                    num.setVisibility(View.VISIBLE);
                    num.setText(c.get(0).getNum() + "");
                }
            }
        }catch (Exception e){

        }
        imgCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCar(s,num);
            }
        });
        return convertView;
    }

    private void addCar(FBShop p,TextView tv){
        String mPid = p.getFb_uid();

        int count = 1;
        //先判断数据库是否有数据
        List<Car> c = db.findAllByWhere(Car.class, "productId="+"'"+mPid+"'");
        try {
            //如果存在数据
            if (c.size() > 0) {
                //将已经存在的数据叠加
                count = count + c.get(0).getNum();
                //删除已经存在的数据
                db.deleteByWhere(Car.class, "productId=" + "'" + mPid + "'");
            }
        }catch (Exception e){

        }
        Car car = new Car();
        car.setProductId(mPid);
        car.setPrice((Float.valueOf(p.getDelivery_price())+Float.valueOf(p.getSend_price())) + "");
        car.setNum(count);
        car.setIs_norm(Constants.PRODUCT_TYPE2);
        tv.setVisibility(View.VISIBLE);
        tv.setText(count + "");
        //保存数据
        AppContext.mDb.save(car);
        loadPriceData();
    }

    private boolean isFirstLoad = true;
    @Override
    protected void onResume() {
        super.onResume();
        if(isFirstLoad){
           isFirstLoad = false;
        }else{
            loadData();
            loadPriceData();
        }
    }

    @Override
    public void loadData() {
        mId = getIntent().getStringExtra(INTENT_ID);
        if(mList == null) {
            mList = new ArrayList<FBShop>();
        }
        mInflater = getLayoutInflater();
        setBack();
        setTitleText("商品列表");

        JSONObject jb = new JSONObject();
        try{
            jb.put("fb_uid",mId);
            jb.put("pg",pageNo);
        }catch (JSONException e){
            finish();
        }

        mBaseHttpClient.postData(FB_SHOP_PRODUCT_LIST, jb, new AppAjaxCallback.onRecevieDataListener<FBShop>() {
            @Override
            public void onReceiverData(List<FBShop> data, String msg) {
                if (pageNo == mDefaultPage) {
                    mList.clear();
                }
                mList.addAll(data);
                setListAdapter();
            }

            @Override
            public void onReceiverError(String msg) {
                showToast(msg);
            }

            @Override
            public Class<FBShop> dataTypeClass() {
                return FBShop.class;
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> paramAdapterView, View view, int position, long num) {
        startActivity(new Intent(FBShopProductListActivity.this, FBProductDetailsActivity.class).putExtra(INTENT_ID, mList.get(position - 1).getFb_uid()));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_buy1:
                buy();
                break;
        }
    }

    /**
     * 购买
     */
    private void buy(){
        if(!AppContext.IS_LOGIN){
            startActivityForResult(new Intent(FBShopProductListActivity.this,WechatAndPhoneLoginActivity.class), LOGIN_CODE1);
        }else{
            startActivity(new Intent(FBShopProductListActivity.this,OrderMakeActivity.class).putExtra(TYPE_ID,PAY_TYPE1));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode) {
                case LOGIN_CODE1:
                    startActivity(new Intent(FBShopProductListActivity.this,OrderMakeActivity.class).putExtra(TYPE_ID,PAY_TYPE1));
                    break;
            }
        }
    }


    private List<Car> mCarList;
    private int mPriceTotal;
    private List<Product> mCarProduct;
    ///////总价参数处理
    public void loadPriceData() {
        mCarList = AppContext.mDb.findAll(Car.class);//查询所有的用户

        if(mCarProduct ==  null){
            mCarProduct = new ArrayList<Product>();
        }

        //如果购物车为空
        if(mCarList.size()==0){
            String html = "订单总额:<font color='red'>0</font>元";
            mTvNumAndPrice.setText(Html.fromHtml(html));
        }else{

            JSONObject jb = new JSONObject();
            try {
                JSONArray ja = new JSONArray();
                for (Car car : mCarList) {
                    JSONObject j = new JSONObject();
                    j.put("productId",car.getProductId());
                    j.put("is_norm",car.getIs_norm());
                    ja.put(j);
                }
                jb.put("pids", ja.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mHttpClient.postData1(SHOPPING_CAR_LIST2, jb,new AppAjaxCallback.onResultListener() {

                @Override
                public void onResult(String data, String msg) {
                    mCarProduct.clear();
                    mCarProduct.addAll(JsonUtils.parseList(data, Product.class));

                    mPriceTotal = 0;
                    for(int i=0; i<mCarProduct.size();i++) {

                        Product p = mCarProduct.get(i);
                        Car c = mCarList.get(i);
                        int n = c.getNum();
                        int pp = p.getPrice();

                        mPriceTotal = mPriceTotal+(n*pp);
//						c.setPrice(p.getPrice()+"");
//						mCarList.set(i, c);
                    }

                    String html = "订单总额:<font color='red'>"+ PriceUtil.floatToString(mPriceTotal)+"</font>元";
                    mTvNumAndPrice.setText(Html.fromHtml(html));
                }

                @Override
                public void onError(String error) {
                    String html = "订单总额:<font color='red'>0</font>元";
                    mTvNumAndPrice.setText(Html.fromHtml(html));
                }
            });
        }

    }


}
