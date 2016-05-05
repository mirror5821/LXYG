package com.lxyg.app.customer.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.ActivityAutumnShoppingCarActivity;
import com.lxyg.app.customer.activity.OrderDetailsActivity;
import com.lxyg.app.customer.activity.OrderMakeActivity;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Ac;
import com.lxyg.app.customer.bean.Order;
import com.lxyg.app.customer.iface.PostDataListener;
import com.lxyg.app.customer.utils.AppAjaxCallback;
import com.lxyg.app.customer.utils.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.mirror.library.activity.DevBaseActivity;
import dev.mirror.library.utils.DpUtil;
import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.utils.PriceUtil;
import dev.mirror.library.utils.UIHelper;
import dev.mirror.library.view.SquareImageView;

/**
 * Created by 王沛栋 on 2015/11/6.
 */
public class OrderListFragment extends BaseListFragment<Order> {

    private int mStatus;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer,Boolean> mIsSelected;
    //是否第一次加载数据 用于保存checkbox的状态
    private boolean mFirstLoad = true;

    private LayoutInflater mInflater;


    public OrderListFragment newInstance(int pager) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putInt(INTENT_ID,pager);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这里我只是简单的用num区别标签，其实具体应用中可以使用真实的fragment对象来作为叶片
        mStatus = getArguments().getInt(INTENT_ID);
    }

    @Override
    public int setLayoutId() {
        mList = new ArrayList<Order>();
        mInflater = getActivity().getLayoutInflater();
        return 0;
    }

    /**
     *
     * 1可抢单 2代发货 3待收货（配送中） 4交易完成 5拒收 6 让单 7流单  这个是b端的 现在这边不管用
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView =mInflater.inflate(R.layout.item_order_new, null);
        }
        LinearLayout viewB1 = (LinearLayout)convertView.findViewById(R.id.view_b1);
        LinearLayout viewB2 = (LinearLayout)convertView.findViewById(R.id.view_b2);
        LinearLayout viewB3 = (LinearLayout)convertView.findViewById(R.id.view_b3);
        LinearLayout viewB5 = (LinearLayout)convertView.findViewById(R.id.view_b5);


        final TextView orderNum = (TextView)convertView.findViewById(R.id.tv_num);
        final TextView orderTime = (TextView)convertView.findViewById(R.id.tv_date);
        final TextView priceAndNum = (TextView)convertView.findViewById(R.id.tv_num_price);
        final TextView numAndNext = (TextView)convertView.findViewById(R.id.p_total);


        final LinearLayout mViewProduct = (LinearLayout)convertView.findViewById(R.id.view2);

        //抢单按钮
        final Button pay = (Button)convertView.findViewById(R.id.btn_pay);

        //详情按钮1 小的
        final Button details1 = (Button)convertView.findViewById(R.id.btn_details);
        //删除订单
        final Button delete = (Button)convertView.findViewById(R.id.btn_delete);

        //详情按钮2 大的
        final Button details2 = (Button)convertView.findViewById(R.id.btn_details2);
        //详情按钮3大的
        final Button details3 = (Button)convertView.findViewById(R.id.btn_details3);
        //确认送达
        final Button sendOk = (Button)convertView.findViewById(R.id.btn_ok);
        //拒绝收货
        final Button ref = (Button)convertView.findViewById(R.id.btn_re);
        //催发提醒
        final Button cuifa = (Button)convertView.findViewById(R.id.btn_cf);
        final Button btn_fh = (Button)convertView.findViewById(R.id.btn_fh);
        //这个才是拒收
        final Button refuse = (Button)convertView.findViewById(R.id.btn_refuse);


        Order o = mList.get(position);

        switch (mStatus) {
            case 1:
                viewB1.setVisibility(View.VISIBLE);
                //付款  type1
                pay.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        btnEvent(position, 1);
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnEvent(position, 6);
                    }
                });
                break;
            case 2:
                viewB2.setVisibility(View.VISIBLE);
                //详情事件
                details1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showDetails(position);
                    }
                });

                cuifa.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        callKf();
                    }
                });

                refuse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refuseOrder(position);
                    }
                });
                break;
            case 3:
                viewB3.setVisibility(View.VISIBLE);
                //详情事件
                details2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showDetails(position);
                    }
                });
                //收货事件 type4
                sendOk.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        btnEvent(position, 4);
                    }
                });

                //拒绝收货
                ref.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        btnEvent(position, 5);
                    }
                });
                btn_fh.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        callKf();
                    }
                });
                break;
            case 5:
                viewB5.setVisibility(View.VISIBLE);
                //详情事件
                details3.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showDetails(position);
                    }
                });
                break;

        }


        orderNum.setText("订单:"+o.getOrder_no());
        orderTime.setText(o.getCreate_time());
        priceAndNum.setText("实付款:￥" + PriceUtil.floatToString(o.getPrice()));

        if(o.getOrderItems() != null){
            numAndNext.setText("共"+o.getOrderItems().size()+"种商品");


            mViewProduct.removeAllViews();
            for (Order.orderItems item : o.getOrderItems()) {
                View view = mInflater.inflate(R.layout.view_imageview_order_make, null);
                final SquareImageView img = (SquareImageView)view.findViewById(R.id.img_s);
                AppContext.displayImage(img, item.getCover_img());
                mViewProduct.addView(view);
            }
        }else{
            numAndNext.setText("共"+o.getOrderActivityItems().size()+"件");

            mViewProduct.removeAllViews();
            for (Order.orderItems item : o.getOrderActivityItems()) {
                View view = mInflater.inflate(R.layout.view_imageview_order_make, null);
                final SquareImageView img = (SquareImageView)view.findViewById(R.id.img_s);
                AppContext.displayImage(img, item.getCover_img());
                mViewProduct.addView(view);
            }
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetails(position);
            }
        });

        return convertView;
    }

    private EditText mEtRefuse;
    private Button mBtn;
    private Dialog mDialog;
    private void refuseOrder(final int position){
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_refuse_order,null);
        mEtRefuse = (EditText)view.findViewById(R.id.et);
        mBtn = (Button)view.findViewById(R.id.sub);
        showNormalDialogByCustomerView(view, new DevBaseActivity.DialogIface() {
            @Override
            public void d(Dialog dialog) {
                mDialog = dialog;
            }
        });

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jb = new JSONObject();
                try {
                    //'uid':'8a1a9f99fbc54896','cause':'xxxxxx','orderId':'93fcddb387984e94'
                    jb.put("cause",mEtRefuse.getText().toString());
                    jb.put("orderId",mList.get(position).getOrder_id());
                }catch (JSONException e){

                }
                NetUtil.loadData(REFUSE, jb, new PostDataListener() {
                    @Override
                    public void getDate(String data, String msg) {
                        mList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        showToast(msg);
                        mDialog.dismiss();
                    }

                    @Override
                    public void error(String msg) {
                        showToast(msg);
                    }
                });
            }
        });
    }

    /**
     * 拨打客服
     */
    private void callKf(){
        UIHelper.makePhoneCall(getActivity(), getResources().getString(R.string.kf_phone_num));
    }
    /**
     *
     * @param position
     * @param type
     *
     * 抢单事件1  让单2  发货3 确认收货4  删除6
     */
    @SuppressWarnings("deprecation")
    private void btnEvent(final int position,final int type){
        String fname = null;
        int s = 0;
        switch (type) {
            case 1:
                //付款
                Order order = mList.get(position);
                if(order.getOrderActivityItems() !=null){
                    List<Ac> mListCar = new ArrayList<Ac>();
                    for (Order.orderItems car : order.getOrderActivityItems()) {
                        Ac a = new Ac();

                        a.setName(car.getName());
                        a.setNum(Integer.valueOf(car.getProduct_number()));
                        a.setName(car.getName());
                        a.setCover_img(car.getCover_img());
                        a.setPrice(car.getProduct_price());
                        mListCar.add(a);
                    }
                    startActivity(new Intent(getActivity(), ActivityAutumnShoppingCarActivity.class).
                            putExtra(INTENT_ID,JsonUtils.listToString(mListCar,Ac.class)).putExtra("activity_id",order.getOrderId()));
                }else{
                    startActivity(new Intent(getActivity(),OrderMakeActivity.class).
                            putExtra(INTENT_ID, JsonUtils.beanToString(mList.get(position), Order.class)).putExtra(TYPE_ID,PAY_TYPE2));
                }

                return;

            case 2:
                //让单
                //			fname = ORDER_RANG;

                break;
            case 3:
                //发货 2
                //			s =2;
                //			fname = ORDER_UPDATE_STATUS;
                break;
            case 4:
                //确认收货 3
                fname = RECEIVER_GOODS;
                //			s =3;
                break;
            case 5:
                fname = ORDER_REF;
                break;
            case 6:
                fname = ORDER_DELETE;
                break;
        }

        if(TextUtils.isEmpty(fname)){
            showToast("此功能暂无开放");
            return;
        }
        JSONObject jb = new JSONObject();
        try {
            jb.put("orderId", mList.get(position).getOrder_id());
            if(s !=0){
                jb.put("orderStatus", s);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        NetUtil.loadData(fname, jb, new PostDataListener() {

            @Override
            public void getDate(String data, String msg) {
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
                showToast(msg);
            }

            @Override
            public void error(String msg) {
                showToast(msg);
            }
        });
    }


    /**
     * 显示详情
     * @param position
     * 我实在是不愿意写那个parcle什么的
     *
     */
    private void showDetails(int position){
        startActivity(new Intent(getActivity(),OrderDetailsActivity.class).putExtra(INTENT_ID,
                JsonUtils.beanToString(mList.get(position), Order.class)).putExtra("type", mStatus));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView.getRefreshableView().setDividerHeight(DpUtil.dip2px(getActivity(), 4));//main_bg
        mListView.setOnItemClickListener(OrderListFragment.this);
    }

    @Override
    public void loadData() {

        JSONObject jb = new JSONObject();
        try {
            jb.put("status", mStatus);
            jb.put("pg", pageNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mBaseHttpClient.postData1(ORDER_LIST, jb, new AppAjaxCallback.onResultListener() {
            @Override
            public void onResult(String data, String msg) {

                if(pageNo == mDefaultPage){
                    if(mList == null){
                        mList = new ArrayList<Order>();
                    }
                    mList.clear();
                }
                try{
                    JSONObject jb = new JSONObject(data);
                    JSONObject jb1 = new JSONObject(jb.getString("order"));

                    List<Order> order1 = JsonUtils.parseList(jb1.getString("list"),Order.class);
                    if(order1 !=null){
                        mList.addAll(order1);
                    }

                    JSONObject jb2 = new JSONObject(jb.getString("orderActivity"));
                    List<Order> order2 = JsonUtils.parseList(jb2.getString("list"),Order.class);
                    if(order2 !=null){
                        mList.addAll(order2);
                    }


                }catch (JSONException e){

                }
                setListAdapter();
            }

            @Override
            public void onError(String error) {

            }
        });

//        mHttpClient.postData(ORDER_LIST, jb, new AppAjaxCallback.onRecevieDataListener<Order>() {
//
//            @Override
//            public void onReceiverData(List<Order> data, String msg) {
//                if(mFirstLoad)
//                    if(mIsSelected == null){
//                        mIsSelected = new HashMap<Integer,Boolean>();
//                    }
//                if(pageNo == mDefaultPage){
//                    mList.clear();
//                    //如果第一次加载列表 则往cb添加数据 否则刷新禁止清空数据
//                    if(mFirstLoad){
//                        mFirstLoad = false;
//                        for(int i=0; i<data.size();i++) {
//                            mIsSelected.put(mList.size()+i,false);
//                        }
//                    }
//                }else{
//                    for(int i=0; i<data.size();i++) {
//                        mIsSelected.put(mList.size()+i,false);
//                    }
//                }
//                mList.addAll(data);
//
//                setListAdapter();
//
//
//            }
//
//            @Override
//            public void onReceiverError(String msg) {
////				showToast(msg);
//                setLoadingFailed(msg);
//            }
//
//            @Override
//            public Class<Order> dataTypeClass() {
//                return Order.class;
//            }
//        });
    }

    @Override
    public void onItemClick(AdapterView<?> paramAdapterView, View view, int position, long num) {
        showDetails(position - 1);
    }
}
