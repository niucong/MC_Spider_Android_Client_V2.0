package com.datacomo.mc.spider.android.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.datacomo.mc.spider.android.url.L;

/**
 * 处理链接工具类
 */
public class LinkDealUtil {
	private static final String TAG = "LinkDealUtil";

	public final static String FORMAT_LINK_START_PARAMTER = "[$LINK]";
	public final static String FORMAT_LINK_END_PARAMTER = "[LINK$]";

	/**
	 * 截取链接
	 * 
	 * @param str
	 * @return
	 */
	public static String subLink(String str) {
		return str.substring(0,
				str.indexOf(LinkDealUtil.FORMAT_LINK_START_PARAMTER));
	}

	/**
	 * 处理链接
	 * 
	 * @param str
	 * @return
	 */
	public static String dealLink(String str) {
		List<String> list = aa(str);
		String msg = "";
		for (String s : list) {
			try {
				msg += bb(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return msg;
	}

	private static String bb(String msg) throws Exception {
		L.d(TAG, "bb msg=" + msg);
		String str = "<div><img src='%s' style=\"height:18;width:18;margin-top:3;\" onerror='this.src=\"file:///android_asset/ioc_link.png\"'> <a style='color:#19a97b;' href='%s'>%s</a><span>%s</span></div>";
		JSONObject object = new JSONObject(msg);

		return String.format(str, object.getString("linkIcon"),
				object.getString("linkUrl"), object.getString("linkName"),
				object.getString("linkDesc"));
	}

	private static List<String> aa(String message) {
		List<String> messageList = new ArrayList<String>();
		String temp = "";
		while (message.indexOf(FORMAT_LINK_START_PARAMTER) != -1) {
			temp = message.substring(
					message.indexOf(FORMAT_LINK_START_PARAMTER)
							+ FORMAT_LINK_START_PARAMTER.length(),
					message.indexOf(FORMAT_LINK_END_PARAMTER));
			messageList.add(temp);
			message = message.replace(FORMAT_LINK_START_PARAMTER + temp
					+ FORMAT_LINK_END_PARAMTER, "");
		}
		return messageList;
	}
}
