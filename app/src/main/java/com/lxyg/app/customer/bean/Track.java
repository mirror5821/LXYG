package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Track implements Parcelable{
	private String order_id;
	private List<pos> pos;

	public static class pos{
		private double lng;
		private double lat;
		public double getLng() {
			return lng;
		}
		public void setLng(double lng) {
			this.lng = lng;
		}
		public double getLat() {
			return lat;
		}
		public void setLat(double lat) {
			this.lat = lat;
		}


	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public List<pos> getPos() {
		return pos;
	}

	public void setPos(List<pos> pos) {
		this.pos = pos;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {

	}


}
