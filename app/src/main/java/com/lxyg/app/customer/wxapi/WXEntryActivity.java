package com.lxyg.app.customer.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.lxyg.app.customer.activity.BaseActivity;
import com.lxyg.app.customer.app.AppContext;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {


	// IWXAPI 是第三方app和微信通信的openapi接口
//	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AppContext.WX_API.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		AppContext.WX_API.handleIntent(intent, this);
		handleIntent(intent);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			break;
		default:
			break;
		}
	}

	private void handleIntent(Intent intent) {
		SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
		if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
			//用户同意
			Toast.makeText(this, resp.toString(), Toast.LENGTH_LONG).show();
		}
		Toast.makeText(this, resp.code+"", Toast.LENGTH_LONG).show();
		System.out.println("lalalalalalalalalalalalalalala");
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(final BaseResp resp) {
		if(resp!=null){
			AppContext.RESP = resp;
		}

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK://成功
			AppContext.WECHAT_LOGIN = true;
			this.finish();
			//String code = ((SendAuth.Resp) resp).code;
			break;

		case BaseResp.ErrCode.ERR_USER_CANCEL://失败
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			break;

		default:
			break;
		}


	}

}