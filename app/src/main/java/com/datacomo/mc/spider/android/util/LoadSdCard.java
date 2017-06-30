package com.datacomo.mc.spider.android.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.datacomo.mc.spider.android.net.HttpRequestServers;
import com.datacomo.mc.spider.android.url.L;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class LoadSdCard {

	public static void loadGif(final String originaliconurl,
			final String savePath, final String filename,
			final Handler handler, final ImageView img, final int msgID) {
		new Thread() {
			public void run() {
				int currentSize = 0;
				InputStream fisFileInputStream = null;
				HttpURLConnection hrc = null;
				BufferedInputStream is = null;
				FileOutputStream fos = null;
				File proFile = null;
				File myFile = null;
				URL url;
				try {
					url = new URL(originaliconurl);

					hrc = HttpRequestServers.getHttpURLConnection(url);

					hrc.setRequestProperty("User-Agent", "NetBear");
					hrc.setRequestProperty("Content-type",
							"application/x-java-serialized-object");
					hrc.setRequestProperty("connection", "Keep-Alive");
					if (currentSize > 0) {
						hrc.setRequestProperty("RANGE", "bytes = "
								+ currentSize + "-");
					}
					hrc.setConnectTimeout(20 * 1000);
					hrc.setReadTimeout(30 * 1000);
					hrc.connect();

					fisFileInputStream = hrc.getInputStream();
					createFile(savePath);

					proFile = new File(savePath + filename + ".sp");
					myFile = new File(savePath + filename);
					fos = new FileOutputStream(proFile);
					is = new BufferedInputStream(fisFileInputStream);

					// int fileLength = hrc.getContentLength();
					// double split = (double) fileLength / 100;
					// int progress = 0;
					// int percent = 0;
					int length = 0;

					byte buf[] = new byte[1024];
					while ((length = is.read(buf)) != -1) {
						// progress = progress + (length);
						// if (progress > split) {
						// percent = (int) (percent + (progress / split));
						// if (percent <= 99) {
						// Message msg = new Message();
						// msg.what = 1;
						// msg.arg1 = percent;
						// handler.sendMessage(msg);
						// }
						// progress = 0;
						// }
						fos.write(buf, 0, length);
					}
					fos.flush();

					// int length = 0;
					// byte buf[] = new byte[1024];
					// while ((length = is.read(buf)) != -1) {
					// fos.write(buf, 0, length);
					// }
					// fos.flush();
					if (proFile != null && proFile.exists()) {
						proFile.renameTo(myFile);
					}
					Message msg = Message.obtain();
					msg.what = msgID;
					L.d("CircleBlogDetailsActivity_text",
							"load_iv_tag" + img.getTag()
									+ myFile.getAbsolutePath());
					msg.obj = new Object[] { img, myFile };
					handler.sendMessage(msg);

				} catch (Exception e) {
					// handler.sendEmptyMessage(2);
					// if (proFile != null && proFile.exists()) {
					// proFile.delete();
					// }
					e.printStackTrace();
				} finally {
					try {
						if (is != null)
							is.close();
						if (hrc != null)
							hrc.disconnect();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();

	}

	/**
	 * ���sd���Ƿ����
	 * 
	 * @return
	 */
	public static boolean IsCanUseSdCard() {
		try {
			return Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getLocalName(String path) {
		return path.substring(0, path.length() - 4);
	}

	/**
	 * �����ļ���
	 * 
	 * @param filePath
	 */
	public static void createFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * ����һ�����ļ�
	 * 
	 * @param filePath
	 * @return true �����ɹ� false ����ʧ�ܻ��ļ��Ѵ���
	 */
	public static boolean createNewFile(String filePath) {
		File file = new File(filePath);
		boolean result = false;
		if (!file.exists()) {
			try {
				file.createNewFile();
				result = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
