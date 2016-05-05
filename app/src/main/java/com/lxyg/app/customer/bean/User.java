package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
	private String id;//22,
	private String phone;//private String 18837145625private String ,
	private String shop_id;//0,
	private String name;//private String 18837145625private String ,
	private String create_time;//private String 2015-07-31private String ,
	private String uuid;//private String 9c22fcbd9122458dprivate String
	private String cash_pay;//0,
	private String score;//0,
	private String head_img;//private String http://wx.qlogo.cn/mmopen/aIfW3O6n5Dc3vKwMvFqHNDC5AVGFc9Cibhvt396ZkssqBFQOFSicgX6WMuusMjcWwbciaksibHX1IIdMpbxVu4aKKcBkPeo4VsbG/0private String ,
	private String wechat_id;//private String ouUNEw3ZEEa-SidXBp-oP6bgiHQAprivate String ,
	private String modify_time;//null,
	private String password;//null




	public String getCash_pay() {
		return cash_pay;
	}
	public void setCash_pay(String cash_pay) {
		this.cash_pay = cash_pay;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getHead_img() {
		return head_img;
	}
	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}
	public String getWechat_id() {
		return wechat_id;
	}
	public void setWechat_id(String wechat_id) {
		this.wechat_id = wechat_id;
	}
	public String getModify_time() {
		return modify_time;
	}
	public void setModify_time(String modify_time) {
		this.modify_time = modify_time;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {

	}


}
