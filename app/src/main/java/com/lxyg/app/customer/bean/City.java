package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lxyg.app.customer.iface.AddrBase;

import java.util.List;

/**
 * Created by 王沛栋 on 2015/11/25.
 */
public class City implements Parcelable ,AddrBase{
    protected City(Parcel in) {
    }


    private String province;//":"北京市",
    private int code;//":"10",
    private List<Areas> city;//"

    @Override
    public String getAddrName() {
        return province;
    }


    public static class Areas implements AddrBase{
        private List<Direct> area;//":[],
        private String name;//":"石家庄市",
        private int code;//":"1201

        @Override
        public String getAddrName() {
            return name;
        }


        public static class Direct implements AddrBase {
            private String name;//":"长安区",
            private int code;//":"120102"

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }


            @Override
            public String getAddrName() {
                return name;
            }
        }

        public List<Direct> getArea() {
            return area;
        }

        public void setArea(List<Direct> area) {
            this.area = area;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Areas> getCity() {
        return city;
    }

    public void setCity(List<Areas> city) {
        this.city = city;
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
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
