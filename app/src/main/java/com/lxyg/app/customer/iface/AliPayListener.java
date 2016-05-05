package com.lxyg.app.customer.iface;

public interface AliPayListener {
	void success(String msg);
	void error(String msg);
	void waitForComfrim(String msg);
}
