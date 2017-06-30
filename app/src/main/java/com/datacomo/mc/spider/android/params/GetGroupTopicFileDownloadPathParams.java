package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取圈博文件下载地址
 * 
 * @author datacomo-160
 * 
 */
public class GetGroupTopicFileDownloadPathParams extends BasicParams {

	/**
	 * 获取圈博文件下载地址参数设置
	 * 
	 * @param context
	 * @param @param groupId
	 * @param fileId
	 */
	public GetGroupTopicFileDownloadPathParams(Context context, String fileUrl,
			String filePath, String fileName) {
		super(context);
		setVariable(fileUrl, filePath, fileName);
	}

	/**
	 * 设置参数
	 * 
	 * @param fileUrl
	 * @param filePath
	 * @param fileName
	 */
	private void setVariable(String fileUrl, String filePath, String fileName) {

		paramsMap.put("fileUrl", fileUrl);
		paramsMap.put("filePath", filePath);
		paramsMap.put("fileName", fileName);
		paramsMap.put("method", "topicFileDown");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
