package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 王沛栋 on 2016/4/13.
 */
public class FBShop implements Parcelable{

    public FBShop(){

    }

    private String delivery_time;// 0,
    private String fb_uid;// private String 93fcddb387984e94private String ,
    private String sale_num;// 0,
    private String name;// private String 非标店private String ,
    private String full_address;// private String 金三路农业南路private String ,
    private String delivery_price;// 0,
    private String cover_img;// private String http://lxyg8.b0.upaiyun.com/activity/gundong1.jpgprivate String ,
    private String send_price;// 0


    protected FBShop(Parcel in) {
        delivery_time = in.readString();
        fb_uid = in.readString();
        sale_num = in.readString();
        name = in.readString();
        full_address = in.readString();
        delivery_price = in.readString();
        cover_img = in.readString();
        send_price = in.readString();
    }

    public static final Creator<FBShop> CREATOR = new Creator<FBShop>() {
        @Override
        public FBShop createFromParcel(Parcel in) {
            return new FBShop(in);
        }

        @Override
        public FBShop[] newArray(int size) {
            return new FBShop[size];
        }
    };

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getFb_uid() {
        return fb_uid;
    }

    public void setFb_uid(String fb_uid) {
        this.fb_uid = fb_uid;
    }

    public String getSale_num() {
        return sale_num;
    }

    public void setSale_num(String sale_num) {
        this.sale_num = sale_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getDelivery_price() {
        return delivery_price;
    }

    public void setDelivery_price(String delivery_price) {
        this.delivery_price = delivery_price;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public String getSend_price() {
        return send_price;
    }

    public void setSend_price(String send_price) {
        this.send_price = send_price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(delivery_time);
        dest.writeString(fb_uid);
        dest.writeString(sale_num);
        dest.writeString(name);
        dest.writeString(full_address);
        dest.writeString(delivery_price);
        dest.writeString(cover_img);
        dest.writeString(send_price);
    }
}
