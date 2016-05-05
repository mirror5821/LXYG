package com.lxyg.app.customer.bean;


import java.util.List;

/**
 * Created by 王沛栋 on 2015/12/21.
 */
public class HomeProduct {
    private Types type;
    private List<Product> products;

    public static class Types{
        private String name;//private String 一次性用品private String ,
        private String img;//private String http://lxyg8.b0.upaiyun.com/icon/ic_category_type18.pngprivate String ,
        private int typeId;//1

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

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
