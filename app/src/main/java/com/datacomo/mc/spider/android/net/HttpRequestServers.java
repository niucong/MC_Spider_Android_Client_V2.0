package com.datacomo.mc.spider.android.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.MonitorLog;

/**
 * Http请求服务器
 * 
 * @author datacomo-160
 * 
 */
public class HttpRequestServers {
	private final static String TAG = "HttpRequestServers";

	/**
	 * 客户端调用API的GET请求方式
	 * 
	 * @param urlstr
	 * @return
	 * @throws MalformedURLException
	 * @throws ProtocolException
	 * @throws IOException
	 */
	public InputStream getRequest(String urlstr) {
		// HttpURLConnection conn = null;
		long time1 = System.currentTimeMillis();
		boolean noException = true;
		try {
			// String url = urlstr.substring(0, urlstr.indexOf("?"));
			// String param = urlstr.substring(urlstr.indexOf("?") + 1);
			// L.d(TAG, "getRequest url=" + url + ",param=" + param);
			// ArrayList<NameValuePair> parameters = null;
			// if (param != null && param.contains("&")) {
			// String[] ps = param.split("&");
			// parameters = new ArrayList<NameValuePair>();
			// for (String pstr : ps) {
			// if (pstr != null && pstr.contains("=")) {
			// parameters.add(new BasicNameValuePair(pstr.substring(0,
			// pstr.indexOf("=")), pstr.substring(pstr
			// .indexOf("=") + 1)));
			// }
			// }
			// }
			return HTTPTools.getRequest(urlstr);

			// URL url = new URL(urlstr);
			//
			// conn = getHttpURLConnection(url);
			// // 设置连接超时时间
			// conn.setConnectTimeout(10 * 1000);
			// // 设置数据读取超时时间
			// conn.setReadTimeout(15 * 1000);
			// conn.setRequestMethod("GET");// 以get方式发起请求
			// // int code = conn.getResponseCode();
			// // L.i(TAG, "getRequest : code = " + code);
			// // L.d(TAG, "getRequest : length = " + conn.getContentLength());
			// conn.setDoInput(true);
			// conn.connect();
			// return conn.getInputStream();// 得到网络返回的输入流
			// } catch (MalformedURLException e) {
			// MonitorLog.saveLog(urlstr, "MalformedURLException");
			// e.printStackTrace();
			// } catch (ProtocolException e) {
			// MonitorLog.saveLog(urlstr, "ProtocolException");
			// e.printStackTrace();
			// } catch (HttpHostConnectException e) {
			// MonitorLog.saveLog(urlstr, "HttpHostConnectException");
			// e.printStackTrace();
		} catch (SocketTimeoutException e) {
			noException = false;
			MonitorLog.saveLog(urlstr, "SocketTimeoutException");
			e.printStackTrace();
			// } catch (KeyManagementException e) {
			// MonitorLog.saveLog(urlstr, "KeyManagementException");
			// e.printStackTrace();
			// } catch (NoSuchAlgorithmException e) {
			// MonitorLog.saveLog(urlstr, "NoSuchAlgorithmException");
			// e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			noException = false;
			MonitorLog.saveLog(urlstr, "UnsupportedEncodingException");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			noException = false;
			MonitorLog.saveLog(urlstr, "UnknownHostException");
			e.printStackTrace();
		} catch (IOException e) {
			noException = false;
			MonitorLog.saveLog(urlstr, "IOException");
			e.printStackTrace();
		} catch (Exception e) {
			noException = false;
			MonitorLog.saveLog(urlstr, "Exception");
			e.printStackTrace();
		} finally {
			long time2 = System.currentTimeMillis();
			L.i(TAG, "getRequest time1=" + time1 + ",time2=" + time2);
			if (noException && time2 - time1 > 5000) {
				MonitorLog.saveLog(urlstr, time2 - time1 + "");
			} else {
				MonitorLog.saveLog(urlstr, "0");
			}
			// try {
			// if (conn != null && Build.VERSION.SDK_INT >= 14)
			// conn.disconnect();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		}
		return null;
	}

	public InputStream postRequest(String url, String param)
			throws ProtocolException, MalformedURLException, IOException,
			SocketTimeoutException, Exception {
		// ArrayList<NameValuePair> parameters = null;
		// if (param != null && param.contains("&")) {
		// String[] ps = param.split("&");
		// parameters = new ArrayList<NameValuePair>();
		// for (String pstr : ps) {
		// if (pstr != null && pstr.contains("=")) {
		// parameters.add(new BasicNameValuePair(pstr.substring(0,
		// pstr.indexOf("=")), pstr.substring(pstr
		// .indexOf("=") + 1)));
		// }
		// }
		// }
		// return HTTPTools.invoke(url, parameters);
		// String apn = null;
		// try {
		// apn = new SoftPhoneInfo(context).getNetworkAPN();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// L.d(TAG, "postRequest apn=" + apn);
		// if (apn != null && apn.endsWith("wap")) {

		// try {
		return sendPost(url, param);
		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// } catch (ProtocolException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// } else {
		// return sendPost0(url, param);
		// }
	}

	private static CookieStore cookieStore;// 定义一个Cookie来保存session

	public InputStream httpClientGet(String urlstr)
			throws ClientProtocolException, IOException {
		// String result = "";
		// // 得到HttpClient对象
		// HttpClient getClient = new DefaultHttpClient();
		// // 得到HttpGet对象
		// HttpGet request = new HttpGet(urlstr);
		// // 客户端使用GET方式执行请教，获得服务器端的回应response
		// HttpResponse response = getClient.execute(request);
		// // 判断请求是否成功
		// if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
		// // 获得输入流
		// InputStream inSream = response.getEntity().getContent();
		// result = StreamUtil.readData(inSream);
		// // 关闭输入流
		// inSream.close();
		// }

		// BufferedReader br = null;
		// InputStream is = null;
		// try {
		HttpGet httpGet = new HttpGet(urlstr);
		HttpParams hp = httpGet.getParams();
		hp.getParameter("true");
		DefaultHttpClient hc = new DefaultHttpClient();
		if (cookieStore != null) {
			hc.setCookieStore(cookieStore);
		}
		HttpResponse ht = hc.execute(httpGet);
		if (ht.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity he = ht.getEntity();
			cookieStore = hc.getCookieStore();
			return he.getContent();
			// br = new BufferedReader(new InputStreamReader(is));
			// String readLine = null;
			// while ((readLine = br.readLine()) != null) {
			// result = result + readLine;
			// }
		}
		// } finally {
		// try {
		// if (is != null)
		// is.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// try {
		// if (br != null)
		// br.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		return null;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public InputStream httpClientPost(String urlstr, String param)
			throws ClientProtocolException, IOException {
		// String result = "";
		// InputStream is = null;
		// BufferedReader br = null;
		// try {
		HttpPost httpPost = new HttpPost(urlstr);
		@SuppressWarnings("rawtypes")
		List params = null;
		if (params != null && params.contains("&")) {
			String[] ps = param.split("&");
			params = new ArrayList<BasicNameValuePair>();
			for (String pstr : ps) {
				if (pstr != null && pstr.contains("=")) {
					params.add(new BasicNameValuePair(pstr.substring(0,
							pstr.indexOf("=")), pstr.substring(pstr
							.indexOf("=") + 1)));
				}
			}
		} else if (param.contains("=")) {
			params = new ArrayList<BasicNameValuePair>();
			if (param != null && param.contains("=")) {
				params.add(new BasicNameValuePair(param.substring(0,
						param.indexOf("=")),
						param.substring(param.indexOf("=") + 1)));
			}
		}

		HttpEntity he = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		httpPost.setEntity(he);

		DefaultHttpClient hc = new DefaultHttpClient();
		// 添加Cookie
		if (cookieStore != null) {
			hc.setCookieStore(cookieStore);
		}
		HttpResponse ht = hc.execute(httpPost);
		// 连接成功
		if (ht.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity het = ht.getEntity();
			// 保存Cookie
			cookieStore = hc.getCookieStore();
			return het.getContent();
			// br = new BufferedReader(new InputStreamReader(is));
			// String readLine = null;
			// while ((readLine = br.readLine()) != null) {
			// result = result + readLine;
			// }
		}
		// } finally {
		// try {
		// if (is != null)
		// is.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// try {
		// if (br != null)
		// br.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		return null;
	}

	/**
	 * 客户端调用API的post请求方式
	 * 
	 * @param urlstr
	 * @return
	 * @throws MalformedURLException
	 * @throws ProtocolException
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private InputStream sendPost0(String urlstr, String param)
			throws MalformedURLException, ProtocolException, IOException,
			SocketTimeoutException {
		L.d(TAG, "postRequest : " + (urlstr + "?" + param).length()
				+ ",urlstr = " + urlstr + "?" + param);

		HttpURLConnection conn = null;
		InputStream is = null;

		try {
			URL url = new URL(urlstr + "?" + param);
			conn = (HttpURLConnection) url.openConnection();
			// 设置连接超时时间
			conn.setConnectTimeout(10 * 1000);
			// 设置数据读取超时时间
			conn.setReadTimeout(15 * 1000);
			conn.setRequestMethod("POST");// 以post方式发起请求
			int code = conn.getResponseCode();
			L.i(TAG, "getRequest : code = " + code);
			L.d(TAG, "getRequest : length = " + conn.getContentLength());

			is = conn.getInputStream();// 得到网络返回的输入流
		} finally {
			// try {
			// if (conn != null && Build.VERSION.SDK_INT >= 14)
			// conn.disconnect();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		}
		return is;
	}

	/**
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String postReq(String url, String params)
			throws UnsupportedEncodingException, ClientProtocolException,
			IOException, SocketTimeoutException {
		/* 建立HTTPost对象 */
		HttpPost httpRequest = new HttpPost(url);

		ArrayList<BasicNameValuePair> parameters = null;

		if (params != null && params.contains("&")) {
			String[] ps = params.split("&");
			parameters = new ArrayList<BasicNameValuePair>();
			for (String pstr : ps) {
				if (pstr != null && pstr.contains("=")) {
					parameters.add(new BasicNameValuePair(pstr.substring(0,
							pstr.indexOf("=")), pstr.substring(pstr
							.indexOf("=") + 1)));
				}
			}
		}

		/* 添加请求参数到请求对象 */
		httpRequest.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));

		/* 发送请求并等待响应 */
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(
				HttpConnectionParams.CONNECTION_TIMEOUT, 10 * 1000);
		client.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,
				15 * 1000);
		HttpResponse httpResponse = client.execute(httpRequest);

		Log.i(TAG, "uploadContacts statusCode : "
				+ httpResponse.getStatusLine().getStatusCode());
		return EntityUtils.toString(httpResponse.getEntity());
	}

	/**
	 * 向指定URL发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 * @throws ProtocolException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private InputStream sendPost(String url, String param) {
		L.d(TAG, "sendPost : " + (url + "?" + param).length() + ",url = " + url
				+ "?" + param);
		PrintWriter out = null;
		HttpURLConnection conn = null;
		long time1 = System.currentTimeMillis();
		boolean noException = true;
		try {
			// ArrayList<NameValuePair> parameters = null;
			// if (param != null && param.contains("&")) {
			// String[] ps = param.split("&");
			// parameters = new ArrayList<NameValuePair>();
			// for (String pstr : ps) {
			// if (pstr != null && pstr.contains("=")) {
			// parameters.add(new BasicNameValuePair(pstr.substring(0,
			// pstr.indexOf("=")), pstr.substring(pstr
			// .indexOf("=") + 1)));
			// }
			// }
			// }
			// return HTTPTools.postRequest(url, parameters);

			URL realUrl = new URL(url);
			conn = getHttpURLConnection(realUrl);
			// 设置连接超时时间
			conn.setConnectTimeout(10 * 1000);
			// 设置数据读取超时时间
			conn.setReadTimeout(15 * 1000);
			// 设置通用的请求属性
			// 设置文件类型:
			conn.setRequestProperty("accept", "*/*");
			// 设置维持长连接:
			conn.setRequestProperty("connection", "Keep-Alive");
			// 设置文件字符集:
			conn.setRequestProperty("Charset", "UTF-8");
			// 设置文件长度:
			conn.setRequestProperty("Content-Length",
					String.valueOf(param.length()));

			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 发送POST请求必须设置如下两行
			// 设置容许输出:
			conn.setDoOutput(true);
			// 设置容许输入:
			conn.setDoInput(true);

			// 设置使用POST的方式发送:
			conn.setRequestMethod("POST");

			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(param.getBytes("UTF-8"));
			outputStream.close();
			conn.connect();
			return conn.getInputStream();
			// }
			// } catch (MalformedURLException e) {
			// MonitorLog.saveLog(url + "?" + param, "MalformedURLException");
			// e.printStackTrace();
			// } catch (ProtocolException e) {
			// MonitorLog.saveLog(url + "?" + param, "ProtocolException");
			// e.printStackTrace();
			// } catch (HttpHostConnectException e) {
			// MonitorLog.saveLog(url + "?" + param,
			// "HttpHostConnectException");
			// e.printStackTrace();
		} catch (SocketTimeoutException e) {
			noException = false;
			MonitorLog.saveLog(url + "?" + param, "SocketTimeoutException");
			e.printStackTrace();
			// } catch (KeyManagementException e) {
			// MonitorLog.saveLog(url + "?" + param, "KeyManagementException");
			// e.printStackTrace();
			// } catch (NoSuchAlgorithmException e) {
			// MonitorLog.saveLog(url + "?" + param,
			// "NoSuchAlgorithmException");
			// e.printStackTrace();
		} catch (UnknownHostException e) {
			noException = false;
			MonitorLog.saveLog(url + "?" + param, "UnknownHostException");
			e.printStackTrace();
		} catch (IOException e) {
			noException = false;
			MonitorLog.saveLog(url + "?" + param, "IOException");
			e.printStackTrace();
		} catch (Exception e) {
			noException = false;
			MonitorLog.saveLog(url + "?" + param, "Exception");
			e.printStackTrace();
		} finally {
			long time2 = System.currentTimeMillis();
			L.i(TAG, "sendPost time1=" + time1 + ",time2=" + time2);
			if (noException && time2 - time1 > 5000) {
				MonitorLog.saveLog(url + "?" + param, time2 - time1 + "");
			} else {
				MonitorLog.saveLog(url + "?" + param, "0");
			}

			try {
				if (out != null)
					out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// try {
			// if (conn != null && Build.VERSION.SDK_INT >= 14)
			// conn.disconnect();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		}
		return null;
	}

	/**
	 * 
	 * @param url
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static HttpURLConnection getHttpURLConnection(URL url)
			throws KeyManagementException, NoSuchAlgorithmException,
			IOException {
		// if (Build.VERSION.SDK_INT < 14)
		// FakeX509TrustManager.allowAllSSL();

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (conn instanceof HttpsURLConnection) {
			// Trust all certificates
			SSLContext context = SSLContext.getInstance("TLS");
			X509TrustManager[] xtmArray = new X509TrustManager[] { xtm };
			context.init(new KeyManager[0], xtmArray, new SecureRandom());
			SSLSocketFactory socketFactory = context.getSocketFactory();
			((HttpsURLConnection) conn).setSSLSocketFactory(socketFactory);
			((HttpsURLConnection) conn).setHostnameVerifier(HOSTNAME_VERIFIER);
		}
		// if (Build.VERSION.SDK_INT < 14) {
		// conn.setRequestProperty("Transfer-Encoding", "chunked");
		// }
		return conn;
	}

	private static final AllowAllHostnameVerifier HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
	private static X509TrustManager xtm = new X509TrustManager() {
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};
}
