package com.datacomo.mc.spider.android.bean;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Message;

import com.datacomo.mc.spider.android.QChatActivity;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.db.ChatMessageBeanService;
import com.datacomo.mc.spider.android.db.QChatSendService;
import com.datacomo.mc.spider.android.enums.ImageSizeEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.HttpRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MessageBean;
import com.datacomo.mc.spider.android.net.been.MessageResourceInfo;
import com.datacomo.mc.spider.android.params.msg.SendMessageParams;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.JsonParseTool;
import com.datacomo.mc.spider.android.util.StreamUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.nostra13.universalimageloader.core.assist.ImageSize;

public class MyQMessageBean {
	private static final String TAG = "MyQMessageBean";

	boolean sendState = false;
	boolean isSuccess = false;
	boolean isSendBle = false;
	private MessageBean msgBean;
	private Context c;
	private SendMsgTask task;
	private String memberId;

	private String filePath;

	private long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Context getC() {
		return c;
	}

	public void setC(Context c) {
		this.c = c;
	}

	public void setSendable(boolean sendable) {
		this.isSendBle = sendable;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public MyQMessageBean(String memberId) {
		this.memberId = memberId;
	}

	public MyQMessageBean(String memberId, MessageBean bean) {
		this.memberId = memberId;
		msgBean = bean;
	}

	public MyQMessageBean(String memberId, MessageBean bean, boolean sendState) {
		this.memberId = memberId;
		msgBean = bean;
		this.sendState = sendState;
	}

	public MyQMessageBean(String memberId, MessageBean bean, boolean sendState,
			boolean success, boolean sendable) {
		this.memberId = memberId;
		msgBean = bean;
		this.sendState = sendState;
		this.isSuccess = success;
		this.isSendBle = sendable;
	}

	public MessageBean getMsgBean() {
		return msgBean;
	}

	public void setMsgBean(MessageBean msg) {
		this.msgBean = msg;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public boolean isSendState() {
		return sendState;
	}

	public void setSendState(boolean sendState) {
		this.sendState = sendState;
	}

	public void sendMsg(LoadQuuMessageInfo load) {
		setSendState(true);
		stopTask();
		task = new SendMsgTask(load);
		task.execute();
	}

	private void stopTask() {
		if (null != task && task.getStatus() == Status.RUNNING) {
			task.cancel(true);
		}

	}

	class SendMsgTask extends AsyncTask<Void, Integer, MCResult> {

		private LoadQuuMessageInfo loadinfo;
		private File file;

		public SendMsgTask(LoadQuuMessageInfo load) {
			loadinfo = load;
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mcResult = null;
			String url = URLProperties.MESSAGE_JSON;
			if ("OBJ_PHOTO".equals(getObjectType())) {
				try {
					file = new File(filePath);
					String paramss = new SendMessageParams(App.app,
							new String[] { memberId + "" }, null, null, null,
							"", true, false + "", "OBJ_PHOTO", null,
							file.getName(), null, null).getParams();
					L.d(TAG, "SendMsgTask url=" + url + "?" + paramss);
					file = FileUtil.ChangeImage(file, false);

					String result = httpUpload(url + "?" + paramss, file);
					L.d(TAG, "SendMsgTask result=" + result);
					mcResult = (MCResult) JsonParseTool.dealSingleResult(
							result, MCResult.class);
				} catch (Exception e) {
					mcResult = null;
					e.printStackTrace();
				}
			} else if ("OBJ_VOICE".equals(getObjectType())) {
				file = new File(filePath);
				String objectLength = msgBean.getMessageResourceInfoList()
						.get(0).getObjectBak1()
						+ "";
				String paramss = new SendMessageParams(App.app,
						new String[] { memberId + "" }, null, null, null, "",
						true, false + "", "OBJ_VOICE", objectLength,
						file.getName(), null, null).getParams();
				L.d(TAG, "SendMsgTask url=" + url + "?" + paramss);
				try {
					String result = httpUpload(url + "?" + paramss, file);
					L.d(TAG, "SendMsgTask result=" + result);
					mcResult = (MCResult) JsonParseTool.dealSingleResult(
							result, MCResult.class);
				} catch (Exception e) {
					mcResult = null;
					e.printStackTrace();
				}
			} else if ("OBJ_TEXT".equals(getObjectType())) {
				try {
					String text = msgBean.getMessageResourceInfoList().get(0)
							.getMessageContent();
					mcResult = APIRequestServers.sendMessage(App.app,
							new String[] { memberId + "" }, null, null, null,
							text, "true", "OBJ_TEXT", null, null, null, null);
				} catch (Exception e) {
					mcResult = null;
					e.printStackTrace();
				}
			}

			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (null == result || result.getResultCode() != 1) {
				setSendState(false);
				setSuccess(false);
				setSendable(false);
				loadinfo.setMessage(MyQMessageBean.this);
				// TODO 保存失败私信
				// memberId、MyQMessageBean
				// setAlertType();
			} else {
				L.i(TAG, "SendMsgTask time=" + time);
				QChatSendService.getService(App.app).deleteChat(time);
				try {
					JSONObject jsonObject = new JSONObject(result.getResult()
							.toString());
					int messageId = jsonObject.getInt("MESSAGEID");
					MessageBean msg = getMsgBean();
					msg.setCreateTime(System.currentTimeMillis() + "");
					msg.setMessageId(messageId);
					if ("OBJ_TEXT".equals(getObjectType())) {

					} else {
						if ("OBJ_PHOTO".equals(getObjectType())) {
							msg.setMessageResourceInfoList(setPath(jsonObject,
									msg, true));
							try {
								String path = jsonObject.getString("URL");
								path = path.replace("com:80", "com");
//								path = path.replace("http://", "https://");
								path = ThumbnailImageUrl.getThumbnailImageUrl(
										path, ImageSizeEnum.THREE_HUNDRED);
								L.i(TAG, "path=" + path);
								MyFinalBitmap.cacheDiscImage(c, path, file,
										new ImageSize(300, 300));
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if ("OBJ_VOICE".equals(getObjectType())) {
							msg.setMessageResourceInfoList(setPath(jsonObject,
									msg, false));
							try {
								String path = jsonObject.getString("URL");
								String name = path.substring(
										path.lastIndexOf("/") + 1,
										path.length());
								new FileUtil().copyfile(file, new File(
										ConstantUtil.VOICE_PATH + name), true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					ChatMessageBeanService.getService(c).saveSend(msg);
					
					setMsgBean(msg);
					setSendState(false);
					setSuccess(true);
					setSendable(false);
					loadinfo.setMessage(MyQMessageBean.this);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					if (QChatActivity.instance.friendId.equals(memberId))
						QChatActivity.instance.size = QChatActivity.instance.size + 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private List<MessageResourceInfo> setPath(JSONObject jsonObject,
			MessageBean msg, boolean isPhoto) throws JSONException {
		List<MessageResourceInfo> messageList = new ArrayList<MessageResourceInfo>();
		MessageResourceInfo bean = msg.getMessageResourceInfoList().get(0);
		String URL = jsonObject.getString("URL");
		URL = URL.replace("com:80", "com");
//		URL = URL.replace("http://", "https://");
		String url;
		String path;
		if (isPhoto) {
			url = URL.substring(0, URL.indexOf("/message_photo/"));
			path = URL.substring(URL.indexOf("/message_photo/"));
		} else {
			url = URL.substring(0, URL.indexOf("/message_audio/"));
			path = URL.substring(URL.indexOf("/message_audio/"));
		}
		bean.setObjectUrl(url);
		bean.setObjectPath(path);
		messageList.add(bean);
		L.i(TAG, "url=" + url + ",path=" + path);
		return messageList;
	}

	public interface LoadQuuMessageInfo {
		void setMessage(MyQMessageBean bean);
	}

	//
	// public int getDataType() { // 受不了这数据
	// if (null == msgBean || null == msgBean.getMessageResourceInfoList()
	// || 0 == msgBean.getMessageResourceInfoList().size()) {
	// return -1;
	// } else {
	// return msgBean.getMessageList().get(0).getMessageType();
	// }
	// }

	public String getObjectType() {
		return getFirstData().getObjectType();
	}

	public MessageResourceInfo getFirstData() {
		return msgBean.getMessageResourceInfoList().get(0);
	}

	/**
	 * HttpURLConnection POST上传文件
	 * 
	 * @param uploadUrl
	 * @param filename
	 * @throws Exception
	 */
	private String httpUpload(String uploadUrl, File file) throws Exception {
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
			L.i(TAG, "httpUpload totalLength=" + totalLength);
			long uploadSize = 0;
			int progress = 0;
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) != -1) {
				// if (isDel) {
				// return null;
				// }
				dos.write(buffer, 0, count);
				uploadSize += count;
				int i = (int) (uploadSize * 100 / totalLength);
				if (i > progress) {
					progress = i;
					Message msg = new Message();
					msg.what = 0;
					msg.arg1 = progress;
				}
			}
			dos.writeBytes(reqEnder);
			dos.flush();

			result = StreamUtil.readData(httpURLConnection.getInputStream());
			L.d(TAG, "httpUpload result=" + result);
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
		return result;
	}

	public boolean isSendable() {
		return isSendBle;
	}

	// @SuppressWarnings("deprecation")
	// private void setAlertType() {
	// if (QChatActivity.instance != null && NotificationService.isChat
	// && QChatActivity.instance.friendId.equals(memberId)) {
	// return;
	// }
	// @SuppressWarnings("static-access")
	// NotificationManager notificationManager = (NotificationManager) App.app
	// .getSystemService(App.app.NOTIFICATION_SERVICE);
	// Notification notification = new Notification();
	// notification.defaults = Notification.DEFAULT_SOUND
	// | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;// 设置铃声震动
	// if (NotificationService.isChat) {
	// notification.defaults = Notification.DEFAULT_LIGHTS;
	// } else {
	// // 根据文件中的参数来设置消息提醒的类型
	// String type = new AppSharedPreferences(App.app).getStringMessage(
	// "NotificationSetup", "type", "all");
	// L.d(TAG, "setAlertType...type=" + type);
	// if ("ring".equals(type)) {
	// L.i(TAG, "setAlertType...铃声");
	// notification.defaults = Notification.DEFAULT_SOUND
	// | Notification.DEFAULT_LIGHTS;// 设置默认铃声
	// } else if ("vibrate".equals(type)) {
	// L.i(TAG, "setAlertType...震动");
	// notification.defaults = Notification.DEFAULT_VIBRATE
	// | Notification.DEFAULT_LIGHTS;// 设置默认震动
	// } else if ("all".equals(type)) {
	// // L.i(TAG, "setAlertType...铃声震动");
	// notification.defaults = Notification.DEFAULT_SOUND
	// | Notification.DEFAULT_VIBRATE
	// | Notification.DEFAULT_LIGHTS;// 设置铃声震动
	// // notification.defaults = Notification.DEFAULT_LIGHTS;
	// } else {
	// notification.defaults = Notification.DEFAULT_LIGHTS;
	// }
	// }
	// // 添加led
	// // ledARGB属性可以用来设置LED的颜色，ledOffMS和ledOnMS属性则可以设置LED闪烁的频率和模式。
	// // ledOnMS设置为1并把ledOffMS设置为0来打开LED,两个都设置为0则关闭LED.
	// notification.ledARGB = Color.BLUE;
	// notification.ledOffMS = 0;
	// notification.ledOnMS = 1;
	// notification.flags = notification.flags | Notification.FLAG_SHOW_LIGHTS;
	//
	// Intent messageIntent = new Intent(App.app, QChatActivity.class);
	// Bundle b = new Bundle();
	// // b.putInt("cut", MsgActivity.MSG_PLETTER);
	// b.putString("memberId", memberId);
	// b.putString("name", msgBean.getReceiverName());
	// b.putString("head", msgBean.getReceiverHead().getFullHeadPath());
	// messageIntent.putExtras(b);
	// PendingIntent messagePendingIntent = PendingIntent.getActivity(App.app,
	// 0, messageIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	// // notification.flags |=
	// // Notification.FLAG_AUTO_CANCEL;
	// // 设置通知栏显示内容
	// notification.icon = R.drawable.icon;
	// notification.tickerText = "私信发送失败";
	// notification.setLatestEventInfo(App.app, "优优工作圈", "私信发送失败",
	// messagePendingIntent);
	// notificationManager.cancel(null, 0);
	// notificationManager.notify(0, notification);
	// }
}
