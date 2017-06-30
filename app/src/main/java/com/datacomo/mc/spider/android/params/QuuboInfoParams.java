package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 圈博详情
 * 
 * @author datacomo-160
 * 
 */
public class QuuboInfoParams extends BasicParams {

	/**
	 * 获取圈博文件下载地址参数设置
	 * 
	 * @param context
	 * @param @param groupId
	 * @param fileId
	 */
	public QuuboInfoParams(Context context, String groupId, String quuboId) {
		super(context);
		setVariable(groupId, quuboId);
	}

	/**
	 * 设置参数
	 * 
	 * @param fileUrl
	 * @param filePath
	 * @param fileName
	 */
	private void setVariable(String groupId, String quuboId) {

		paramsMap.put("groupId", groupId);
		paramsMap.put("quuboId", quuboId);
		paramsMap.put("method", "quuboInfo");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
