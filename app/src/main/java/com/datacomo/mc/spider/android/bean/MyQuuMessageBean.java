package com.datacomo.mc.spider.android.bean;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Message;

import com.datacomo.mc.spider.android.db.ChatGroupMessageBeanService;
import com.datacomo.mc.spider.android.db.QChatSendService;
import com.datacomo.mc.spider.android.enums.ImageSizeEnum;
import com.datacomo.mc.spider.android.net.APIGroupChatRequestServers;
import com.datacomo.mc.spider.android.net.HttpRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.groupchat.GroupChatMessageBean;
import com.datacomo.mc.spider.android.net.been.groupchat.ObjectInfoBean;
import com.datacomo.mc.spider.android.params.groupchat.SendGroupMessageParams;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.JsonParseTool;
import com.datacomo.mc.spider.android.util.StreamUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.nostra13.universalimageloader.core.assist.ImageSize;

public class MyQuuMessageBean {
	private static final String TAG = "MyQuuMessageBean";

	boolean sendState = false;
	boolean isSuccess = false;
	boolean isSendBle = false;
	private GroupChatMessageBean msgBean;
	private Context c;
	private SendMsgTask task;
	private int groupId;

	private String filePath;
	private String memberName, chatName;

	private long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setName(String memberName, String chatName) {
		this.memberName = memberName;
		this.chatName = chatName;
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

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public MyQuuMessageBean(int groupId) {
		this.groupId = groupId;
	}

	public MyQuuMessageBean(int groupId, GroupChatMessageBean bean) {
		this.groupId = groupId;
		msgBean = bean;
	}

	public MyQuuMessageBean(int groupId, GroupChatMessageBean bean,
			boolean sendState) {
		this.groupId = groupId;
		msgBean = bean;
		this.sendState = sendState;
	}

	public MyQuuMessageBean(int groupId, GroupChatMessageBean bean,
			boolean sendState, boolean success, boolean sendable) {
		this.groupId = groupId;
		msgBean = bean;
		this.sendState = sendState;
		this.isSuccess = success;
		this.isSendBle = sendable;
	}

	public GroupChatMessageBean getMsgBean() {
		return msgBean;
	}

	public void setMsgBean(GroupChatMessageBean msg) {
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

		final List<ObjectInfoBean> beans;
		private LoadQuuMessageInfo loadinfo;
		private File file;

		public SendMsgTask(LoadQuuMessageInfo load) {
			beans = getMsgBean().getMessageList();
			loadinfo = load;
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mcResult = null;
			String text = null;
			long objectLength = 0;
			for (ObjectInfoBean bean : beans) {
				if (1 == bean.getMessageType()) {
					text = bean.getMessageContent();
				}
				objectLength = bean.getObjectLength();
				break;
			}

			String url = URLProperties.GROUP_CHAT_JSON;
			if ("OBJ_PHOTO".equals(getObjectType())) {
				L.i(TAG, "SendMsgTask filePath=" + filePath);
				try {
					file = new File(filePath);

					FileUtil.createFile(ConstantUtil.TEMP_PATH);
					file = FileUtil.ChangeImage(file, false);

					String paramss = new SendGroupMessageParams(c,
							new String[] { groupId + "" }, false, null, null,
							memberName, chatName, "OBJ_PHOTO", file.getName(),
							null, null, null).getParams();
					L.d(TAG, "SendMsgTask url=" + url + "?" + paramss);
					String result = httpUpload(url + "?" + paramss, file);
					L.d(TAG, "SendMsgTask result=" + result);
					mcResult = (MCResult) JsonParseTool.dealSingleResult(
							result, MCResult.class);
				} catch (Exception e) {
					mcResult = null;
					e.printStackTrace();
				}
			} else if ("OBJ_VOICE".equals(getObjectType())) {
				try {
					file = new File(filePath);
					String paramss = new SendGroupMessageParams(c,
							new String[] { groupId + "" }, false, null, null,
							memberName, chatName, "OBJ_VOICE", file.getName(),
							objectLength + "", null, null).getParams();
					L.d(TAG, "SendMsgTask url=" + url + "?" + paramss);
					String result = httpUpload(url + "?" + paramss, file);
					L.d(TAG, "SendMsgTask result=" + result);
					mcResult = (MCResult) JsonParseTool.dealSingleResult(
							result, MCResult.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					mcResult = APIGroupChatRequestServers.sendGroupMessage(
							getC(), new String[] { groupId + "" }, true, text,
							null, memberName, chatName, "OBJ_TEXT", null, null,
							null, null); // chatId
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
				loadinfo.setMessage(MyQuuMessageBean.this);
			} else {
				L.i(TAG, "SendMsgTask time=" + time);
				QChatSendService.getService(c).deleteChat(time);
				try {
					// {"result":[{"MESSAGE_ID":5228,"RESULT":1,"GROUPID":8948}],"resultStaus":true,"resultCode":1,"resultMessage":"","version":"v1.0"}
					JSONArray array = new JSONArray(result.getResult()
							.toString());
					JSONObject jsonObject = (JSONObject) array.get(0);
					int messageId = jsonObject.getInt("MESSAGE_ID");
					GroupChatMessageBean msg = getMsgBean();
					msg.setCreateTime(System.currentTimeMillis() + "");
					msg.setMessageId(messageId);

					if ("OBJ_TEXT".equals(getObjectType())) {

					} else {
						if ("OBJ_PHOTO".equals(getObjectType())) {
							msg.setMessageList(setPath(jsonObject, msg, true));
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
							msg.setMessageList(setPath(jsonObject, msg, false));
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

					ChatGroupMessageBeanService.getService(c).saveSend(msg,
							groupId + "");

					setMsgBean(msg);
					setSendState(false);
					setSuccess(true);
					setSendable(false);
					loadinfo.setMessage(MyQuuMessageBean.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private List<ObjectInfoBean> setPath(JSONObject jsonObject,
			GroupChatMessageBean msg, boolean isPhoto) throws JSONException {
		List<ObjectInfoBean> messageList = new ArrayList<ObjectInfoBean>();
		ObjectInfoBean bean = msg.getMessageList().get(0);
		String URL = jsonObject.getString("URL");
		URL = URL.replace("com:80", "com");
//		URL = URL.replace("http://", "https://");
		String url;
		String path;
		if (isPhoto) {
			url = URL.substring(0, URL.indexOf("/group_chat_photo/"));
			path = URL.substring(URL.indexOf("/group_chat_photo/"));
		} else {
			url = URL.substring(0, URL.indexOf("/group_chat_audio/"));
			path = URL.substring(URL.indexOf("/group_chat_audio/"));
		}
		bean.setObjectUrl(url);
		bean.setObjectPath(path);
		messageList.add(bean);
		return messageList;
	}

	public interface LoadQuuMessageInfo {
		void setMessage(MyQuuMessageBean bean);
	}

	public int getDataType() { // 受不了这数据
		if (null == msgBean || null == msgBean.getMessageList()
				|| 0 == msgBean.getMessageList().size()) {
			return -1;
		} else {
			return msgBean.getMessageList().get(0).getMessageType();
		}
	}

	public String getObjectType() { // 受不了这数据
		return msgBean.getMessageList().get(0).getObjectType();
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
}
