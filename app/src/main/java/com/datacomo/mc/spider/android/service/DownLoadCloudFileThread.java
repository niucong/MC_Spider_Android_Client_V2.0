package com.datacomo.mc.spider.android.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.ReLoadingActivity;
import com.datacomo.mc.spider.android.SettingActivity;
import com.datacomo.mc.spider.android.net.HttpRequestServers;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadCloudFileThread extends Thread {
	private final String TAG = "DownLoadCloudFileThread";

	private int id;
	private String fileUrl;
	private long fileLength;
	private String filename;
	private boolean downloadCloudFile;
	private long currentSize = 0;

	private Context context;

	// 通知栏
	private NotificationManager downloadNotificationManager = null;
	private Notification downloadNotification = null;
	// 通知栏跳转Intent
	private Intent downloadIntent = null;
	private PendingIntent downloadPendingIntent = null;
	private Notification.Builder builder;

	private String versionName;

	/**
	 * 普通文件下载
	 * 
	 * @param context
	 * @param fileUrl
	 * @param fileLength
	 * @param filename
	 */
	public DownLoadCloudFileThread(Context context, String fileUrl,
			long fileLength, String filename) {
		this.context = context;
		this.id = (int) System.currentTimeMillis();
		this.fileUrl = fileUrl;
		this.fileLength = fileLength;
		this.filename = filename;

		init();
	}

	/**
	 * 优优工作圈升级下载
	 * 
	 * @param context
	 * @param fileUrl
	 * @param fileLength
	 * @param filename
	 */
	public DownLoadCloudFileThread(Context context, String fileUrl,
			long fileLength, String filename, String versionName) {
		this.context = context;
		this.id = (int) System.currentTimeMillis();
		this.fileUrl = fileUrl;
		this.fileLength = fileLength;
		this.filename = filename;
		this.versionName = versionName;

		init();
	}

	/**
	 * 云文件下载
	 * 
	 * @param context
	 * @param fileUrl
	 * @param fileLength
	 * @param filename
	 * @param downloadCloudFile
	 */
	public DownLoadCloudFileThread(Context context, String fileUrl,
			long fileLength, String filename, boolean downloadCloudFile,
			long currentSize) {
		this.context = context;
		this.id = (int) System.currentTimeMillis();
		this.fileUrl = fileUrl;
		this.fileLength = fileLength;
		this.filename = filename;
		this.downloadCloudFile = downloadCloudFile;
		this.currentSize = currentSize;

		init();
	}

	@SuppressWarnings("deprecation")
	private void init() {
		if (filename == null || filename.equals("")) {
			try {
				this.filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (!filename.contains(".")) {
			this.filename = this.filename
					+ fileUrl.substring(fileUrl.lastIndexOf("."));
		}

		try {
			if (versionName != null && !"".equals(versionName)) {
				this.filename = filename
						.substring(0, filename.lastIndexOf("."))
						+ versionName
						+ filename.substring(filename.lastIndexOf("."));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		downloadNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		downloadNotification = new Notification();

		// 设置下载过程中，点击通知栏，跳到OpenFile.class
		downloadIntent = new Intent();
		downloadIntent.putExtra("fileName", filename);
		downloadPendingIntent = PendingIntent.getActivity(context, 0,
				downloadIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//		// 设置通知栏显示内容
//		downloadNotification.icon = R.drawable.icon;
//		downloadNotification.tickerText = "开始下载“" + filename + "”";
//		downloadNotification.setLatestEventInfo(context, "开始下载", filename,
//				downloadPendingIntent);
//		downloadNotificationManager.notify(id, downloadNotification);
		builder = new Notification.Builder(context).setTicker(
				"开始下载“" + filename + "”").setSmallIcon(R.drawable.icon);
		downloadNotification = builder.setContentIntent(downloadPendingIntent)
				.setContentTitle("开始下载").setContentText(filename).build();

		downloadNotificationManager.notify(id, downloadNotification);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		super.run();
		L.d(TAG, "run fileUrl = " + fileUrl);
		HttpURLConnection hrc = null;
		InputStream is = null;
		FileOutputStream fos = null;
		File proFile = null;
		try {
			URL url = new URL(fileUrl);
			hrc = HttpRequestServers.getHttpURLConnection(url);

			hrc.setRequestProperty("User-Agent", "NetBear");
			hrc.setRequestProperty("Content-type",
					"application/x-java-serialized-object");
			hrc.setRequestProperty("connection", "Keep-Alive");
			hrc.setConnectTimeout(20 * 1000);
			hrc.setReadTimeout(30 * 1000);

			String fileAbsolutePath = ConstantUtil.CLOUD_PATH;
			FileUtil.createFile(fileAbsolutePath);
			String downName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
			File myFile = new File(fileAbsolutePath + downName);

			double split = (double) fileLength / 100;
			L.i(TAG, "split : " + split);
			L.i(TAG, "fileLength : " + fileLength);
			int progress = 0;
			int length = 0;
			byte buf[] = new byte[1024];

			@SuppressWarnings("resource")
			RandomAccessFile randomAccessFile = new RandomAccessFile(
					fileAbsolutePath + downName + ".sp", "rwd");

			L.i(TAG, "currentSize : " + currentSize);
			if (fileLength == 0) {
				fileLength = hrc.getContentLength();
			}
			if (currentSize > 0) {
				hrc.setRequestProperty("Range", "bytes=" + currentSize + "-"
						+ fileLength);
				randomAccessFile.seek(currentSize);
			} else {
				randomAccessFile.setLength(fileLength);
			}

			is = hrc.getInputStream();
			progress = (int) (currentSize * 100 / fileLength);
			while ((length = is.read(buf)) != -1) {
				randomAccessFile.write(buf, 0, length);
				currentSize += length;
				int i = (int) (currentSize * 100 / fileLength);
				if (i > progress) {
					progress = i;
					// 设置通知显示的参数
//					downloadNotification.setLatestEventInfo(context, "已下载 "
//							+ progress + "%", filename, downloadPendingIntent);
					downloadNotification = builder
							.setContentIntent(downloadPendingIntent)
							.setContentTitle("已下载 " + progress + "%")
							.setContentText(filename).build();
					// 这个可以理解为开始执行这个通知
					downloadNotificationManager
							.notify(id, downloadNotification);
					L.i(TAG, "id=" + id + ",progress=" + progress
							+ ",currentSize=" + currentSize);
				}
			}

			proFile = new File(fileAbsolutePath + downName + ".sp");
			if (proFile != null && proFile.exists())
				proFile.renameTo(myFile);
			// 设置通知显示的参数
//			downloadNotification.setLatestEventInfo(context, "下载完成", filename,
//					downloadPendingIntent);
			downloadNotification = builder
					.setContentIntent(downloadPendingIntent)
					.setContentTitle("已下载 " + progress + "%")
					.setContentText(filename).build();
			// 这个可以理解为开始执行这个通知
			downloadNotificationManager.notify(id, downloadNotification);
			downloadNotificationManager.cancel(null, id);

			new FileUtil().openFile(context, myFile);

			context.sendBroadcast(new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(myFile)));
		} catch (Exception e) {
			e.printStackTrace();
			if (downloadCloudFile) {
				L.i(TAG, "currentSize : " + currentSize);
				// 设置下载过程中，点击通知栏，不回到主界面
				downloadIntent = new Intent(context, ReLoadingActivity.class);

				downloadIntent.putExtra("FileUrl", fileUrl);
				downloadIntent.putExtra("FileLength", fileLength);
				downloadIntent.putExtra("FileName", filename);
				downloadIntent.putExtra("IsDownLoad", true);
				downloadIntent.putExtra("currentSize", currentSize);

				downloadPendingIntent = PendingIntent.getActivity(context, 0,
						downloadIntent, 0);
				downloadNotification = new Notification();
				// 设置通知栏显示内容
				downloadNotification.flags |= Notification.FLAG_AUTO_CANCEL;
//				downloadNotification.icon = R.drawable.icon;
//				downloadNotification.tickerText = "“" + filename + "”下载失败！";
//				downloadNotification.setLatestEventInfo(context, "下载失败！",
//						filename, downloadPendingIntent);
				downloadNotification = builder
						.setContentIntent(downloadPendingIntent)
						.setContentTitle("下载失败！")
						.setContentText(filename).build();
				downloadNotificationManager.notify(id, downloadNotification);
			}
		} finally {
			if (versionName != null && !"".equals(versionName)) {
				SettingActivity.versionThreadRun = false;
			}

			if (proFile != null && proFile.exists())
				proFile.delete();
			try {
				if (is != null)
					is.close();
				if (fos != null)
					fos.close();
				if (hrc != null)
					hrc.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (downloadCloudFile) {
				try {
					ConstantUtil.downloadingList.remove(fileUrl
							.substring(fileUrl.lastIndexOf("/") + 1));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
