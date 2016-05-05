package com.lxyg.app.customer.wxapi;


import android.content.Intent;
import android.os.Bundle;

import com.lxyg.app.customer.activity.BaseActivity;
import com.lxyg.app.customer.app.AppContext;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppContext.WX_API.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if(resp!=null){
			AppContext.RESP = resp;
		}
		switch (resp.errCode) {
		case 0://成功
			AppContext.IS_PAY_OK = true;
			break;
		default://任何失败
			AppContext.IS_PAY_OK = false;
			break;

		}

		finish();

	}
}