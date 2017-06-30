package com.datacomo.mc.spider.android.location;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.datacomo.mc.spider.android.net.HttpRequestServers;
import com.datacomo.mc.spider.android.url.L;

public class MoreLocationInfo {
	private static final String TAG = "MoreLocationInfo";

	/**
	 * 通过腾讯微薄开放接口获取更多位置
	 * 
	 * @return
	 */
	public ArrayList<String> getMoreLocation(Double mLon, Double mlat) {
		ArrayList<String> locList = new ArrayList<String>();
		String url = "http://ugc.map.soso.com/rgeoc/?lnglat=%s,%s&reqsrc=wb";
		url = String.format(url, mLon, mlat);
		try {
			L.d(TAG, "getMoreLocation url=" + url);
			String locs = readData(new HttpRequestServers().getRequest(url));
			L.d(TAG, "getMoreLocation locs=" + locs);
			if (locs != null && !"".equals(locs)) {
				JSONObject jsonObject = new JSONObject(locs);
				JSONObject detailObject = jsonObject.getJSONObject("detail");
				JSONArray resultsArray = detailObject.getJSONArray("results");
				if (resultsArray != null) {
					for (int i = 0; i < resultsArray.length(); i++) {
						JSONObject object = resultsArray.getJSONObject(i);
						String address = object.getString("name");
						if (address != null && !address.equals("")) {
							locList.add(address);
						}
					}
				}
				JSONArray poilistArray = detailObject.getJSONArray("poilist");
				if (poilistArray != null) {
					for (int i = 0; i < poilistArray.length(); i++) {
						JSONObject object = poilistArray.getJSONObject(i);
						String address = object.getString("name");
						if (address != null && !address.equals("")) {
							locList.add(address);
						}
						String addr = object.getString("addr");
						if (addr != null && !addr.equals("")) {
							locList.add(addr);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < locList.size(); i++) {
			L.d(TAG, "getMoreLocation " + i + " " + locList.get(i));
		}
		return locList;
	}

	/**
	 * 读取请求数据
	 * 
	 * @param inSream
	 * @param charsetName
	 * @return
	 * @throws Exception
	 */
	private String readData(InputStream inSream) throws Exception {
		ByteArrayOutputStream outStream = null;
		String str = null;
		try {
			outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = inSream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			byte[] data = outStream.toByteArray();
			str = new String(data, "gbk");
		} finally {
			if (outStream != null)
				outStream.close();
			if (inSream != null)
				inSream.close();
		}
		return str;
	}
}
