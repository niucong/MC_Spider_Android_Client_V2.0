package com.datacomo.mc.spider.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.datacomo.mc.spider.android.R;

public class LogicUtil {

	public static void enter(Context context, Class<?> cls, Bundle bundle,
			boolean isFinish) {
		try {
			if (null == context || null == cls) {
				return;
			}
			Intent intent = new Intent(context, cls);
			if (null != bundle)
				intent.putExtras(bundle);
			context.startActivity(intent);
			if (isFinish) {
				((Activity) context).finish();
			}
			((Activity) context).overridePendingTransition(R.anim.push_rihgt_in,
					R.anim.push_left_out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void enter(Context context, Class<?> cls, Bundle bundle,
			int requestCode) {
		try {
			if (null == context || null == cls) {
				return;
			}
			Intent intent = new Intent(context, cls);
			if (null != bundle)
				intent.putExtras(bundle);
			((Activity) context).startActivityForResult(intent, requestCode);
			((Activity) context).overridePendingTransition(R.anim.push_rihgt_in,
					R.anim.push_left_out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void finish(Context context) {
		try {
			((Activity) context).finish();
			showFinishAni(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showEnterAni(Context mContext) {
		try {
			((Activity) mContext).overridePendingTransition(R.anim.push_rihgt_in,
					R.anim.push_left_out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showFinishAni(Context mContext) {
		try {
			((Activity) mContext).overridePendingTransition(R.anim.push_left_in,
					R.anim.push_rihgt_out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 本地存图片去掉后缀
	 * 
	 * @param cls
	 * @param bundle
	 * @param isFinish
	 */
	public static String getLocalName(String path) {
		try {
			if (path != null && !"".equals(path) && path.contains(".")) {
				return path.substring(0, path.lastIndexOf("."));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}

}
