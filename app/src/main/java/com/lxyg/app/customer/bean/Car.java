package com.lxyg.app.customer.bean;

import net.tsz.afinal.annotation.sqlite.Table;

@Table(name="kk_car")
public class Car {
	public Car(){

	}
	
	private int id;
	private String productId;
	private int num;
	private String price;
	private int is_norm;
	private int activity_id;

	public int getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
	}

	public int getIs_norm() {
		return is_norm;
	}

	public void setIs_norm(int is_norm) {
		this.is_norm = is_norm;
	}
	
	

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}


}
