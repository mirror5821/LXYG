package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable{
	public Category() {
	}

	private String typeId;//1
	private String name;//private String 烟private String ,
	private String img;//null,
	private int is_norm;
	private int id;

	protected Category(Parcel in) {
		typeId = in.readString();
		name = in.readString();
		img = in.readString();
		is_norm = in.readInt();
		id = in.readInt();
		p_type_id = in.readString();
		p_type_name = in.readString();
		p_brand_name = in.readString();
		p_brand_id = in.readString();
	}

	public static final Creator<Category> CREATOR = new Creator<Category>() {
		@Override
		public Category createFromParcel(Parcel in) {
			return new Category(in);
		}

		@Override
		public Category[] newArray(int size) {
			return new Category[size];
		}
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String p_type_id;//1,
	private String p_type_name;//private String 海鲜干制品private String ,
	private String p_brand_name;//private String 鱿鱼干private String ,
	private String p_brand_id;//41

	public String getP_type_id() {
		return p_type_id;
	}

	public void setP_type_id(String p_type_id) {
		this.p_type_id = p_type_id;
	}

	public String getP_type_name() {
		return p_type_name;
	}

	public void setP_type_name(String p_type_name) {
		this.p_type_name = p_type_name;
	}

	public String getP_brand_name() {
		return p_brand_name;
	}

	public void setP_brand_name(String p_brand_name) {
		this.p_brand_name = p_brand_name;
	}

	public String getP_brand_id() {
		return p_brand_id;
	}

	public void setP_brand_id(String p_brand_id) {
		this.p_brand_id = p_brand_id;
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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(typeId);
		dest.writeString(name);
		dest.writeString(img);
		dest.writeInt(is_norm);
		dest.writeInt(id);
		dest.writeString(p_type_id);
		dest.writeString(p_type_name);
		dest.writeString(p_brand_name);
		dest.writeString(p_brand_id);
	}

}
