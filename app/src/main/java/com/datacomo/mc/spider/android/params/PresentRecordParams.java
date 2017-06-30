package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取圈子捐赠记录
 */
public class PresentRecordParams extends BasicParams {

	/**
	 * 
	 * @param context
	 * @param groupId
	 * @param maxResult
	 * @param noPaging
	 */
	public PresentRecordParams(Context context, String groupId,
			String startRecord, String maxResult, boolean noPaging) {
		super(context);
		setVariable(groupId, startRecord, maxResult, noPaging);
	}

	/**
	 * 设置参数
	 * 
	 * @param groupId
	 * @param maxResult
	 * @param noPaging
	 */
	private void setVariable(String groupId, String startRecord,
			String maxResult, boolean noPaging) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResult);
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("method", "getPresentRecord");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
