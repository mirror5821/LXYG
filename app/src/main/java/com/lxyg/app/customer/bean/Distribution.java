package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 王沛栋 on 2015/12/25.
 */
public class Distribution  implements Parcelable{
    private double lng;//": 113.656325,
    private double lat;//": 34.849218

    protected Distribution(Parcel in) {
        lng = in.readDouble();
        lat = in.readDouble();
    }

    public static final Creator<Distribution> CREATOR = new Creator<Distribution>() {
        @Override
        public Distribution createFromParcel(Parcel in) {
            return new Distribution(in);
        }

        @Override
        public Distribution[] newArray(int size) {
            return new Distribution[size];
        }
    };

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lng);
        dest.writeDouble(lat);
    }
}
