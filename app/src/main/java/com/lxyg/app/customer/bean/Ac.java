package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 王沛栋 on 2015/12/19.
 */
public class Ac implements Parcelable{
    public Ac(){

    }
    private String p_unit_name;// private String 瓶private String ,
    private String p_brand_name;// private String 可口可乐private String ,
    private String p_type_id;// 24,
    private int surplus_num;// 50,
    private String cover_img;// private String http://lxyg8.b0.upaiyun.com/platform/1wRd6sdq3fzPAfJp.jpgprivate String ,
    private String productId;// 2,
    private String title;// private String 净含量: 500ml包装方式: 包装产地: 中国大陆 private String ,
    private int cash_pay;// 0,
    private int price;// 200,
    private String p_brand_id;// 102,
    private String name;// private String 雪碧500ml private String ,
    private String p_type_name;// private String 碳酸饮料private String ,
    private String create_time;// private String 2015-12-19 11:23:48private String ,
    private String limit_num;// 50
    private int num;


    protected Ac(Parcel in) {
        p_unit_name = in.readString();
        p_brand_name = in.readString();
        p_type_id = in.readString();
        surplus_num = in.readInt();
        cover_img = in.readString();
        productId = in.readString();
        title = in.readString();
        cash_pay = in.readInt();
        price = in.readInt();
        p_brand_id = in.readString();
        name = in.readString();
        p_type_name = in.readString();
        create_time = in.readString();
        limit_num = in.readString();
    }

    public static final Creator<Ac> CREATOR = new Creator<Ac>() {
        @Override
        public Ac createFromParcel(Parcel in) {
            return new Ac(in);
        }

        @Override
        public Ac[] newArray(int size) {
            return new Ac[size];
        }
    };

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

    public String getP_brand_name() {
        return p_brand_name;
    }

    public void setP_brand_name(String p_brand_name) {
        this.p_brand_name = p_brand_name;
    }

    public String getP_type_id() {
        return p_type_id;
    }

    public void setP_type_id(String p_type_id) {
        this.p_type_id = p_type_id;
    }

    public int getSurplus_num() {
        return surplus_num;
    }

    public void setSurplus_num(int surplus_num) {
        this.surplus_num = surplus_num;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCash_pay() {
        return cash_pay;
    }

    public void setCash_pay(int cash_pay) {
        this.cash_pay = cash_pay;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getP_brand_id() {
        return p_brand_id;
    }

    public void setP_brand_id(String p_brand_id) {
        this.p_brand_id = p_brand_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getP_type_name() {
        return p_type_name;
    }

    public void setP_type_name(String p_type_name) {
        this.p_type_name = p_type_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getLimit_num() {
        return limit_num;
    }

    public void setLimit_num(String limit_num) {
        this.limit_num = limit_num;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(p_unit_name);
        dest.writeString(p_brand_name);
        dest.writeString(p_type_id);
        dest.writeInt(surplus_num);
        dest.writeString(cover_img);
        dest.writeString(productId);
        dest.writeString(title);
        dest.writeInt(cash_pay);
        dest.writeInt(price);
        dest.writeString(p_brand_id);
        dest.writeString(name);
        dest.writeString(p_type_name);
        dest.writeString(create_time);
        dest.writeString(limit_num);
    }
}
