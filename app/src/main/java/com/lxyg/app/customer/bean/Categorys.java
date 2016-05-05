package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lxyg.app.customer.iface.AddrBase;

import java.util.List;

/**
 * Created by 王沛栋 on 2015/12/17.
 */
public class Categorys implements Parcelable,AddrBase {
    private int id;//1,
    private String name;//private String 休闲零食private String ,
    private String img;//null,
    private String create_time;//private String 2015-12-10 10:51:34private String ,
    private int is_norm;//1,
    private List<cate> types;//[],
    private String modify_time;//private String 2015-12-10 10:51:34private String ,
    private int shop_type;//1,
    private int sort_id;//1

    protected Categorys(Parcel in) {
        id = in.readInt();
        name = in.readString();
        img = in.readString();
        create_time = in.readString();
        is_norm = in.readInt();
        modify_time = in.readString();
        shop_type = in.readInt();
        sort_id = in.readInt();
    }

    public static final Creator<Categorys> CREATOR = new Creator<Categorys>() {
        @Override
        public Categorys createFromParcel(Parcel in) {
            return new Categorys(in);
        }

        @Override
        public Categorys[] newArray(int size) {
            return new Categorys[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(img);
        dest.writeString(create_time);
        dest.writeInt(is_norm);
        dest.writeString(modify_time);
        dest.writeInt(shop_type);
        dest.writeInt(sort_id);
    }

    @Override
    public String getAddrName() {
        return name;
    }

    public static class cate implements AddrBase{
        private int id;//15,
        private List<brand> brands;//[],
        private int p_category_id;//1,
        private String name;//private String 干果炒货private String ,
        private String img;//private String http://lxyg8.b0.upaiyun.com/icon/ic_category_type44.pngprivate String ,
        private String create_time;//private String 2015-12-10 10:58:43private String ,
        private int is_norm;//1,
        private String modify_time;//private String 2015-12-10 10:58:43private String ,
        private int shop_type;//1,
        private int sort_id;//1

        @Override
        public String getAddrName() {
            return name;
        }


        public static class brand implements AddrBase{
            private int id;//233,
            private int p_type_id;//17,
            private int p_category_id;//1,
            private String p_type_name;//private String 果脯蜜饯private String ,
            private String name;//private String 溜溜梅private String ,
            private String create_time;//private String 2015-12-10 11:08:05private String ,
            private String img;//null,
            private String modify_time;//private String 2015-12-10 11:08:05private String

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getP_type_id() {
                return p_type_id;
            }

            public void setP_type_id(int p_type_id) {
                this.p_type_id = p_type_id;
            }

            public int getP_category_id() {
                return p_category_id;
            }

            public void setP_category_id(int p_category_id) {
                this.p_category_id = p_category_id;
            }

            public String getP_type_name() {
                return p_type_name;
            }

            public void setP_type_name(String p_type_name) {
                this.p_type_name = p_type_name;
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

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getModify_time() {
                return modify_time;
            }

            public void setModify_time(String modify_time) {
                this.modify_time = modify_time;
            }

            @Override
            public String getAddrName() {
                return name;
            }
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<brand> getBrands() {
            return brands;
        }

        public void setBrands(List<brand> brands) {
            this.brands = brands;
        }

        public int getP_category_id() {
            return p_category_id;
        }

        public void setP_category_id(int p_category_id) {
            this.p_category_id = p_category_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getIs_norm() {
            return is_norm;
        }

        public void setIs_norm(int is_norm) {
            this.is_norm = is_norm;
        }

        public String getModify_time() {
            return modify_time;
        }

        public void setModify_time(String modify_time) {
            this.modify_time = modify_time;
        }

        public int getShop_type() {
            return shop_type;
        }

        public void setShop_type(int shop_type) {
            this.shop_type = shop_type;
        }

        public int getSort_id() {
            return sort_id;
        }

        public void setSort_id(int sort_id) {
            this.sort_id = sort_id;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getIs_norm() {
        return is_norm;
    }

    public void setIs_norm(int is_norm) {
        this.is_norm = is_norm;
    }

    public List<cate> getTypes() {
        return types;
    }

    public void setTypes(List<cate> types) {
        this.types = types;
    }

    public String getModify_time() {
        return modify_time;
    }

    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }

    public int getShop_type() {
        return shop_type;
    }

    public void setShop_type(int shop_type) {
        this.shop_type = shop_type;
    }

    public int getSort_id() {
        return sort_id;
    }

    public void setSort_id(int sort_id) {
        this.sort_id = sort_id;
    }
}
