package com.lxyg.app.customer.bean;

public class WxPay {


	private String sign;//53EF189D248FAF71BD21B37FB7702EBFprivate String ,
	private String timestamp;//1439801200private String ,
	private String noncestr;//qe88okc5ryd7sopwt414n1fbyyqp6dynprivate String ,
	private String partnerid;//1263785401private String ,
	private String prepayid;//wx2015081716484896d1b6afb10714197759private String ,
//	private String package;//Sign=WXPayprivate String ,
	private String appid;//wx9e721ab3631bd8bbprivate String
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}




}
