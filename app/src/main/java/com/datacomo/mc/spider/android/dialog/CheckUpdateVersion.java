package com.datacomo.mc.spider.android.dialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.datacomo.mc.spider.android.InfoWallActivity;
import com.datacomo.mc.spider.android.SettingActivity;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.service.DownLoadFileThread;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.FileUtil;

public class CheckUpdateVersion {
	private static final String TAG = "CheckUpdateVersion";

	private Context context;
	private static Map<String, String> version;

	public CheckUpdateVersion(Context context) {
		this.context = context;
	}

	/**
	 * 检测版本
	 * 
	 * @throws Exception
	 */
	public boolean updateVersion() throws Exception {
		version = APIRequestServers.getNewVersion(App.app);

		if (version == null) {
			return false;
		}

		String isUpdate = version.get("isUpdate");
		L.d(TAG, "updateVersion isUpdate : " + isUpdate);
		if (isUpdate != null && "true".equals(isUpdate)) {
			return true;
		}
		return false;
	}

	/**
	 * 提示新版本对话框
	 */
	@SuppressLint("SimpleDateFormat")
	public void versionDialog() {
		try {
			final String versionUrl = version.get("fileUrl");
			L.d(TAG, "versionDialog versionUrl = " + versionUrl);
			String versionDesc = version.get("versionDesc");
			String msg = "";
			if (versionDesc.contains("#")) {
				String[] descs = versionDesc.split("#");
				for (String desc : descs) {
					msg = msg + desc + "\n";
				}
				msg = msg.substring(0, msg.length() - 1);
			} else {
				msg = versionDesc;
			}

			final String versionName = version.get("versionName");
			new AlertDialog.Builder(context)
					.setTitle("检测到新版本 V" + versionName)
					.setMessage("新版本更新\n" + msg)
					.setCancelable(false)
					.setPositiveButton("升级",
							new OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									String filename = versionUrl.substring(
											versionUrl.lastIndexOf("/") + 1,
											versionUrl.lastIndexOf("."))
											// + versionName
											+ ".apk";
									String filename2 = versionUrl.substring(
											versionUrl.lastIndexOf("/") + 1,
											versionUrl.lastIndexOf("."))
											+ versionName + ".apk";
									File myFile = new File(
											ConstantUtil.DOWNLOAD_PATH
													+ filename2);
									L.d(TAG, "versionDialog filename2 = "
											+ filename2);

									if (myFile != null && myFile.exists()) {
										L.d(TAG, "versionDialog myFile = "
												+ myFile.length());
										new FileUtil()
												.openFile(context, myFile);
										SettingActivity.versionThreadRun = false;
										return;
									}
									L.d(TAG, "versionDialog filename2 = "
											+ filename2);
									new DownLoadFileThread(context, versionUrl,
											0, filename, versionName).start();
								}
							}).setNegativeButton("稍后再说", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							L.d(TAG, "versionDialog versionName3 = "
									+ versionName);
							SettingActivity.versionThreadRun = false;
						}
					}).show();
			InfoWallActivity.versionDate = new SimpleDateFormat("yyyy-MM-dd")
					.format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
