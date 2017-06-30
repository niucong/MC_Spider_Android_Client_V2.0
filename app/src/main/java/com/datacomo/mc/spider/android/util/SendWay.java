package com.datacomo.mc.spider.android.util;

public class SendWay {

	/**
	 * 资源发布方式 1、client=web（电脑）2、client=iphone（iPhone客户端）
	 * 3、client=android（Android客户端）4、android（Android）
	 * 5、ipad（iPad）6、iphone(iPhone)7、wap(手机版)
	 * 
	 * @param way
	 */
	public static String resoureSendWay(String way) {
		String sendWay = "";// "来自";
		if (way != null && !"".equals(way)) {
			// way转化为小写
			way = way.toLowerCase();
			if ("client=web".equals(way) || "web".equals(way)) {
				sendWay += "电脑";
			} else if ("client=iphone".equals(way)) {
				sendWay += "iPhone客户端";
			} else if ("client=android".equals(way)) {
				sendWay += "Android客户端";
			} else if ("android".equals(way)) {
				sendWay += "Android";
			} else if ("ipad".equals(way)) {
				sendWay += "iPad";
			} else if ("iphone".equals(way)) {
				sendWay += "iPhone";
			} else if ("client=sms".equals(way) || "sms".equals(way)) {
				sendWay += "短信";
			} else if ("client=wap".equals(way) || "wap".equals(way)) {
				sendWay += "手机版";
			} else if ("from_notepad".equals(way)) {
				sendWay += "云笔记";
			} else {
				sendWay += "电脑";
			}
		}
		return sendWay;
	}

	/**
	 * 资源发布方式 1、client=web（电脑）2、client=iphone（iPhone客户端）
	 * 3、client=android（Android客户端）4、android（Android）
	 * 5、ipad（iPad）6、iphone(iPhone)7、wap(手机版)
	 * 
	 * @param way
	 */
	public static String loginWay(String way) {
		String loginWay = "";
		if (way != null && !"".equals(way)) {
			// way转化为小写
			way = way.toLowerCase();
			if ("web".equals(way)) {
				loginWay += "电脑";
			} else if ("iphone".equals(way)) {
				loginWay += "iPhone客户端";
			} else if ("android".equals(way)) {
				loginWay += "安卓客户端";
			} else if ("touch".equals(way)) {
				loginWay += "手机";
			} else if ("ipad".equals(way)) {
				loginWay += "iPad";
			} else {
				loginWay += "电脑";
			}
		} else {
			loginWay += "电脑";
		}
		return loginWay;
	}

}
