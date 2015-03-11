package com.soundario;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class Weibo extends CordovaPlugin {

    private static final int WEIBO_NOT_INSTALLED = 1;
    private static final int WEIBO_AUTHORIZE_CANCELED = 2;
    private static final int WEIBO_INVALID_TOKEN = 3;
    private static final int WEIBO_UNKNOWN_ERROR = 99;
    
    private static final String TAG = "Cordova-Weibo-SSO";

    private SsoHandler mSsoHandler = null;

    @Override
    public boolean execute(String action, JSONArray args,
            CallbackContext context) throws JSONException {
        this.buildSsoHandler();
        if (action.equals("login")) {
            this.login(context);
            return true;
        } else if (action.equals("isInstalled")) {
            this.checkWeibo(context);
            return true;
        }
        return false;
    }

    private void buildSsoHandler() {
        Activity activity = this.cordova.getActivity();

        String key = "请填写key";
        // redirect_url and scope can be optional
        String url = "https://api.weibo.com/oauth2/default.html";
        String scope = "email,direct_messages_read,direct_messages_write,"
+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read," + "follow_app_official_microblog," + "invitation_write";

        AuthInfo authInfo = new AuthInfo(activity, key, url, scope);

        this.mSsoHandler = new SsoHandler(activity, authInfo);
    }

    private void checkWeibo(final CallbackContext context) {
        context.success(this.isWeiboInstalled() ? 1 : 0);
    }

    private boolean isWeiboInstalled() {
        return mSsoHandler != null && mSsoHandler.isWeiboAppInstalled();
    }

    private void login(final CallbackContext context) {
        Activity activity = this.cordova.getActivity();
        this.cordova.setActivityResultCallback(this);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSsoHandler.authorize(new AuthListener(context));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    class ErrorMessage extends JSONObject {
        public ErrorMessage(int code, String message) {
            try {
                this.put("code", code);
                this.put("message", message);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class AuthListener implements WeiboAuthListener {
        private CallbackContext context;

        public AuthListener(CallbackContext context) {
            this.context = context;
        }

        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken.isSessionValid()) {
                JSONObject res = new JSONObject();
                try {
                    res.put("uid", accessToken.getUid());
                    res.put("token", accessToken.getToken());
                    res.put("expire_at", accessToken.getExpiresTime());
                    res.put("refresh_token",accessToken.getRefreshToken());
                    context.success(res);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                String code = values.getString("code");
                context.error(new ErrorMessage(WEIBO_INVALID_TOKEN, code));
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            String message = e.getMessage();
            context.error(new ErrorMessage(WEIBO_UNKNOWN_ERROR, message));
            Log.e(TAG, message, e);
        }

        @Override
        public void onCancel() {
            String message = "authorize cancelled";
            context.error(new ErrorMessage(WEIBO_AUTHORIZE_CANCELED, message));
            Log.i(TAG, message);
        }
    }
}
