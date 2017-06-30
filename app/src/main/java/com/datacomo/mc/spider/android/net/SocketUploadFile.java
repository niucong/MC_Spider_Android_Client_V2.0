package com.datacomo.mc.spider.android.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

import android.os.Handler;
import android.os.Message;

import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.FormFile;

public class SocketUploadFile {
	private final static String TAG = "SocketUploadFile";

	private static String fileName = null;
	private static OutputStream outStream = null;
	private static BufferedReader reader = null;
	private static Socket socket = null;

	public static boolean uploadFile(String path, Map<String, String> params,
			FormFile formFile, int id, Handler mHandler)
			throws MalformedURLException, IOException {
		fileName = formFile.getFilname();
		long fileLength = formFile.getFile().length();

		L.i(TAG, "uploadFile : path = " + path + ",fileName = " + fileName
				+ ",fileLength = " + fileLength);

		// 数据分隔线
		final String BOUNDARY = "---------------------------7db2fd1b120612";
		// 数据结束标志
		final String ENDLINE = "--" + BOUNDARY + "--\r\n";
		// 获取实体数据总长度
		StringBuilder textEntity = new StringBuilder();
		// 1、获取文本类型参数的实体数据
		for (Map.Entry<String, String> entry : params.entrySet()) {
			textEntity.append("--");
			textEntity.append(BOUNDARY);
			textEntity.append("\r\n");
			textEntity.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"\r\n\r\n");
			textEntity.append(entry.getValue());
			textEntity.append("\r\n");
		}
		L.i(TAG, "uploadFile : textEntity  = " + textEntity.toString());

		// 2、获取文件类型参数的实体数据及长度
		StringBuilder fileData = new StringBuilder();
		fileData.append("--");
		fileData.append(BOUNDARY);
		fileData.append("\r\n");
		fileData.append("Content-Disposition: form-data;name=\""
				+ formFile.getParameterName() + "\";filename=\""
				+ formFile.getFilname() + "\"\r\n");
		fileData.append("Content-Type: " + formFile.getContentType()
				+ "\r\n\r\n");
		fileData.append("\r\n");
		L.i(TAG, "uploadFile : fileData = " + fileData.toString());

		int fileDataLength = fileData.length();
		if (formFile.getInStream() != null) {
			fileDataLength += formFile.getFile().length();
		} else {
			fileDataLength += formFile.getData().length;
		}
		// 计算传输给服务器的实体数据总长度
		int dataLength = textEntity.toString().getBytes().length
				+ fileDataLength + ENDLINE.getBytes().length;

		// 编写HTTP协议
		URL url = new URL(path);
		int port = url.getPort() == -1 ? 80 : url.getPort();
		L.i(TAG, "uploadFile : 端口号  = " + port);
		// 创建Socket连接
		socket = new Socket(InetAddress.getByName(url.getHost()), port);
		outStream = socket.getOutputStream();
		// 下面完成HTTP请求头的发送
		String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
		outStream.write(requestmethod.getBytes());
		String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, "
				+ "application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, "
				+ "application/vnd.ms-powerpoint, application/msword, */*\r\n";
		outStream.write(accept.getBytes());
		String language = "Accept-Language: zh-CN\r\n";
		outStream.write(language.getBytes());
		String contenttype = "Content-Type: multipart/form-data; boundary="
				+ BOUNDARY + "\r\n";
		outStream.write(contenttype.getBytes());
		String host = "Host: " + url.getHost() + ":" + port + "\r\n";
		outStream.write(host.getBytes());
		String contentlength = "Content-Length: " + dataLength + "\r\n";
		outStream.write(contentlength.getBytes());
		String alive = "Connection: Keep-Alive\r\n";
		outStream.write(alive.getBytes());
		// 根据HTTP协议在HTTP请求头后面需要再写一个回车换行
		outStream.write("\r\n".getBytes());
		// 发送所有文本类型的实体数据
		outStream.write(textEntity.toString().getBytes());

		// 发送所有文件类型的实体数据
		StringBuilder fileEntity = new StringBuilder();
		fileEntity.append("--");
		fileEntity.append(BOUNDARY);
		fileEntity.append("\r\n");
		fileEntity.append("Content-Disposition: form-data;name=\""
				+ formFile.getParameterName() + "\";filename=\""
				+ formFile.getFilname() + "\"\r\n");
		fileEntity.append("Content-Type: " + formFile.getContentType()
				+ "\r\n\r\n");
		L.i(TAG, "uploadFile : fileEntity  = " + fileEntity.toString());

		outStream.write(fileEntity.toString().getBytes());
		long spit = fileLength / 100;
		L.i(TAG, "uploadFile : spit = " + spit + " , fileLength : "
				+ fileLength);
		if (formFile.getInStream() != null) {
			byte[] buffer = new byte[1024];
			int len = 0;
			int percent = 0;
			int progress = 0;
			while ((len = formFile.getInStream().read(buffer)) != -1) {
				percent = percent + len;
				if (percent > spit) {
					progress++;
					Message msg = new Message();
					msg.what = 3;
					msg.obj = progress;
					mHandler.sendMessage(msg);
					// 设置通知显示的参数
					// updateNotification.setLatestEventInfo(UploadService.this,
					// "正在上传", fileName + " " + progress + "%",
					// updatePendingIntent);
					// // 这个可以理解为开始执行这个通知
					// updateNotificationManager.notify(id, updateNotification);
					percent = 0;
					L.d(TAG, "uploadFile : progress = " + progress);
				}
				outStream.write(buffer, 0, len);
			}
			// 设置通知显示的参数
			// updateNotification.setLatestEventInfo(UploadService.this, "上传完成",
			// fileName + " " + 100 + "%", updatePendingIntent);
			// updateNotificationManager.notify(id, updateNotification);
			// stopService(updateIntent);
			formFile.getInStream().close();
		} else {
			outStream.write(formFile.getData(), 0, formFile.getData().length);
		}
		outStream.write("\r\n".getBytes());
		// 发送数据结束标志，表示数据已经结束
		outStream.write(ENDLINE.getBytes());
		reader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));

		String str = reader.readLine();
		L.i(TAG, "uploadFile : str = " + str);
		if (str == null)
			str = "";
		// 读取web服务器返回的数据，判断请求码是否为303，如果不是303，代表请求失败
		if (str.indexOf("303") == -1) {
			// L.i(TAG, "上传失败");
			showResult(id);
			return false;
		} else {
			// L.i(TAG, "上传成功");
			showResult(id);
			return true;
		}
	}

	/**
	 * 
	 * @param id
	 * @throws IOException
	 */
	private static void showResult(int id) throws IOException {
		if (outStream != null) {
			outStream.flush();
			outStream.close();
		}
		if (reader != null)
			reader.close();
		if (socket != null)
			socket.close();
	}
}
