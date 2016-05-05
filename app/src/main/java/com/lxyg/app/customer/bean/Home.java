package com.lxyg.app.customer.bean;

import java.util.List;

/**
 * Created by 王沛栋 on 2015/12/21.
 */
public class Home {
    private int shopId;
    private String uuid;
    private int shopCount;
    private String cover_img;
    private int shop_type;
    private int is_norm;
    private String name;
    private String service_phone;
    private List<HomeProduct> types;
    private List<Category> category;
    private List<Activitys> shopActivits;
    private List<Product> recommGoods;
    private List<Activitys> activitys;
    private String title;

    public List<Activitys> getActivitys() {
        return activitys;
    }

    public void setActivitys(List<Activitys> activitys) {
        this.activitys = activitys;
    }

    public List<HomeProduct> getTypes() {
        return types;
    }

    public void setTypes(List<HomeProduct> types) {
        this.types = types;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getShopCount() {
        return shopCount;
    }

    public void setShopCount(int shopCount) {
        this.shopCount = shopCount;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public int getShop_type() {
        return shop_type;
    }

    public void setShop_type(int shop_type) {
        this.shop_type = shop_type;
    }

    public int getIs_norm() {
        return is_norm;
    }

    public void setIs_norm(int is_norm) {
        this.is_norm = is_norm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Activitys> getShopActivits() {
        return shopActivits;
    }

    public void setShopActivits(List<Activitys> shopActivits) {
        this.shopActivits = shopActivits;
    }

    public List<Product> getRecommGoods() {
        return recommGoods;
    }

    public void setRecommGoods(List<Product> recommGoods) {
        this.recommGoods = recommGoods;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
