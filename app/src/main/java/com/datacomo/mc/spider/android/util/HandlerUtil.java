package com.datacomo.mc.spider.android.util;

import android.os.Handler;
import android.os.Message;

public class HandlerUtil {
	public static  void sendMsgToHandler(Handler handler, int what, Object obj, int arg) {
		handler.sendMessage(getMessage(what, obj, arg, 0));
	}
	
	public static  void sendMsgToHandler(Handler handler, int what, Object obj, int arg1, int arg2) {
		handler.sendMessage(getMessage(what, obj, arg1, arg2));
	}

	public static void sendMsgToHandler(Handler handler, Message msg) {
		handler.sendMessage(msg);
	}

	public static Message getMessage(int what, Object obj, int arg1, int arg2) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		return msg;
	}
}
