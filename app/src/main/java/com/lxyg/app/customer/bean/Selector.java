package com.lxyg.app.customer.bean;


import com.lxyg.app.customer.iface.AddrBase;

/**
 * 自定义文本选择
 * @author 王沛栋
 *
 */
public class Selector implements AddrBase {
	private String id;
	private String selection;
	
	

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getSelection() {
		return selection;
	}



	public void setSelection(String selection) {
		this.selection = selection;
	}



	@Override
	public String getAddrName() {
		return selection;
	}

}
