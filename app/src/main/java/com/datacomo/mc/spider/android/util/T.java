package com.datacomo.mc.spider.android.util;

import android.app.AlertDialog;
import android.content.Context;

import com.datacomo.mc.spider.android.dialog.ShowToast;

public class T {
	// private static final boolean flag = true;
	public static final String ErrStr = "网络不稳定，请稍后再试！";
	public static final String TimeOutStr = "网络连接超时";

	public static void show(Context context, String text) {
		// if (flag) {
		if (text != null && !text.equals(ErrStr)) {
			// Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			ShowToast.getToast(context).show(text);
		}
		// }
	}

	public static void show(Context context, int id) {
		// if (flag) {
		String text = context.getResources().getString(id);
		if (text != null) {
			ShowToast.getToast(context).show(text);
		}
		// }
	}

	public static void dialog(Context context, String text) {
		new AlertDialog.Builder(context).setTitle("提示").setMessage(text)
				.setPositiveButton("我知道了", null).show();
	}
}
