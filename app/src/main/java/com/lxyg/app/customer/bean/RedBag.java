package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 王沛栋 on 2015/12/22.
 */
public class RedBag implements Parcelable {
    private int limit_price;//":28,
    private int reduce;//":1

    protected RedBag(Parcel in) {
        limit_price = in.readInt();
        reduce = in.readInt();
    }

    public static final Creator<RedBag> CREATOR = new Creator<RedBag>() {
        @Override
        public RedBag createFromParcel(Parcel in) {
            return new RedBag(in);
        }

        @Override
        public RedBag[] newArray(int size) {
            return new RedBag[size];
        }
    };

    public int getLimit_price() {
        return limit_price;
    }

    public void setLimit_price(int limit_price) {
        this.limit_price = limit_price;
    }

    public int getReduce() {
        return reduce;
    }

    public void setReduce(int reduce) {
        this.reduce = reduce;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(limit_price);
        dest.writeInt(reduce);
    }
}
