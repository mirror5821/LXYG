package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by haha on 2015/9/8.
 */
public class Autumn  implements Parcelable{
    private String p_unit_name;//private String 瓶private String ,
    private int cash_pay;//0,
    private String title;//private String 前锋甘美怡人 单宁柔醇private String ,
    private int price;//10000,
    private String payment;//null,
    private String activity_id;//92,
    private String name;//private String 酒神城堡干红葡萄酒 三支装private String ,
    private String create_time;//private String 2015-09-07 15:54:54private String ,
    private String paId;//1,
    private String cover_img;//private String http://7xk59r.com2.z0.glb.qiniucdn.com/jiushenhongjiu.jpgprivate String ,
    private String order_no;//1
    private int num = 1;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getP_unit_name() {
        return p_unit_name;
    }

    public void setP_unit_name(String p_unit_name) {
        this.p_unit_name = p_unit_name;
    }

    public int getCash_pay() {
        return cash_pay;
    }

    public void setCash_pay(int cash_pay) {
        this.cash_pay = cash_pay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
