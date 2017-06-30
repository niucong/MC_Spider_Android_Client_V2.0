package com.datacomo.mc.spider.android.params;

import java.util.Map;

import android.content.Context;

/**
 * 上传头像
 * 
 * @author datacomo-160
 * 
 */
public class UploadFileParams extends BasicParams {

	/**
	 * 上传文件参数设置
	 */
	public UploadFileParams(Context context, String method, String uploadName,
			String groupId) {
		super(context);
		setVariable(method, uploadName, groupId);
	}

	/**
	 * 上传文件参数设置
	 */
	public UploadFileParams(Context context, String method, String uploadName,
			long l) {
		super(context);
		setVariable(method, uploadName, l);
	}

	/**
	 * 设置参数
	 * 
	 * @param loadName
	 * @param password
	 */
	private void setVariable(String method, String uploadName, String groupId) {
		if (groupId != null)
			paramsMap.put("groupId", groupId);
		if (uploadName != null)
			paramsMap.put("uploadName", uploadName);
		paramsMap.put("method", method);

		super.setVariable(true);
	}

	/**
	 * 设置参数
	 * 
	 * @param loadName
	 * @param password
	 */
	private void setVariable(String method, String uploadName, long l) {
		paramsMap.put("l", l + "");
		if (uploadName != null)
			paramsMap.put("uploadName", uploadName);
		paramsMap.put("method", method);

		super.setVariable(true);
	}

	public Map<String, String> getParamsMap() {
		return paramsMap;
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
