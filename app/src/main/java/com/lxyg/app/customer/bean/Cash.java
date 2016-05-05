package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Cash implements Parcelable{
	private int cash;//":17800,"
	private String u_uuid;//":"63c70b83e5f94ed2"

	private int cash_status;//":1,
	private String end_time;//":"2016-01-14 16:53:13",
	private String create_time;//":"2015-08-20 14:28:15",
	private String cash_title;//":"登录即送178",


	protected Cash(Parcel in) {
		cash = in.readInt();
		u_uuid = in.readString();
		cash_status = in.readInt();
		end_time = in.readString();
		create_time = in.readString();
		cash_title = in.readString();
	}

	public static final Creator<Cash> CREATOR = new Creator<Cash>() {
		@Override
		public Cash createFromParcel(Parcel in) {
			return new Cash(in);
		}

		@Override
		public Cash[] newArray(int size) {
			return new Cash[size];
		}
	};

	public String getCash_title() {
		return cash_title;
	}

	public void setCash_title(String cash_title) {
		this.cash_title = cash_title;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public int getCash_status() {
		return cash_status;
	}

	public void setCash_status(int cash_status) {
		this.cash_status = cash_status;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

	public String getU_uuid() {
		return u_uuid;
	}

	public void setU_uuid(String u_uuid) {
		this.u_uuid = u_uuid;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(cash);
		arg0.writeString(u_uuid);
		arg0.writeInt(cash_status);
		arg0.writeString(end_time);
		arg0.writeString(create_time);
		arg0.writeString(cash_title);
	}

}
