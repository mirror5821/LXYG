package com.lxyg.app.customer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Car;
import com.lxyg.app.customer.bean.Product;
import com.lxyg.app.customer.utils.AppAjaxCallback;

import net.tsz.afinal.FinalDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;
import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.utils.PriceUtil;

/**
 * Created by 王沛栋 on 2015/11/10.
 */
public class ShoppingCarActivity  extends BaseActivity{

    private ShoppingCarAdapter mAdapter;

    // 用来控制CheckBox的选中状况
    private static HashMap<Integer,Boolean> mIsSelected;
    //购物车
    private List<Car> mCarList;
    private List<Product> mList;
    private boolean isEdit = false;
    private int mPriceTotal;

    private TextView mTvRight;
    private LinearLayout mView1,mView2;
    private CheckBox mCbAll;
    public View mLoadView;
    public View mEmptyView;
    private ListView mListView;
    private TextView mTvPriceAll;
    private Button mBtn1,mBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_car);

        setBack();
        mList = new ArrayList<Product>();
        mTvRight = initTextView(R.id.right_text);
        mTvRight.setText("编辑");
        setTitleText("购物车");

        mView1 = initLinearLayout(R.id.view1);
        mView2 = initLinearLayout(R.id.view2);


        mCbAll = (CheckBox)findViewById(R.id.cb_all);
        initButton(R.id.btn_delete);
        mBtn1 = initButton(R.id.btn_buy1);
        mBtn2 = initButton(R.id.btn_buy2);
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);

        mLoadView = findViewById(R.id.loading);
        mEmptyView = findViewById(R.id.empty);
        mListView = (ListView)findViewById(R.id.list);
        mTvPriceAll = initTextView(R.id.price_all);

        mCbAll.setOnClickListener(this);

        loadData();
//
    }

    /**
     *
     * 适配器
     *
     */
    public class ShoppingCarAdapter extends DevListBaseAdapter<Product> {

        private final LayoutInflater mInflater;
        private final List<Product> mList;
        private FinalDb mDB ;
        public ShoppingCarAdapter(Context context, List<Product> list) {
            super(context, list);
            mInflater = LayoutInflater.from(context);
            mList = list;
            mDB = AppContext.mDb;

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
                    if(!cb.isChecked()){
                        mCbAll.setChecked(false);
                    }
                    mIsSelected.put(position,  cb.isChecked());
                }
            });

            final Product g = mList.get(position);
            //名称
            name.setText(g.getName());
            //列表图
            AppContext.displayImage(img,g.getCover_img());
            //价格
            final int p = g.getPrice();
            //得到这个项目购物车中的信息
            final Car car = mCarList.get(position);
            //显示价格
            price.setText("单价:￥"+ PriceUtil.floatToString(p));
            et.setText(car.getNum()+"");



            jian.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int i = Integer.valueOf(et.getText().toString().trim());
                    if(i == 1){
                        mDB.deleteByWhere(Car.class, "productId="+"'"+ mCarList.get(position).getProductId()+"'");
                        //更新价格
                        mList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        updatePrice(1, p);
                        mCarList.remove(position);
                        return;
                    }else{
                        i = i -1;
                    }
                    et.setText("" + i);

                    //更新价格
                    updatePrice(1, p);
                    String pId = car.getProductId();
                    Car c = new Car();
                    c.setNum(i);
                    c.setPrice(String.valueOf(p));
                    c.setProductId(pId);
                    c.setIs_norm(car.getIs_norm());
                    c.setActivity_id(car.getActivity_id());
                    mDB.update(c, "productId="+"'"+pId+"'");
                }
            });
            jia.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int i = Integer.valueOf(et.getText().toString().trim());
                    i = i+1;
                    et.setText(""+i);

                    //更新价格
                    updatePrice(2, p);

                    String pId = car.getProductId();//.getProductId();

                    Car c = new Car();
                    c.setNum(i);
                    c.setPrice(String.valueOf(p));
                    c.setProductId(pId);
                    c.setIs_norm(car.getIs_norm());
                    c.setActivity_id(car.getActivity_id());
                    mDB.update(c, "productId=" + "'" + pId + "'");
                }
            });

            return convertView;
        }
    }

    private void updatePrice(int type,int price){
        if(type==1){
            mPriceTotal = mPriceTotal -price;
        }else{
            mPriceTotal = mPriceTotal +price;
        }

        String html = "订单总额:<font color='red'>"+ PriceUtil.floatToString(mPriceTotal)+"</font>元";
        mTvPriceAll.setText(Html.fromHtml(html));

    }


    /**
     * 加载无数据时 view
     * @param msg
     */
    public void setLoadingFailed(CharSequence msg){
        mLoadView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        if(mEmptyView instanceof TextView)
            ((TextView)mEmptyView).setText(msg);
    }

    /**
     * 展示列表
     */
    public void showList(){
        mLoadView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }

    public void loadData() {

        mCarList = AppContext.mDb.findAll(Car.class);//查询所有的用户
        //如果购物车为空
        if(mCarList==null){
            setLoadingFailed("您还未添加任何商品!");
            mTvRight.setVisibility(View.GONE);
            mView1.setVisibility(View.GONE);
            mList.clear();

        }else{
            mTvRight.setVisibility(View.VISIBLE);

            if(isEdit){
                mTvRight.setText("完成");
                mView1.setVisibility(View.GONE);
                mView2.setVisibility(View.VISIBLE);
                mCbAll.setChecked(false);
            }else{
                mView1.setVisibility(View.VISIBLE);
            }

            JSONObject jb = new JSONObject();
            try {
                JSONArray ja = new JSONArray();
                for (Car car : mCarList) {
                    JSONObject j = new JSONObject();
                    j.put("productId",car.getProductId());
                    j.put("is_norm",car.getIs_norm());
                    j.put("activity_id",car.getActivity_id());
                    ja.put(j);
                }
                jb.put("pids", ja.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mBaseHttpClient.postData1(SHOPPING_CAR_LIST2, jb,new AppAjaxCallback.onResultListener() {

                @Override
                public void onResult(String data, String msg) {
                    if(mIsSelected == null){
                        mIsSelected = new HashMap<Integer,Boolean>();
                    }

                    mList.clear();
                    mIsSelected.clear();


                    mList.addAll(JsonUtils.parseList(data, Product.class));
                    mAdapter = new ShoppingCarAdapter(getActivity(), mList);

                    mListView.setAdapter(mAdapter);
                    showList();

                    mPriceTotal = 0;
                    for(int i=0; i<mList.size();i++) {
                        mIsSelected.put(i,false);

                        Product p = mList.get(i);
                        Car c = mCarList.get(i);
                        int n = c.getNum();
                        int pp = p.getPrice();

                        mPriceTotal = mPriceTotal+(n*pp);
                        c.setPrice(p.getPrice()+"");

                        mCarList.set(i, c);
                    }
                    String html = "订单总额:<font color='red'>"+ PriceUtil.floatToString(mPriceTotal)+"</font>元";
                    mTvPriceAll.setText(Html.fromHtml(html));
                }

                @Override
                public void onError(String error) {
                    setLoadingFailed("您还未添加任何商品!");
                    mTvRight.setVisibility(View.GONE);
                    mView1.setVisibility(View.GONE);
                    mList.clear();
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //删除按钮
            case R.id.btn_delete:
                showNormalDialog("删除商品", "确认删除这些商品?", "确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int len = mIsSelected.size();
                        for(int i=0;i<len;i++){
                            //如果选中
                            if(mIsSelected.get(i)){
                                //先删除数据
                                AppContext.mDb.deleteByWhere(Car.class, "productId="+"'"+mCarList.get(i).getProductId()+"'");
                            }

                        }


                        //设置全选按钮为false
                        mCbAll.setChecked(false);
                        isEdit = false;
                        mTvRight.setText("编辑");
                        //恢复界面
                        mView1.setVisibility(View.VISIBLE);
                        mView2.setVisibility(View.GONE);

                        loadData();

                    }
                });

                break;
            case R.id.btn_buy1:
                buy();
                break;
            case R.id.btn_buy2:
                buy();
                break;
            case R.id.right_text:
                if(isEdit){
                    mTvRight.setText("编辑");
                    isEdit = false;
                    mView1.setVisibility(View.VISIBLE);
                    mView2.setVisibility(View.GONE);
                }else{
                    mTvRight.setText("完成");
                    isEdit = true;
                    mView1.setVisibility(View.GONE);
                    mView2.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.cb_all: //全选 反选事件
                mIsSelected.clear();

                if(mCbAll.isChecked()){
                    for(int i=0; i<mList.size();i++) {
                        mIsSelected.put(i,true);
                    }
                }else{
                    for(int i=0; i<mList.size();i++) {
                        mIsSelected.put(i,false);
                    }
                }
                mAdapter.notifyDataSetChanged();
                break;

        }
    }

    private void buy(){


        if(!AppContext.IS_LOGIN){
            startActivityForResult(new Intent(getActivity(),WechatAndPhoneLoginActivity.class), LOGIN_CODE1);
        }else{
            startActivity(new Intent(getActivity(),OrderMakeActivity.class).putExtra(TYPE_ID,PAY_TYPE1));
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode) {
                case LOGIN_CODE1:
                    startActivity(new Intent(getActivity(),OrderMakeActivity.class).putExtra(TYPE_ID,PAY_TYPE1));
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isEdit){
            mTvRight.setText("完成");
            mView1.setVisibility(View.GONE);
            mView2.setVisibility(View.VISIBLE);
            mCbAll.setChecked(false);
        }

        loadData();
    }
}
