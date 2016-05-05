package com.lxyg.app.customer.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxyg.app.customer.R;
import com.lxyg.app.customer.app.AppContext;
import com.lxyg.app.customer.bean.Car;
import com.lxyg.app.customer.bean.Product;

import java.util.List;

import dev.mirror.library.adapter.DevListBaseAdapter;
import dev.mirror.library.utils.PriceUtil;

/**
 * Created by 王沛栋 on 2015/12/4.
 */
public class ShoppingScanAdapter extends BaseAdapter {
    private List<Product> mDatas;
    private LayoutInflater mInflater;
    private TextView mTv;

    private int mPriceTotal;
    public ShoppingScanAdapter(Context context,List<Product> datas,TextView tv){
        this.mDatas =  datas;
        mInflater = LayoutInflater.from(context);
        mTv = tv;

//        for (int i =0;i<mDatas.size();i++){
//            Product p = mDatas.get(i);
//            if(mDatas.get(i))
//        }
    }
    @Override
    public int getCount() {
        return mDatas.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_shopping_car, null);
        }

        TextView name = (TextView) DevListBaseAdapter.ViewHolder.get(convertView, R.id.name);
        TextView price = (TextView) DevListBaseAdapter.ViewHolder.get(convertView, R.id.price);
        ImageView img = (ImageView) DevListBaseAdapter.ViewHolder.get(convertView, R.id.img);
        final CheckBox cb = (CheckBox) DevListBaseAdapter.ViewHolder.get(convertView, R.id.cb);
        final EditText et = (EditText) DevListBaseAdapter.ViewHolder.get(convertView, R.id.num);
        Button jian = (Button) DevListBaseAdapter.ViewHolder.get(convertView, R.id.jian);
        Button jia = (Button) DevListBaseAdapter.ViewHolder.get(convertView, R.id.add);


        final Product g = mDatas.get(position);
        //名称
        name.setText(g.getName());
        //列表图
        AppContext.displayImage(img, g.getCover_img());
        //价格
        final int p = g.getPrice();

        //显示价格
        price.setText("单价:￥" + PriceUtil.floatToString(p));
        if (g.getNum() == 0){
            et.setText("1");
        }else{
            et.setText(g.getNum()+"");
        }
        jian.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int i = Integer.valueOf(et.getText().toString().trim());
                if(i == 1){

                    mDatas.remove(position);
                    notifyDataSetChanged();
                    updatePrice(1, p);
                    return;
                }else{
                    i = i -1;
                }
                et.setText("" + i);

                //更新价格
                updatePrice(1, p);
                g.setNum(i);
            }
        });
        jia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int i = Integer.valueOf(et.getText().toString().trim());
                i = i+1;
                et.setText("" + i);

                //更新价格
                updatePrice(2, p);
                g.setNum(i);

            }
        });

        return convertView;
    }

    public void updatePrice(int type,int price){

        if(type==1){
            mPriceTotal = mPriceTotal -price;
        }else{
            mPriceTotal = mPriceTotal +price;
        }

        String html = "订单总额:<font color='red'>"+ PriceUtil.floatToString(mPriceTotal)+"</font>元";
        mTv.setText(Html.fromHtml(html));
    }
}
