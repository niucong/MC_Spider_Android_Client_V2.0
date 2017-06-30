package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取文件下载地址
 * 
 * @author datacomo-160
 * 
 */
public class GetFileDownloadPathParams extends BasicParams {

	/**
	 * 社员朋友、通信录列表参数设置
	 * 
	 * @param context
	 * @param groupId
	 * @param fileId
	 */
	public GetFileDownloadPathParams(Context context, String groupId,
			String fileId) {
		super(context);
		setVariable(groupId, fileId);
	}

	/**
	 * 设置参数
	 * 
	 * @param groupId
	 * @param fileId
	 */
	private void setVariable(String groupId, String fileId) {

		if (groupId != null) {
			paramsMap.put("groupId", groupId);
		}
		paramsMap.put("fileId", fileId);
		paramsMap.put("method", "downloadFile");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
