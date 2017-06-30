package com.datacomo.mc.spider.android.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;

import com.datacomo.mc.spider.android.InfoWallActivity;
import com.datacomo.mc.spider.android.MsgActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.db.CommentSendService;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.dialog.IsNetworkConnected;
import com.datacomo.mc.spider.android.enums.NewNumTypeEnum;
import com.datacomo.mc.spider.android.net.APIMailRequestServers;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.HttpRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.NewNoticesNumBean;
import com.datacomo.mc.spider.android.params.UploadFileParams;
import com.datacomo.mc.spider.android.receiver.BootBroadcastReceiver;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.SendWay;
import com.datacomo.mc.spider.android.util.SoftPhoneInfo;
import com.datacomo.mc.spider.android.util.StreamUtil;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationService extends Service {
	private static final String TAG = "NotificationService";

	private static String session_key;

	// 通知栏
	private NotificationManager notificationManager = null;
	private Notification notification = null;
	// 通知栏跳转Intent
	private Intent messageIntent = null;
	private PendingIntent messagePendingIntent = null;
	private Notification.Builder builder;

	// private boolean flag = true;

	private SendRequestThread sendRequest = null;

	/** 请求消息时间间隔 */
	// private long TIME_NIGHT = 5 * 60 * 1000;
	private long TIME_DAY = 10 * 1000;
	private long TIME_BACK = 30 * 1000;

	// public static boolean isChat = false;

	private int pNumber, lNumber, nNumber;// , gNumber

	private final File file = new File(ConstantUtil.TEMP_PATH);
	private final String imei = new SoftPhoneInfo(App.app).getPhoneMark();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);// 初始化管理器
		this.notification = new Notification();
		sendRequest = new SendRequestThread();
		sendRequest.setPriority(Thread.MAX_PRIORITY);
		sendRequest.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// flag = true;
		boolean isSilence = false;
		if (intent != null)
			isSilence = intent.getBooleanExtra("isSilence", false);
		setAlertType(isSilence);
		super.onStart(intent, startId);

		// TODO
		// int myId = GetDbInfoUtil.getMemberId(this);
		// XMPPAPI.getXmppapi().enterChat(myId + "", myId + "", this);
	}

	private void setAlertType(boolean isSilence) {
		// L.i(TAG, "setAlertType isChat=" + isChat);
		if (isSilence) {
			notification.defaults = Notification.DEFAULT_LIGHTS;
		} else {
			// 根据文件中的参数来设置消息提醒的类型
			String type = App.app.share.getStringMessage("NotificationSetup",
					"type", "all");
			L.d(TAG, "setAlertType...type=" + type);
			if ("ring".equals(type)) {
				L.i(TAG, "setAlertType...铃声");
				notification.defaults = Notification.DEFAULT_SOUND
						| Notification.DEFAULT_LIGHTS;// 设置默认铃声
			} else if ("vibrate".equals(type)) {
				L.i(TAG, "setAlertType...震动");
				notification.defaults = Notification.DEFAULT_VIBRATE
						| Notification.DEFAULT_LIGHTS;// 设置默认震动
			} else if ("all".equals(type)) {
				// L.i(TAG, "setAlertType...铃声震动");
				notification.defaults = Notification.DEFAULT_SOUND
						| Notification.DEFAULT_VIBRATE
						| Notification.DEFAULT_LIGHTS;// 设置铃声震动
				// notification.defaults = Notification.DEFAULT_LIGHTS;
			} else {
				notification.defaults = Notification.DEFAULT_LIGHTS;
			}
		}
		// 添加led
		// ledARGB属性可以用来设置LED的颜色，ledOffMS和ledOnMS属性则可以设置LED闪烁的频率和模式。
		// ledOnMS设置为1并把ledOffMS设置为0来打开LED,两个都设置为0则关闭LED.
		notification.ledARGB = Color.BLUE;
		notification.ledOffMS = 0;
		notification.ledOnMS = 1;
		notification.flags = notification.flags | Notification.FLAG_SHOW_LIGHTS;
	}

	@Override
	public void onDestroy() {
		try {
			// flag = false;
			notificationManager.cancelAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	/**
	 * 开启消息通知的线程
	 */
	class SendRequestThread extends Thread {
		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

			while (true) {
				// while (flag) {

				try {
					session_key = App.app.share.getSessionKey();
					L.i(TAG, "SendRequestThread session_key=" + session_key);
					if (session_key != null
							&& !"".equals(session_key)
							&& IsNetworkConnected
									.checkNetworkInfo(NotificationService.this)) {
						MCResult mcResult = null;
						NewNoticesNumBean numBean = null;
						try {
							mcResult = APIRequestServers.newNum(
									NotificationService.this,
									NewNumTypeEnum.NEWALLNUM);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						if (mcResult != null) {
							int code = mcResult.getResultCode();
							if (code == 1) {
								numBean = (NewNoticesNumBean) mcResult
										.getResult();
							} else if (code == 100 || code == 11) {
								String way = "";
								try {
									JSONObject jsonObject = new JSONObject(
											mcResult.getResult().toString());
									way = jsonObject.getString("loginVisitWay");
								} catch (Exception e) {
									e.printStackTrace();
								}

								App.app.share.saveBooleanMessage(
										"isOtherLogin", "isOtherLogin", true);
								App.app.share.saveStringMessage("isOtherLogin",
										"loginWay", way);
								App.app.share.saveBooleanMessage("program",
										"auto_login", false);
								new UserBusinessDatabase(
										NotificationService.this)
										.updateUserPassword(session_key, "");
								App.app.share.saveSessionKey("");
								// flag = false;

								// ScreenManager.getInctance().closeAllActivity();
								// Intent i = new
								// Intent(NotificationService.this,
								// LoginActivity.class);
								// i.putExtra("action", "logout");
								// i.putExtra("otherLogin", true);
								// startActivity(i);
								// sharedMessage.saveBooleanMessage(
								// "isOtherLogin", "isOtherLogin", false);

								messageIntent = new Intent(
										NotificationService.this,
										InfoWallActivity.class);
								messagePendingIntent = PendingIntent
										.getActivity(
												NotificationService.this,
												0,
												messageIntent,
												PendingIntent.FLAG_UPDATE_CURRENT);
								// notification.flags |=
								// Notification.FLAG_AUTO_CANCEL;
								// 设置通知栏显示内容
//								notification.icon = R.drawable.icon;
//								notification.tickerText = "您的帐号在另一处登录（"
//										+ SendWay.loginWay(way) + "），您被迫下线。";
//								notification.setLatestEventInfo(
//										NotificationService.this, "优优工作圈",
//										"您的帐号在另一处登录（" + SendWay.loginWay(way)
//												+ "），您被迫下线。",
//										messagePendingIntent);
								builder = new Notification.Builder(NotificationService.this).setTicker(
										"您的帐号在另一处登录（" + SendWay.loginWay(way)
												+ "），您被迫下线。").setSmallIcon(R.drawable.icon);
								notification = builder.setContentIntent(messagePendingIntent)
										.setContentTitle("优优工作圈").setContentText("您的帐号在另一处登录（" + SendWay.loginWay(way)
												+ "），您被迫下线。").build();
								notificationManager.cancelAll();
								notificationManager.notify(0, notification);

								Intent nIntent = new Intent(
										BootBroadcastReceiver.EXIT_APP);
								sendBroadcast(nIntent);

								// notificationManager.cancelAll();

								// TODO
								// try {
								// XMPPManager.getConnection().disconnect();
								// } catch (Exception e) {
								// e.printStackTrace();
								// }
								return;
							}
						}

						int LETTER_NUM = 0;
						int notice_num = 0;
						int greet_num = 0;
						int GROUP_CHAT_NUM = 0;

						if (numBean != null) {// && flag) {
							int ALL_NUM = numBean.getALL_NUM()
									- numBean.getGROUP_CHAT_NUM();
							// TODO
							GROUP_CHAT_NUM = numBean.getGROUP_CHAT_NUM();
							// GROUP_CHAT_NUM = App.app.share.getAllMessage(
							// "group_chat_unread", 0);
							LETTER_NUM = numBean.getLETTER_NUM();
							greet_num = numBean.getPOKER_NUM();
							notice_num = ALL_NUM - LETTER_NUM;
							L.i(TAG, "SendRequestThread lNumber=" + lNumber
									+ ",nNumber=" + nNumber + ",ALL_NUM="
									+ ALL_NUM);
							L.d(TAG, "SendRequestThread LETTER_NUM="
									+ LETTER_NUM + ",GROUP_CHAT_NUM="
									+ GROUP_CHAT_NUM + ",notice_num="
									+ notice_num + ",greet_num=" + greet_num);
							int n = CommentSendService.getService(App.app)
									.getCount(session_key);

							Intent nIntent = new Intent(
									BootBroadcastReceiver.MSG_NUMBERS);
							Bundle bl = new Bundle();
							bl.putIntArray("nums", new int[] { notice_num,
									LETTER_NUM, GROUP_CHAT_NUM, greet_num, n });
							nIntent.putExtras(bl);
							sendBroadcast(nIntent);

							App.app.share.saveStringMessage("isOtherLogin",
									"newNums", notice_num + "#" + LETTER_NUM
											+ "#" + GROUP_CHAT_NUM + "#"
											+ greet_num);

							if (ALL_NUM > 0 || GROUP_CHAT_NUM > 0) {// ||
																	// greet_num
																	// > 0
								if (LETTER_NUM > 0) {// 有新私信
									Intent mIntent = new Intent(
											BootBroadcastReceiver.CHAT_NUMBER);
									mIntent.putExtra("LETTER_NUM", LETTER_NUM);
									sendBroadcast(mIntent);
								}
								// if (GROUP_CHAT_NUM > 0) {// 有新圈聊
								// Intent gIntent = new Intent(
								// BootBroadcastReceiver.QUUCHAT_NUMBER);
								// gIntent.putExtra("LETTER_NUM",
								// GROUP_CHAT_NUM);
								// sendBroadcast(gIntent);
								// }

								if (lNumber != LETTER_NUM
										|| pNumber != GROUP_CHAT_NUM
										// || gNumber != greet_num
										|| nNumber != notice_num) {
									pNumber = GROUP_CHAT_NUM;
									lNumber = LETTER_NUM;
									nNumber = notice_num;
									// gNumber = greet_num;

									String contentTitle = "优优工作圈";
									String contentText = "您有 ";

									if (notice_num > 0) {
										if (notice_num > 99) {
											contentText += 99 + "+ 条新消息  ";
										} else {
											contentText += notice_num
													+ " 条新消息  ";
										}
									}
									if (LETTER_NUM > 0) {
										if (LETTER_NUM > 99) {
											contentText += 99 + "+ 条新私信  ";
										} else {
											contentText += LETTER_NUM
													+ " 条新私信  ";
										}
									}
									if (GROUP_CHAT_NUM > 0) {
										if (GROUP_CHAT_NUM > 99) {
											contentText += 99 + "+ 条新圈聊 ";
										} else {
											contentText += GROUP_CHAT_NUM
													+ " 条新圈聊 ";
										}
									}

									L.i(TAG,
											"SendRequestThread noticeTypeName="
													+ contentTitle);
									L.d(TAG, "SendRequestThread contentText="
											+ contentText);
									int type = MsgActivity.MSG_NOTICE;
									if (notice_num > 0) {

									} else if (LETTER_NUM > 0) {
										type = MsgActivity.MSG_PLETTER;
									} else if (GROUP_CHAT_NUM > 0) {
										type = MsgActivity.MSG_GROUPCHAT;
										// } else if (greet_num > 0) {
										// type = MsgActivity.MSG_GREET;
									} else if (n > 0) {
										type = 4;
									}

									messageIntent = new Intent(
											NotificationService.this,
											MsgActivity.class);
									Bundle b = new Bundle();
									b.putBoolean("isFresh", true);
									b.putInt("cut", type);
									messageIntent.putExtras(b);
									messagePendingIntent = PendingIntent
											.getActivity(
													NotificationService.this,
													0,
													messageIntent,
													PendingIntent.FLAG_UPDATE_CURRENT);
									// notification.flags |=
									// Notification.FLAG_AUTO_CANCEL;
									// 设置通知栏显示内容
									notification.icon = R.drawable.icon;

									notification.tickerText = contentText;
//									notification.setLatestEventInfo(
//											NotificationService.this,
//											contentTitle,// noticeTypeName,//noticeTypeName;
//											contentText, messagePendingIntent);
									notification = builder
											.setContentIntent(messagePendingIntent)
											.setContentTitle(contentTitle)
											.setContentText(contentText).build();
									notificationManager.cancel(null, 0);
									// if (flag) {
									notificationManager.notify(0, notification);
									// }
								}
							} else {
								pNumber = 0;
								lNumber = 0;
								nNumber = 0;
								// gNumber = 0;
								// notificationManager.notify(0, notification);
								notificationManager.cancel(null, 0);
							}

						} else {
							pNumber = 0;
							lNumber = 0;
							nNumber = 0;
							// gNumber = 0;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					// int hour = new Date().getHours();
					// if (hour < 1 || hour > 6) {
					if (appIsBack() || isScreenLocked()) {
						sleep(TIME_BACK);
					} else {
						sleep(TIME_DAY);
					}
					// } else {
					// if (isChat) {
					// sleep(TIME_DAY);
					// } else {
					// sleep(TIME_NIGHT);
					// }
					// }
				} catch (Exception e) {
					e.printStackTrace();
				}

				uploadLog(session_key);
			}
		}
	}

	/**
	 * 是否锁屏
	 * 
	 * @return
	 */
	private boolean isScreenLocked() {
		KeyguardManager mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		boolean isScreenLocked = mKeyguardManager
				.inKeyguardRestrictedInputMode();
		return isScreenLocked;
	}

	/**
	 * 是否后台运行
	 * 
	 * @return
	 */
	private boolean appIsBack() {
		boolean isAppBackRunning = true;
		String MY_PKG_NAME = null;
		try {
			ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> list = am.getRunningTasks(20);
			MY_PKG_NAME = getPackageName();
			if (list.get(0).topActivity.getPackageName().equals(MY_PKG_NAME)) {
				isAppBackRunning = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isAppBackRunning;
	}

	/**
	 * 更新通知数据
	 * 
	 * @param cut
	 */
	@SuppressWarnings("unused")
	private void getNoticeData(final int cut) {
		new Thread() {
			public void run() {
				try {
					switch (cut) {
					case 0:
						// APIRequestServers.noticeList(NotificationService.this,
						// "0", "20");
						break;
					case 1:
						APIRequestServers.myMessageList(
								NotificationService.this, "0", "20");
						break;
					case 2:
						APIRequestServers.greetInfoList(
								NotificationService.this, "0", "20");
						break;
					case 3:
						APIMailRequestServers.contactLeaguers(
								NotificationService.this, "0", "20");
						break;
					default:
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	private static boolean isloading;

	/**
	 * 上传前一日监控日志
	 */
	@SuppressLint("SimpleDateFormat")
	private void uploadLog(final String session_key) {
		if (isloading)
			return;
		Date nowtime = new Date();
		final String date = new SimpleDateFormat("yyyyMMdd").format(nowtime);
		if (file.exists()) {
			new Thread() {
				public void run() {
					isloading = true;
					File[] files = file.listFiles();
					if (files != null) {
						for (File file : files) {
							String name = file.getName();
							L.i(TAG, "uploadLog name=" + name + ",date=" + date);
							if (name.endsWith("_" + imei + ".txt")
									&& !name.contains(date)) {
								String params = new UploadFileParams(App.app,
										"uploadClientLog", name, null)
										.getParams();
								String url = URLProperties.SPIDER_JSON + "?"
										+ params;
								httpUpload(url, file);// 上传地址
								isloading = false;
							}
						}
					}
				};
			}.start();
		}
	}

	/**
	 * HttpURLConnection POST上传文件
	 * 
	 * @param uploadUrl
	 * @param file
	 */
	private void httpUpload(String uploadUrl, File file) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		DataOutputStream dos = null;
		FileInputStream fis = null;
		L.i(TAG, "httpUpload uploadUrl=" + uploadUrl);
		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = HttpRequestServers
					.getHttpURLConnection(url);

			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			String reqHeader = twoHyphens
					+ boundary
					+ end
					+ "Content-Disposition: form-data; name=\"upload\"; filename=\""
					+ file.getName() + "\"" + end
					+ "Content-Type: application/octet-stream" + end + end;
			String reqEnder = end + twoHyphens + boundary + twoHyphens + end;

			long totalLength = file.length();
			httpURLConnection.setFixedLengthStreamingMode(reqHeader.length()
					+ (int) (totalLength) + reqEnder.length());

			dos = new DataOutputStream(httpURLConnection.getOutputStream());
			dos.writeBytes(reqHeader);
			fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, count);
			}
			dos.writeBytes(reqEnder);
			dos.flush();
			String result = StreamUtil.readData(httpURLConnection
					.getInputStream());
			// 上传成功后删除
			L.d(TAG, "httpUpload result=" + result);
			final JSONObject object = new JSONObject(result);
			if (object.getInt("resultCode") == 1)
				file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			if (dos != null)
				try {
					dos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
}
