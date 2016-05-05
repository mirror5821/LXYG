package com.lxyg.app.customer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Order implements Parcelable{

	private String phone;//private String 18837145615private String ,
	private String city_name;//private String 郑州市private String ,
	private String street;//private String 建业如意家园20号楼private String ,
	private String lng;//113.778145,
	private String order_id;//private String 6af0194ed50040b4private String ,
	private String order_no;//private String 1437722288823private String ,
	private String send_type;//2,
	private String send_time;//private String 2015-07-24 15:00:00private String ,
	private String address_id;//7,
	private String pay_type;//1,
	private String name;//private String Wangprivate String ,
	private String area_name;//private String 金水区private String ,
	private String modify_time;//null,
	private String lat;//34.77176,
	private String orderId;//134,
	private String send_id;//0,
	private String shop_id;//private String 94b39f59e8b3466dprivate String ,
	private String finish_time;//null,
	private String full_address;//private String 河南省郑州市金水区建业如意家园20号楼private String ,
	private String order_status;//2,
	private String send_name;//private String 定时送private String ,
	private String shop_name;//private String mirror5821卡卡无忧店铺private String ,
	private int price;//27,
	private String province_name;//private String 河南省private String ,
	private String address;//private String 河南省郑州市金水区建业如意家园20号楼private String ,
	private String create_time;//private String 2015-07-24 15:18:08private String ,
	private String u_uuid;//private String 8cbda72ab668452aprivate String ,
	private String order_type;//1,
	private String pay_name;//private String 微信支付private String
	private String  receive_code;
	private String rec_phone;
	private String rec_name;
	private String send_goods_type;//取货方式

	public String getSend_goods_type() {
		return send_goods_type;
	}

	public void setSend_goods_type(String send_goods_type) {
		this.send_goods_type = send_goods_type;
	}

	protected Order(Parcel in) {
		phone = in.readString();
		city_name = in.readString();
		street = in.readString();
		lng = in.readString();
		order_id = in.readString();
		order_no = in.readString();
		send_type = in.readString();
		send_time = in.readString();
		address_id = in.readString();
		pay_type = in.readString();
		name = in.readString();
		area_name = in.readString();
		modify_time = in.readString();
		lat = in.readString();
		orderId = in.readString();
		send_id = in.readString();
		shop_id = in.readString();
		finish_time = in.readString();
		full_address = in.readString();
		order_status = in.readString();
		send_name = in.readString();
		shop_name = in.readString();
		price = in.readInt();
		province_name = in.readString();
		address = in.readString();
		create_time = in.readString();
		u_uuid = in.readString();
		order_type = in.readString();
		pay_name = in.readString();
		receive_code = in.readString();
		rec_phone = in.readString();
		rec_name = in.readString();
	}

	public static final Creator<Order> CREATOR = new Creator<Order>() {
		@Override
		public Order createFromParcel(Parcel in) {
			return new Order(in);
		}

		@Override
		public Order[] newArray(int size) {
			return new Order[size];
		}
	};

	public String getRec_phone() {
		return rec_phone;
	}

	public void setRec_phone(String rec_phone) {
		this.rec_phone = rec_phone;
	}

	public String getRec_name() {
		return rec_name;
	}

	public void setRec_name(String rec_name) {
		this.rec_name = rec_name;
	}

	private List<orderItems> orderItems;

	private List<orderItems> orderActivityItems;


	public static class orderItems{
		private String cash_pay;//1,
		private String product_pay;//8,
		private String product_id;//39,
		private int product_price;//9,
		private String orderItemId;//76,
		private String name;//private String 红旗渠private String ,
		private String product_number;//1,
		private String cover_img;//
		private String receive_code;//收货码
		private int activity_id;
		private int is_norm;

		public int getIs_norm() {
			return is_norm;
		}

		public void setIs_norm(int is_norm) {
			this.is_norm = is_norm;
		}

		public int getActivity_id() {
			return activity_id;
		}

		public void setActivity_id(int activity_id) {
			this.activity_id = activity_id;
		}

		public String getReceive_code() {
			return receive_code;
		}
		public void setReceive_code(String receive_code) {
			this.receive_code = receive_code;
		}
		public String getCash_pay() {
			return cash_pay;
		}
		public void setCash_pay(String cash_pay) {
			this.cash_pay = cash_pay;
		}
		public String getProduct_pay() {
			return product_pay;
		}
		public void setProduct_pay(String product_pay) {
			this.product_pay = product_pay;
		}
		public String getProduct_id() {
			return product_id;
		}
		public void setProduct_id(String product_id) {
			this.product_id = product_id;
		}
		public int getProduct_price() {
			return product_price;
		}
		public void setProduct_price(int product_price) {
			this.product_price = product_price;
		}
		public String getOrderItemId() {
			return orderItemId;
		}
		public void setOrderItemId(String orderItemId) {
			this.orderItemId = orderItemId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getProduct_number() {
			return product_number;
		}
		public void setProduct_number(String product_number) {
			this.product_number = product_number;
		}
		public String getCover_img() {
			return cover_img;
		}
		public void setCover_img(String cover_img) {
			this.cover_img = cover_img;
		}
		
		
	}

	public List<Order.orderItems> getOrderActivityItems() {
		return orderActivityItems;
	}

	public void setOrderActivityItems(List<Order.orderItems> orderActivityItems) {
		this.orderActivityItems = orderActivityItems;
	}

	public String getReceive_code() {
		return receive_code;
	}

	public void setReceive_code(String receive_code) {
		this.receive_code = receive_code;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getSend_type() {
		return send_type;
	}

	public void setSend_type(String send_type) {
		this.send_type = send_type;
	}

	public String getSend_time() {
		return send_time;
	}

	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}

	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getModify_time() {
		return modify_time;
	}

	public void setModify_time(String modify_time) {
		this.modify_time = modify_time;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSend_id() {
		return send_id;
	}

	public void setSend_id(String send_id) {
		this.send_id = send_id;
	}

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public String getFinish_time() {
		return finish_time;
	}

	public void setFinish_time(String finish_time) {
		this.finish_time = finish_time;
	}

	public String getFull_address() {
		return full_address;
	}

	public void setFull_address(String full_address) {
		this.full_address = full_address;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getSend_name() {
		return send_name;
	}

	public void setSend_name(String send_name) {
		this.send_name = send_name;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getProvince_name() {
		return province_name;
	}

	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getU_uuid() {
		return u_uuid;
	}

	public void setU_uuid(String u_uuid) {
		this.u_uuid = u_uuid;
	}

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	public String getPay_name() {
		return pay_name;
	}

	public void setPay_name(String pay_name) {
		this.pay_name = pay_name;
	}

	
	public List<orderItems> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<orderItems> orderItems) {
		this.orderItems = orderItems;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

		dest.writeString(phone);
		dest.writeString(city_name);
		dest.writeString(street);
		dest.writeString(lng);
		dest.writeString(order_id);
		dest.writeString(order_no);
		dest.writeString(send_type);
		dest.writeString(send_time);
		dest.writeString(address_id);
		dest.writeString(pay_type);
		dest.writeString(name);
		dest.writeString(area_name);
		dest.writeString(modify_time);
		dest.writeString(lat);
		dest.writeString(orderId);
		dest.writeString(send_id);
		dest.writeString(shop_id);
		dest.writeString(finish_time);
		dest.writeString(full_address);
		dest.writeString(order_status);
		dest.writeString(send_name);
		dest.writeString(shop_name);
		dest.writeInt(price);
		dest.writeString(province_name);
		dest.writeString(address);
		dest.writeString(create_time);
		dest.writeString(u_uuid);
		dest.writeString(order_type);
		dest.writeString(pay_name);
		dest.writeString(receive_code);
		dest.writeString(rec_phone);
		dest.writeString(rec_name);
	}

}
