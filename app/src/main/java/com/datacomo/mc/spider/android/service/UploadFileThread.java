package com.datacomo.mc.spider.android.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.datacomo.mc.spider.android.BasicMenuActivity;
import com.datacomo.mc.spider.android.CloudFileActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.ReLoadingActivity;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.db.GroupListService;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.net.HttpRequestServers;
import com.datacomo.mc.spider.android.receiver.BootBroadcastReceiver;
import com.datacomo.mc.spider.android.receiver.SimpleReceiver;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.StreamUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadFileThread extends Thread {
	private final String TAG = "UploadFileThread";

	private Context context;
	private int id;
	private File file;
	private String uploadUrl;
	private String tip;
	private String groupId;

	// 通知栏
	private NotificationManager updateNotificationManager = null;
	private Notification updateNotification = null;
	// 通知栏跳转Intent
	private Intent updateIntent = null;
	private PendingIntent updatePendingIntent = null;
	private Notification.Builder builder;

	/**
	 * 文件、头像、海报、图片上传
	 * 
	 * @param context
	 * @param file
	 * @param uploadUrl
	 * @param tip
	 */
	public UploadFileThread(Context context, File file, String uploadUrl,
			String tip, String groupId) {
		this.context = context;
		this.id = (int) System.currentTimeMillis();
		this.file = file;
		this.uploadUrl = uploadUrl;
		this.tip = tip;

		this.groupId = groupId;

		updateNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		updateNotification = new Notification();

		// 设置下载过程中，点击通知栏，不回到主界面
		updateIntent = new Intent();
		updatePendingIntent = PendingIntent.getActivity(context, 0,
				updateIntent, 0);
		// 设置通知栏显示内容
		// updateNotification.flags |= Notification.FLAG_AUTO_CANCEL;
//		updateNotification.icon = R.drawable.icon;
//		updateNotification.tickerText = "开始上传“" + file.getName() + "”";
//		updateNotification.setLatestEventInfo(context, "开始上传" + tip,
//				file.getName(), updatePendingIntent);
		builder = new Notification.Builder(context).setTicker(
				"开始上传" + tip).setSmallIcon(R.drawable.icon);
		updateNotification = builder.setContentIntent(updatePendingIntent)
				.setContentTitle("开始上传" + tip).setContentText(file.getName()).build();
		updateNotificationManager.notify(id, updateNotification);
	}

	@Override
	public void run() {
		super.run();
		// String url = URLProperties.FILE_JSON;
		// String params = new UploadFileParams(context, "fileUpload",
		// file.getName(), null).getParams();
		// uploadUrl = url + "?" + params;
		L.i(TAG, "httpUpload uploadUrl=" + uploadUrl);
		try {
			httpUpload();
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(0);
		}
		ConstantUtil.uploadingList.remove(file.getPath());
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if ("个人头像".equals(tip)) {
					updateNotificationManager.notify(id, updateNotification);
					updateNotificationManager.cancel(null, id);
					T.show(context, "头像修改失败！");
				} else if ("圈子海报".equals(tip)) {
					updateNotificationManager.notify(id, updateNotification);
					updateNotificationManager.cancel(null, id);
					T.show(context, "海报修改失败！");
				} else if ("个人文件".equals(tip)) {
					// 设置下载过程中，点击通知栏，不回到主界面
					updateIntent = new Intent(context, ReLoadingActivity.class);
					updateIntent.putExtra("FilePath", file.getPath());
					updateIntent.putExtra("IsDownLoad", false);

					updatePendingIntent = PendingIntent.getActivity(context, 0,
							updateIntent, 0);
					updateNotification = new Notification();
					// 设置通知栏显示内容
					updateNotification.flags |= Notification.FLAG_AUTO_CANCEL;
//					updateNotification.icon = R.drawable.icon;
//					updateNotification.tickerText = "“" + file.getName()
//							+ "”上传失败！";
//					updateNotification.setLatestEventInfo(context, "上传失败！",
//							file.getName(), updatePendingIntent);
					updateNotification = builder
							.setContentIntent(updatePendingIntent)
							.setContentTitle("上传失败！")
							.setContentText(file.getName()).build();
					updateNotificationManager.notify(id, updateNotification);
				}
				break;
			case 1:
				updateNotificationManager.notify(id, updateNotification);
				updateNotificationManager.cancel(null, id);
				if ("个人头像".equals(tip)) {
					// T.show(context, "头像修改成功！");
				} else if ("圈子海报".equals(tip)) {
					// T.show(context, "海报修改成功！");
				} else if ("个人文件".equals(tip)) {
					CloudFileActivity.isNeedRefresh = true;
					// 设置下载过程中，点击通知栏，不回到主界面
					T.show(context, "“" + file.getName() + "”上传完成！");

					Intent nIntent = new Intent(
							BootBroadcastReceiver.REFERSH_ACTION);
					context.sendBroadcast(nIntent);
				}
				break;
			case 100:
				try {
					String reHeadUrl = (String) msg.obj;
					L.d(TAG, "handler reHeadUrl=" + reHeadUrl);
					// if (InfoWallActivity.infoWallActivity != null
					// && InfoWallActivity.infoWallActivity.menus != null) {
					// InfoWallActivity.infoWallActivity.menus
					// .resetHeadUrl(reHeadUrl);
					// }

					// TODO if (HomePgActivity.homePg != null) {
					// HomePgActivity.homePg.loadInfo(true);
					// }
					new UserBusinessDatabase(context).updateHeadUrlPath(
							App.app.share.getSessionKey(), reHeadUrl);
					// MenuPage.refreshInfo();
					BasicMenuActivity.refreshInfo();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		};
	};

	/**
	 * HttpURLConnection POST上传文件
	 *
	 * @throws Exception
	 */
	private String httpUpload() throws Exception {
		L.i(TAG, "httpUpload uploadUrl=" + uploadUrl);

		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		DataOutputStream dos = null;
		FileInputStream fis = null;
		String result = null;

		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = HttpRequestServers
					.getHttpURLConnection(url);

			// 设置连接超时时间
			httpURLConnection.setConnectTimeout(20 * 1000);
			// 设置数据读取超时时间
			httpURLConnection.setReadTimeout(25 * 1000);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			String fileName = file.getName();
			String reqHeader = twoHyphens
					+ boundary
					+ end
					+ "Content-Disposition: form-data; name=\"upload\"; filename=\""
					+ fileName + "\"" + end
					+ "Content-Type: application/octet-stream" + end + end;
			String reqEnder = end + twoHyphens + boundary + twoHyphens + end;

			long totalLength = file.length();
			httpURLConnection.setFixedLengthStreamingMode(reqHeader.length()
					+ (int) (totalLength) + reqEnder.length());

			dos = new DataOutputStream(httpURLConnection.getOutputStream());
			dos.writeBytes(reqHeader);
			fis = new FileInputStream(file);
			L.i(TAG, "httpUpload totalLength=" + totalLength);
			long uploadSize = 0;
			int progress = 0;
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, count);
				uploadSize += count;
				int i = (int) (uploadSize * 100 / totalLength);
				if (i > progress) {
					progress = i;
					// 设置通知显示的参数
//					updateNotification.setLatestEventInfo(context, tip + "已上传 "
//							+ progress + "%", fileName, updatePendingIntent);
					updateNotification = builder
							.setContentIntent(updatePendingIntent)
							.setContentTitle(tip + "已上传 "
									+ progress + "%")
							.setContentText(fileName).build();
					// 这个可以理解为开始执行这个通知
					updateNotificationManager.notify(id, updateNotification);
					L.d(TAG, "httpUpload progress=" + progress);
				}
			}
			dos.writeBytes(reqEnder);
			L.d(TAG, "httpUpload dos.size2=" + dos.size());
			dos.flush();

			result = StreamUtil.readData(httpURLConnection.getInputStream());
			L.d(TAG, "httpUpload result=" + result);
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.getInt("resultCode") == 1) {
				// 设置通知显示的参数
//				updateNotification.setLatestEventInfo(context, tip + "上传完成",
//						fileName, updatePendingIntent);
				updateNotification = builder
						.setContentIntent(updatePendingIntent)
						.setContentTitle(tip + "上传完成")
						.setContentText(fileName).build();

				if ("个人头像".equals(tip)) {
					handler.sendEmptyMessage(1);
					Message msg = new Message();
					msg.what = 100;
					String headPath = jsonObject.getString("resultMessage");
					headPath = headPath.replace("com:80", "com");
//					headPath = headPath.replace("http://", "https://");
					msg.obj = headPath;

					headPath = ThumbnailImageUrl.getThumbnailHeadUrl(headPath,
							HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
					L.i(TAG, "httpUpload headPath=" + headPath);
					MyFinalBitmap.cacheDiscImage(context, headPath, file,
							new ImageSize(180, 180));
					handler.sendMessage(msg);
				} else if ("圈子海报".equals(tip)) {
					handler.sendEmptyMessage(1);

					String headPath = jsonObject.getString("resultMessage");
					headPath = ThumbnailImageUrl.getThumbnailPostUrl(headPath,
							PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
					headPath = headPath.replace("com:80", "com");
//					headPath = headPath.replace("http://", "https://"); // 获取图片用的https://
																		// 名字需要保持一致；
					L.i(TAG, "httpUpload headPath=" + headPath);
					MyFinalBitmap.cacheDiscImage(context, headPath, file,
							new ImageSize(120, 120));
					try {
						UpdateGroupListThread.updateGroupList(context, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
					new Thread() {
						public void run() {
							GroupListService.getService(context)
									.saveContactTime(new String[] { groupId });
						};
					}.start();

					Bundle b = new Bundle();
					b.putBoolean("poster", true);
					SimpleReceiver.sendBoardcast(context,
							SimpleReceiver.RECEIVER_DATA_CHANGED, b);
				} else if ("个人文件".equals(tip)) {
					handler.sendEmptyMessage(1);
				}
			} else {
				L.d(TAG, "httpUpload Unknow fileId...");
			}
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (dos != null)
				try {
					dos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return result;
	}
}
