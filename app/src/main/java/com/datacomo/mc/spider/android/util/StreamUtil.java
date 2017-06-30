package com.datacomo.mc.spider.android.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.AsyncTask;

public class StreamUtil {

	/**
	 * 读取请求数据
	 * 
	 * @param inSream
	 * @param charsetName
	 * @return
	 * @throws Exception
	 */
	public static String readData(InputStream inSream) throws Exception {
		if (inSream == null)
			return null;
		ByteArrayOutputStream outStream = null;
		String str = null;
		try {
			outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = inSream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			byte[] data = outStream.toByteArray();
			str = new String(data, "utf-8");
		} catch (OutOfMemoryError e) {
		} finally {
			if (outStream != null)
				outStream.close();
			if (inSream != null)
				inSream.close();
		}
		return str;
	}

	/**
	 * 把inStream转化为byte[]
	 * 
	 * @param inStream
	 * @return this stream's current contents as a byte array.
	 */
	public static byte[] readStream(InputStream inStream) {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];
		int len = 0;
		try {
			if (inStream != null) {
				while ((len = inStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != inStream)
					inStream.close();
				if (null != outStream)
					outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return outStream.toByteArray();
	}

}
