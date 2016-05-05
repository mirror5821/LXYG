package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lxyg.app.customer.iface.AddrBase;

public class Types implements Parcelable,AddrBase{
	public Types() {
	}

	private String img;//null,
	private String p_type_id;//1,
	private String p_type_name;//private String 海鲜干制品private String ,

	@Override
	public String getAddrName() {
		return p_type_name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

	}


}
