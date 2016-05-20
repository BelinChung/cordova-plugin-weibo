package com.hiliaox;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WeiboShareCallback extends Activity implements IWeiboHandler.Response {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Weibo.mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Weibo.appKey);
		Weibo.mWeiboShareAPI.registerApp();
        Weibo.mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
	}

	@Override
	public void onResponse(BaseResponse baseResp) {
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			Weibo.currentCallbackContext.success("success");
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			Weibo.currentCallbackContext.error("cancel");
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			Weibo.currentCallbackContext.error("error");
			break;
		}
		this.finish();
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Weibo.mWeiboShareAPI.handleWeiboResponse(intent, this);
	}

}