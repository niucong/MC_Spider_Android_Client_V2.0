package com.datacomo.mc.spider.android.weibo;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.actionbarsherlock.view.MenuItem;
import com.datacomo.mc.spider.android.BasicActionBarActivity;
import com.datacomo.mc.spider.android.LoginActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.bean.UserBean;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.tencent.weibo.api.Friends_API;
import com.tencent.weibo.api.User_API;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.Configuration;
import com.tencent.weibo.utils.OAuthClient;

public class TenCentOAuthActivity extends BasicActionBarActivity {
	private final String TAG = "TenCentOAuthActivity";

	// private Button imageView;
	// private TextView textView;
	private ProgressBar progressBar;
	private WebView webView;

	private String oauth_token = null;
	private String access_token = null;
	private String access_token_secret = null;
	private OAuth oauth = new OAuth(WeiBoUtil.QQ_oauth_consumer_key,
			WeiBoUtil.QQ_oauth_consumer_secret, WeiBoUtil.QQ_oauth_callback);;

	private String clientIp;

	private MCResult loginResponse;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_oauth);
		// setTitle("腾讯微博授权", R.drawable.title_fanhui, null);
		ab.setTitle("腾讯微博授权");

		progressBar = (ProgressBar) findViewById(R.id.oauth_progressBar);
		webView = (WebView) findViewById(R.id.oauth_webView);

		try {
			OAuthClient auth = new OAuthClient();
			// 获取request token
			oauth = auth.requestToken(oauth);
			if (oauth.getStatus() == 1) {
				return;
			} else {
				String oauth_token = oauth.getOauth_token();
				L.d(TAG, "onCreate oauth_token=" + oauth_token);
				initialize(WeiBoUtil.QQ_Request_token_url + oauth_token);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize(String url) {
		L.d(TAG, "initialize url=" + url);

		webView.clearCache(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setWebViewClient(new WebViewC());
		webView.setWebChromeClient(new OAuthWebChromeClient());
		webView.loadUrl(url);
	}

	private class OAuthWebChromeClient extends WebChromeClient {
		public void onProgressChanged(WebView view, int progress) {
			L.i(TAG, "onProgressChanged progress=" + progress);
			progressBar.setProgress(progress);
		}
	}

	private class WebViewC extends WebViewClient {
		@SuppressWarnings("unused")
		int index = 0;

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// 表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
			view.loadUrl(url);
			return true;
		}

		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			// 此方法可以让webview处理https请求
			handler.proceed();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			progressBar.setVisibility(View.VISIBLE);
			/**
			 * url.contains(callBackUrl) 如果授权成功url中包含之前设置的callbackurl 包含：授权成功
			 * index == 2 由于该方法onPageStarted可能被多次调用造成重复跳转 则添加此标示
			 */
			index++;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			progressBar.setVisibility(View.GONE);
			L.d(TAG, "onPageFinished url=" + url);
			Uri uri = Uri.parse(url);
			if (url.contains("oauth_verifier")) {
				oauth_token = uri.getQueryParameter("oauth_verifier");
				L.d(TAG, "onPageFinished oauth_token=" + oauth_token);
				if (oauth_token != null && !"".equals(oauth_token)) {
					LogicUtil.finish(TenCentOAuthActivity.this);
					getOAuthToken(uri);
					// loading();
				}
			}
		}
	}

	public void getOAuthToken(Uri uri) {
		try {
			String oauth_verifier = uri.getQueryParameter("oauth_verifier");
			String oauth_token = uri.getQueryParameter("oauth_token");
			oauth.setOauth_token(oauth_token);
			oauth.setOauth_verifier(oauth_verifier);

			OAuthClient auth = new OAuthClient();
			oauth = auth.accessToken(oauth);
			access_token = oauth.getOauth_token();
			access_token_secret = oauth.getOauth_token_secret();

			L.i(TAG, "getOAuthToken access_token=" + access_token);
			L.i(TAG, "getOAuthToken access_token_secret=" + access_token_secret);
			spdDialog.showProgressDialog("授权成功！正在登录…");
			new Thread() {
				public void run() {
					loginYuuQuu(access_token, access_token_secret);
				};
			}.start();
		} catch (Exception e) {
			LoginActivity.loginActivity.sinaHandler.sendEmptyMessage(0);
			e.printStackTrace();
		}
	}

	/**
	 * 注册登录优优工作圈
	 * 
	 * @param access_Token
	 * @param access_Token_secret
	 */
	private void loginYuuQuu(String access_Token, String access_Token_secret) {
		User_API user = new User_API();
		try {
			String usre_str = user.info(oauth, "json");
			L.i(TAG, "loginYuuQuu usre_str=" + usre_str);
			JSONObject info = new JSONObject(usre_str);
			JSONObject dataInfo = new JSONObject(info.getString("data"));
			String name = dataInfo.getString("nick"); // 用户的昵称
			L.i(TAG, "loginYuuQuu name=" + name);
			String birth = dataInfo.getString("birth_year") + "-"
					+ dataInfo.getString("birth_month") + "-"
					+ dataInfo.getString("birth_day");
			L.i(TAG, "loginYuuQuu birth=" + birth);
			String email = dataInfo.getString("email");
			L.i(TAG, "loginYuuQuu email=" + email);
			String sex = dataInfo.getString("sex");
			L.i(TAG, "loginYuuQuu sex=" + sex);
			String openId = dataInfo.getString("openid");
			L.i(TAG, "loginYuuQuu openId=" + openId);
			String introduction = dataInfo.getString("introduction");
			L.i(TAG, "loginYuuQuu introduction=" + introduction);
			String headUrlPath = dataInfo.getString("head") + "/100";
			L.i(TAG, "loginYuuQuu headUrlPath=" + headUrlPath);

			clientIp = Configuration.wifiIp;
			L.i(TAG, "loginYuuQuu clientIp=" + clientIp);

			Friends_API friends_API = new Friends_API();
			String friends_str = friends_API.add(oauth, "json",
					WeiBoUtil.QQ_YuuQuu, clientIp);
			L.i(TAG, "loginYuuQuu friends_str=" + friends_str);
			// 发表微博 tapi.add(oauth, "json", content, clientIp, "", "");
			// 发表图片 tapi.add_pic(oauth, "json", content, clientIp, picPath);

			// API注册
			loginResponse = APIRequestServers.registerByThirdParty(
					LoginActivity.loginActivity, openId, WeiBoUtil.QQ, name,
					sex, headUrlPath, access_Token, access_Token_secret);
			if (loginResponse.getResultCode() == 1) {
				String session_key = loginResponse.getResult().toString();
				L.i(TAG, "loginYuuQuu session_key=" + session_key);

				new UserBusinessDatabase(TenCentOAuthActivity.this)
						.delete(session_key);
				UserBean userBean = new UserBean("", "", session_key, "yes");
				userBean.setAccountType(WeiBoUtil.QQ);
				userBean.setOpenId(openId);
				userBean.setName(name);
				userBean.setSex(sex);
				userBean.setHeadUrlPath(headUrlPath);
				userBean.setAccess_Token(access_Token);
				userBean.setAccess_Token_secret(access_Token_secret);

				LoginActivity.loginActivity.weiboSaveInfo(session_key);// userBean,
				LoginActivity.loginActivity.sinaHandler.sendEmptyMessage(1);
			} else {
				L.i(TAG, "loginYuuQuu 登录失败。。。");
				LoginActivity.loginActivity.sinaHandler.sendEmptyMessage(0);
			}
		} catch (Exception e) {
			LoginActivity.loginActivity.sinaHandler.sendEmptyMessage(0);
			e.printStackTrace();
		}
	}

	/**
	 * 捕捉键盘事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
		// webView.goBack();
		// return true;
		// }
		Intent request = new Intent(TenCentOAuthActivity.this,
				LoginActivity.class);
		startActivity(request);
		finish();
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent request = new Intent(TenCentOAuthActivity.this,
					LoginActivity.class);
			startActivity(request);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// // if (webView.canGoBack()) {
	// // webView.goBack();
	// // } else {
	// Intent request = new Intent(TenCentOAuthActivity.this,
	// LoginActivity.class);
	// startActivity(request);
	// finish();
	// // }
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	//
	// }

}
