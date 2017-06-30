package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 交流圈成员列表
 */
public class GroupLeaguerListParams extends BasicParams {

	/**
	 * 
	 * @param context
	 * @param groupId
	 * @param maxResult
	 * @param noPaging
	 */
	public GroupLeaguerListParams(Context context, String groupId,
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
		paramsMap.put("maxResult", maxResult);
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("method", "groupLeaguerList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
