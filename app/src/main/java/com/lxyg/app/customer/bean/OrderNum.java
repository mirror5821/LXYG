package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by haha on 2015/8/31.
 */
public class OrderNum implements Parcelable {
    private int dsh;//":8,
    private int ywc;//":0,
    private int dfh;//":1,
    private int dfk;//":3

    public int getDsh() {
        return dsh;
    }

    public void setDsh(int dsh) {
        this.dsh = dsh;
    }

    public int getYwc() {
        return ywc;
    }

    public void setYwc(int ywc) {
        this.ywc = ywc;
    }

    public int getDfh() {
        return dfh;
    }

    public void setDfh(int dfh) {
        this.dfh = dfh;
    }

    public int getDfk() {
        return dfk;
    }

    public void setDfk(int dfk) {
        this.dfk = dfk;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
