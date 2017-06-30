package com.datacomo.mc.spider.android.util;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.datacomo.mc.spider.android.application.App;

public class BaseData {
	public static int getScreenWidth() {
		return App.app.share.getIntMessage("program", "screen_width", 0);
	}

	public static int getScreenHeight() {
		return App.app.share.getIntMessage("program", "screen_height", 0);
	}

	/**
	 * header高度 in px
	 * 
	 * @param context
	 * @return
	 */
	public static int getHeaderHeight(Context context) {
		return App.app.share.getIntMessage("program", "header_height",
				DensityUtil.dip2px(context, 43.5f));
	}

	/**
	 * 状态栏高度 in px
	 * 
	 * @param context
	 * @return
	 */
	public static int getStateBarHeight(Context context) {
		return App.app.share.getIntMessage("program", "state_bar_height",
				setStateBarHeight(context));
	}

	public static int setStateBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		App.app.share.saveIntMessage("program", "state_bar_height", sbar);
		return sbar;
	}

	/**
	 * 隐藏界面键盘
	 */
	public static void hideKeyBoard(Activity context) {
		try {
			((InputMethodManager) context
					.getSystemService(Activity.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(context.getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 隐藏对话框键盘
	 */
	public static void hideKeyViewBoard(Activity context, View editView) {
		try {
			((InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(editView.getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示键盘
	 */
	public static void showKeyBoard(Activity context, View editView) {
		try {
			editView.requestFocus();
			((InputMethodManager) context
					.getSystemService(Activity.INPUT_METHOD_SERVICE))
					.showSoftInput(editView, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
