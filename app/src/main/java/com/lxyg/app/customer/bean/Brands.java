package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lxyg.app.customer.iface.AddrBase;

public class Brands implements Parcelable,AddrBase{
	public Brands() {
	}


	private String p_type_id;//1,
	private String p_type_name;//private String 海鲜干制品private String ,
	private String p_brand_name;//private String 鱿鱼干private String ,
	private String p_brand_id;//41

	@Override
	public String getAddrName() {
		return p_brand_name;
	}


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



	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
	}


}
