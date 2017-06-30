package com.datacomo.mc.spider.android.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.provider.Settings;

public class IsNetworkConnected {

	/**
	 * 判断网络是否连接
	 * 
	 * @return
	 */
	public static boolean checkNetworkInfo(Context context) {
		try {
			ConnectivityManager conMan = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			// mobile 3G Data Network
			State mobile = conMan.getNetworkInfo(
					ConnectivityManager.TYPE_MOBILE).getState();
			// wifi
			State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.getState();
			// 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
			if (mobile == State.CONNECTED)
				return true;
			if (wifi == State.CONNECTED)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 如果没有网络连接调用次方法 设置网络连接
	 * 
	 * @param isClose
	 *            是否关闭应用程序
	 */
	public static void settingNetWork(final Context context) {
		new AlertDialog.Builder(context).setTitle("无法连接网络")
				.setMessage("请检查手机的网络设置")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Intent intent = new Intent(
								Settings.ACTION_WIRELESS_SETTINGS);
						context.startActivity(intent);
					}
				}).setNegativeButton("取消", null).show();
	}

}
