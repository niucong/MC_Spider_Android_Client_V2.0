package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取圈子管理员列表
 * 
 * @author datacomo-160
 * 
 */
public class GroupManagerListParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupId
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	public GroupManagerListParams(Context context, String groupId,
			String startRecord, String maxResults, boolean noPaging) {
		super(context);
		setVariable(groupId, startRecord, maxResults, noPaging);
	}

	/**
	 * 设置参数
	 * 
	 * @param groupId
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	private void setVariable(String groupId, String startRecord,
			String maxResults, boolean noPaging) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("method", "groupManagerList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
