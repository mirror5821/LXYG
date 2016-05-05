package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class Product implements Parcelable{

	public Product(){

	}

	private String p_unit_name;//private String  盒 private String ,
	private String server_id;//2,
	private String p_type_id;//1,
	private String p_brand_name;//private String  白沙 private String ,
	private String payment;//private String [{payId:1,payName:微信支付}]private String ,
	private String index_show;//2,
	private String cover_img;//private String http:\/\/7xk59r.com1.z0.glb.clouddn.com\/FkwASKNe_A3LpfYjXmChmUhMvrJvprivate String ,
	private String p_unit_id;//6,
	private String productId;//38,
	private String server_name;//private String  商家平台 private String ,
	private String title;//private String 白沙private String ,
	private int price;//15,
	private String hide;//1,
	private String name;//private String 白沙private String ,
	private String p_brand_id;//10,
	private String p_type_name;//private String  烟 private String ,
	private String create_time;//private String 2015-07-17 16:56:12private String
	private int cash_pay;
	private int is_norm;
	private int num;
	private int product_number;

	public int getProduct_number() {
		return product_number;
	}

	public void setProduct_number(int product_number) {
		this.product_number = product_number;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getIs_norm() {
		return is_norm;
	}

	public void setIs_norm(int is_norm) {
		this.is_norm = 1;
	}


	public int getCash_pay() {
		return cash_pay;
	}

	public void setCash_pay(int cash_pay) {
		this.cash_pay = cash_pay;
	}

	public String getP_unit_name() {
		return p_unit_name;
	}

	public void setP_unit_name(String p_unit_name) {
		this.p_unit_name = p_unit_name;
	}

	public String getServer_id() {
		return server_id;
	}

	public void setServer_id(String server_id) {
		this.server_id = server_id;
	}

	public String getP_type_id() {
		return p_type_id;
	}

	public void setP_type_id(String p_type_id) {
		this.p_type_id = p_type_id;
	}

	public String getP_brand_name() {
		return p_brand_name;
	}

	public void setP_brand_name(String p_brand_name) {
		this.p_brand_name = p_brand_name;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getIndex_show() {
		return index_show;
	}

	public void setIndex_show(String index_show) {
		this.index_show = index_show;
	}

	public String getCover_img() {
		return cover_img;
	}

	public void setCover_img(String cover_img) {
		this.cover_img = cover_img;
	}

	public String getP_unit_id() {
		return p_unit_id;
	}

	public void setP_unit_id(String p_unit_id) {
		this.p_unit_id = p_unit_id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getServer_name() {
		return server_name;
	}

	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getHide() {
		return hide;
	}

	public void setHide(String hide) {
		this.hide = hide;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getP_brand_id() {
		return p_brand_id;
	}

	public void setP_brand_id(String p_brand_id) {
		this.p_brand_id = p_brand_id;
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

	}


	//MIICXAIBAAKBgQChDhu+KQyut3gEf0PGVzEkvguLyecegJsoG0VBpUOCb+hENdYkJdzWL9GFF+jqZtAcbznUG0MpbI7StBvHzlm+j+L0J/lrqT54KTSQFreFIEsYS2g27Rp9XpoNcXkGopTMhT4AURgbFBzmbZfzWzAul+XOBWG6Y6fUUNqruteE1wIDAQABAoGACNMkFARdzIkDC4QJq6mFaNT8/vjeMB6t4cG0xscObuxA1tP4Wty0QAw4t8k6mvVtO9GgYo3n3TI0lTkHOKiG5ELuEyet26Q550dFSb3/cvmq354Ku/ep0hGBY3E3QArod04xNM6Lt/XFRAmhFpeeU72tXHo/yIPYDbqZS0uAJskCQQDPr1RTQX711Qe3ZGZq0i/StNra233PR8wRo781FpK1xI5ocwtyA/BgkOmCQ0Lvt+pxx+ez8g7Hh/c45GVjIEa9AkEAxoW+XD0R+qmQTGrJ1pWiN0i6ancM7KYDZxe+eTdgKqgFLTliPzXsAM7xPDw4mnSIlhrk/cuUQOellYA6KrNNIwJAXNT0DlriUyQr1M+t7+WPttFcWiNuMu7WyrO0zhgO7+yx8wIphYc5NRDRL4a9LsW4p69BZG+4J3whB28f2pUcyQJARYKg06UifXUPrGIbyAHeqaLSeuKyaM38FQXQUJseFfF81ofoKZ/Uwbez6dZgL1ysUnqaQW8dutghN0aNl1PupwJBALGW96h+fhYg0BXsnZs5PAjH+JDYjiYJPQWhj9FqD8bk8mbDdpxMu6Jj+BckEdXHg5sYV5uX1fuHIKLL+PWEtM8=

}
