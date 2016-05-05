package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by 王沛栋 on 2015/12/3.
 */
public class Sign implements Parcelable {
    private Record record;
    private List<Times> times;
    private String date;

    public static class Record{
        private int jf_num;//":10,
        private int lxqd_num;//":1,
        private int zqd_num;//":

        public int getJf_num() {
            return jf_num;
        }

        public void setJf_num(int jf_num) {
            this.jf_num = jf_num;
        }

        public int getLxqd_num() {
            return lxqd_num;
        }

        public void setLxqd_num(int lxqd_num) {
            this.lxqd_num = lxqd_num;
        }

        public int getZqd_num() {
            return zqd_num;
        }

        public void setZqd_num(int zqd_num) {
            this.zqd_num = zqd_num;
        }
    }

    public static class Times{
        private String create_time;

        public String getcTime() {
            return create_time;
        }

        public void setcTime(String create_time) {
            this.create_time = create_time;
        }
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public List<Times> getTimes() {
        return times;
    }

    public void setTimes(List<Times> times) {
        this.times = times;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    protected Sign(Parcel in) {
    }

    public static final Creator<Sign> CREATOR = new Creator<Sign>() {
        @Override
        public Sign createFromParcel(Parcel in) {
            return new Sign(in);
        }

        @Override
        public Sign[] newArray(int size) {
            return new Sign[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
