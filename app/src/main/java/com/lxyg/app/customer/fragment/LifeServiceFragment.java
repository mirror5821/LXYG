package com.lxyg.app.customer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.activity.ShoppingScanActivity;
import com.lxyg.app.customer.activity.WebViewNormalActivity;
import com.lxyg.app.customer.bean.Product;
import com.lxyg.app.customer.utils.ImmersionModeUtils;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.view.SquareImageView;

/**
 * Created by 王沛栋 on 2015/11/13.
 */
public class LifeServiceFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    @Override
    public int setLayoutId() {
        return R.layout.fragment_left_service;
    }

    private GridView mGridView;
    private List<Product> mDatas = new ArrayList<>();
//    private int [] mImgs = {R.mipmap.ic_phone_car,R.mipmap.ic_phone_jin,R.mipmap.ic_phone_life,R.mipmap.ic_phone_post,
//            R.mipmap.ic_phone_public,R.mipmap.ic_phone_shouhou,R.mipmap.ic_phone_suo,R.mipmap.ic_phone_wather,R.mipmap.ic_phone_gx,
//            R.mipmap.ic_phone_cp,R.mipmap.ic_phone_pw,R.mipmap.ic_phone_wz,R.mipmap.ic_phone_wy};
//    private String [] mNames = {"汽车维修","金融保险","家政服务","快递物流","网络服务","手机售后","小区门禁","水费","干洗",
//            "彩票","出行票务","违章查询","物业管理"};

    private int [] mImgs = {R.mipmap.ic_phone_wz,R.mipmap.ic_phone_fei,R.mipmap.ic_phone_post,
            R.mipmap.ic_phone_car_yue,R.mipmap.ic_phone_wather,R.mipmap.ic_phone_cp};
    private String [] mNames = {"违章查询","话费充值","快递查询","车辆预约","水电气充值","彩票查询"};
    private String [] mUrls = {"http://m.weizhang8.cn/",
            "http://chong.qq.com/promote/mobile/wx/index_21423.shtml",
            "http://m.kuaidi100.com/",
            "http://www.xiaojukeji.com/",
            "https://jiaofei.alipay.com/jiaofei.htm",
            "http://touch.lecai.com"};

    private TextView mTvRight;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImmersionModeUtils.setTranslucentStatus(getActivity(), R.color.main_red);
        setTitleText("生活服务");
        mGridView = (GridView) view.findViewById(R.id.g_view);
        mTvRight = (TextView)view.findViewById(R.id.right_text);
//        mTvRight.setText("扫码购物");
        mTvRight.setOnClickListener(this);

        initView();
    }


    private ImgsAdapter mAdapter;

    private void initView(){
        if(mAdapter == null){
            mDatas.clear();
            for (int i=0;i<mImgs.length;i++){
                Product p = new Product();
                p.setCash_pay(mImgs[i]);
                p.setName(mNames[i]);
                mDatas.add(p);
            }

            mAdapter = new ImgsAdapter(getActivity(),mDatas);
            mGridView.setAdapter(mAdapter);
            mGridView.setOnItemClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.right_text:
                startActivity(new Intent(getActivity(), ShoppingScanActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getActivity(), WebViewNormalActivity.class).putExtra(INTENT_ID,mUrls[position]).
                putExtra("TITLE",mNames[position]));
    }

    class ImgsAdapter extends BaseAdapter{

        private List<Product> mData;
        private LayoutInflater mInflater;
        public ImgsAdapter(Context context,List<Product> datas){
            mData = datas;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.item_service,null);
            }

            SquareImageView img = (SquareImageView)convertView.findViewById(R.id.img_s);
            TextView tv = (TextView)convertView.findViewById(R.id.tv);
            img.setImageResource(mData.get(position).getCash_pay());
            tv.setText(mData.get(position).getName());
            return convertView;
        }
    }
}
