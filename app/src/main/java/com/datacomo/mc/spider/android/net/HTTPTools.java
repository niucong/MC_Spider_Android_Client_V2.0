/**
 * 与服务器通信交互类
 */
package com.datacomo.mc.spider.android.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;

import android.net.http.AndroidHttpClient;

public class HTTPTools {
	// private static final String TAG = "HTTPtools";

	private static CookieStore cookieStore;// 定义一个Cookie来保存session

	/**
	 * 向服务器发送URL请求 获得返回数据
	 * 
	 * @param doUrl
	 *            stauts的类名
	 * @param actionName
	 *            方法名
	 * @param params
	 *            传递的参数
	 * @return 获得返回的JSON结果
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 *             // * @throws ClientProtocolException
	 */
	public static InputStream postRequest(String url, List<NameValuePair> params)
			throws UnsupportedEncodingException, IOException, Exception {
		AndroidHttpClient httpClient = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			if (params != null && params.size() > 0) {
				HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
				httpPost.setEntity(entity);
			}
			BasicHttpContext context = new BasicHttpContext();
			if (cookieStore == null) {
				cookieStore = new BasicCookieStore();
			}
			context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			httpClient = AndroidHttpClient.newInstance("");
			HttpResponse httpResponse = httpClient.execute(httpPost, context);
			// DefaultHttpClient httpClient = new DefaultHttpClient();
			// // 添加Cookie
			// if (cookieStore != null) {
			// httpClient.setCookieStore(cookieStore);
			// }
			// HttpResponse httpResponse = httpClient.execute(httpPost);
			// cookieStore = ((AbstractHttpClient) httpClient).getCookieStore();
			return httpResponse.getEntity().getContent();
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}
		}
	}

	/**
	 * 向服务器发送URL请求 获得返回数据
	 * 
	 * @param doUrl
	 *            stauts的类名
	 * @param actionName
	 *            方法名
	 * @param params
	 *            传递的参数
	 * @return 获得返回的JSON结果
	 * @throws IOException
	 *             // * @throws ClientProtocolException
	 */
	public static InputStream getRequest(String url) throws IOException,
			Exception {
		AndroidHttpClient httpClient = null;
		try {
//			@SuppressWarnings("deprecation")
//			Protocol myhttps = new Protocol("https",
//					new MySecureProtocolSocketFactory(), 443);
//			Protocol.registerProtocol("https", myhttps);
			// 创建httpget.
			HttpGet httpget = new HttpGet(url);
			BasicHttpContext context = new BasicHttpContext();
			if (cookieStore == null) {
				cookieStore = new BasicCookieStore();
			}
			context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			httpClient = AndroidHttpClient.newInstance("");
			HttpResponse httpResponse = httpClient.execute(httpget, context);
			// httpClient.close();
			// AbstractHttpClient httpClient = new DefaultHttpClient();
			// // 添加Cookie
			// if (cookieStore != null) {
			// httpClient.setCookieStore(cookieStore);
			// }
			// HttpResponse httpResponse = httpClient.execute(httpget);
			// // 保存Cookie
			// cookieStore = ((AbstractHttpClient) httpClient).getCookieStore();
			return httpResponse.getEntity().getContent();
		} finally {
			// if (httpClient != null) {
			// httpClient.close();
			// }
		}
	}
}
