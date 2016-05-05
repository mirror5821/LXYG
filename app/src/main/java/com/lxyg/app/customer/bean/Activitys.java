package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Activitys implements Parcelable{
	public Activitys() {
	}
	
	private String img_url;//private String http:\/\/7xk59r.com1.z0.glb.clouddn.com\/Fnv4AbnmNTISBY8qQAA4FhDdyM7Gprivate String ,
	private String activityId;//3,
	private String label_cn;//private String erveybody let't go !!!private String ,
	private String shop_id;//59,
	private String alt;//null
	private int type;
	private String start_time;
	private String end_time;
	private String name;
	private int activity_type;
	private int limit_e;

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getActivity_type() {
		return activity_type;
	}

	public void setActivity_type(int activity_type) {
		this.activity_type = activity_type;
	}

	public int getLimit_e() {
		return limit_e;
	}

	public void setLimit_e(int limit_e) {
		this.limit_e = limit_e;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getLabel_cn() {
		return label_cn;
	}
	public void setLabel_cn(String label_cn) {
		this.label_cn = label_cn;
	}
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(img_url);//http:\/\/7xk59r.com1.z0.glb.clouddn.com\/Fnv4AbnmNTISBY8qQAA4FhDdyM7G,
		dest.writeString(activityId);//3,
		dest.writeString(label_cn);//erveybody let't go !!!,
		dest.writeString(shop_id);//59,
		dest.writeString(alt);//null
		dest.writeInt(type);
	}
	
	public Activitys(Parcel parcel) {
		img_url = parcel.readString();//http:\/\/7xk59r.com1.z0.glb.clouddn.com\/Fnv4AbnmNTISBY8qQAA4FhDdyM7G,
		activityId = parcel.readString();//3,
		label_cn = parcel.readString();//erveybody let't go !!!,
		shop_id = parcel.readString();//59,
		alt = parcel.readString();//null
		type = parcel.readInt();
	}
	
	public static final Creator<Activitys> CREATOR = new Creator<Activitys>() {

		@Override
		public Activitys[] newArray(int size) {
			return new Activitys[size];
		}

		@Override
		public Activitys createFromParcel(Parcel source) {
			return new Activitys(source);
		}
	};


}
