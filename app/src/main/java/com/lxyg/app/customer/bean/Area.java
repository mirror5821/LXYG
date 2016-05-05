package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lxyg.app.customer.iface.AddrBase;

import java.util.List;

/**
 * Created by 王沛栋 on 2015/10/29.
 */
public class Area implements Parcelable ,AddrBase{
    private String city_id;//7001,
    private String city_name;//private String 郑州市private String ,
    private List<District> district;//[

    protected Area(Parcel in) {
        city_id = in.readString();
        city_name = in.readString();
    }

    public static final Creator<Area> CREATOR = new Creator<Area>() {
        @Override
        public Area createFromParcel(Parcel in) {
            return new Area(in);
        }

        @Override
        public Area[] newArray(int size) {
            return new Area[size];
        }
    };

    @Override
    public String getAddrName() {
        return city_name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public List<District> getDistrict() {
        return district;
    }

    public void setDistrict(List<District> district) {
        this.district = district;
    }

    public static class District implements AddrBase{
        private List<Shops> shops;//[],
        private String name;//private String 名门世家private String ,
        private String districe_id;//3

        @Override
        public String getAddrName() {
            return name;
        }

        public List<Shops> getShops() {
            return shops;
        }

        public void setShops(List<Shops> shops) {
            this.shops = shops;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDistrice_id() {
            return districe_id;
        }

        public void setDistrice_id(String districe_id) {
            this.districe_id = districe_id;
        }

        public static class Shops implements AddrBase{
            private String phone;//private String 15010751366private String ,
            private String link_man;//private String 孔豪private String ,
            private String shop_id;//1,
            private String name;//private String 孔豪无忧店private String ,
            private String full_address;//private String 河南省郑州市南阳路孟砦南街 南阳 南阳private String
            private String s_uid;

            public String getS_uid() {
                return s_uid;
            }

            public void setS_uid(String s_uid) {
                this.s_uid = s_uid;
            }

            @Override
            public String getAddrName() {
                return name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getLink_man() {
                return link_man;
            }

            public void setLink_man(String link_man) {
                this.link_man = link_man;
            }

            public String getShop_id() {
                return shop_id;
            }

            public void setShop_id(String shop_id) {
                this.shop_id = shop_id;
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


        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city_id);
        dest.writeString(city_name);
    }
}
