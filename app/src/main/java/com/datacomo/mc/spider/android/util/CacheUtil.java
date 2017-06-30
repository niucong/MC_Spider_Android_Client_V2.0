package com.datacomo.mc.spider.android.util;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.datacomo.mc.spider.android.db.ChatGroupMessageBeanService;
import com.datacomo.mc.spider.android.db.ChatMessageBeanService;
import com.datacomo.mc.spider.android.db.FileUrlService;
import com.datacomo.mc.spider.android.db.MailInfoService;
import com.datacomo.mc.spider.android.db.MessageNoticeBeanService;
import com.datacomo.mc.spider.android.db.QuuBoInfoService;
import com.datacomo.mc.spider.android.url.L;

public class CacheUtil {
	private final String TAG = "CacheUtil";

	/**
	 * 清除缓存
	 * 
	 * @param context
	 * @param cleanCook
	 *            是否清除Cook
	 */
	@SuppressLint("SdCardPath")
	public void cleanCachePhoto(Context context) {
		try {
			CookieSyncManager.createInstance(context);
			CookieManager.getInstance().removeAllCookie();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 左侧导航一级页面本地化数据
		File df = new File("/data/data/" + context.getPackageName() + "/files");
		if (df != null && df.exists()) {
			try {
				Runtime.getRuntime().exec("rm -r " + df);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 与某人邮件本地化数据
		File ml = new File("/data/data/" + context.getPackageName()
				+ "/shared_prefs/mail_list.xml");
		if (ml != null && ml.exists()) {
			ml.delete();
		}

		// 圈聊本地化数据
		ChatGroupMessageBeanService.getService(context).deleteAll();
		// 私信本地化数据
		ChatMessageBeanService.getService(context).deleteAll();
		// 通知本地化数据
		MessageNoticeBeanService.getService(context).deleteAll();
		// 邮件详情本地化数据
		MailInfoService.getService(context).deleteAll();
		// 圈博详情本地化数据
		QuuBoInfoService.getService(context).deleteAll();
		// 圈博文件下载地址本地化数据
		FileUrlService.getService(context).deleteAll();

		String[] folder = { ConstantUtil.HEAD_PATH, ConstantUtil.IMAGE_PATH,
				ConstantUtil.POSTER_PATH, ConstantUtil.VOICE_PATH,
				ConstantUtil.TEMP_PATH, ConstantUtil.CACHE_PHOTO_PATH };
		long lon = 3 * 24 * 60 * 60 * 1000L;
		for (String path : folder) {
			File imgFile = new File(path);
			File[] files = imgFile.listFiles();
			if (null != files) {
				for (File file : files) {
					L.d(TAG, "cleanCachePhoto " + file.getPath());
					L.d(TAG, "cleanCachePhoto " + System.currentTimeMillis()
							+ " " + file.lastModified());
					try {
						if (file.length() == 0
								|| (System.currentTimeMillis()
										- file.lastModified() > lon)) {
							file.delete();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		File headFile = new File(ConstantUtil.UPLOAD_HEAD_PATH);
		if (headFile != null && headFile.exists()) {
			try {
				Runtime.getRuntime().exec("rm -r " + headFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			context.deleteDatabase("webview.db");
			context.deleteDatabase("webviewCache.db");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
