package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 搜索圈子中的社员
 */
public class GroupSearchMemberParams extends BasicParams {

	/**
	 * 
	 * @param context
	 * @param groupId
	 * @param searchContent
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	public GroupSearchMemberParams(Context context, String groupId,
			String searchContent, String startRecord, String maxResult,
			boolean noPaging) {
		super(context);
		setVariable(groupId, searchContent, startRecord, maxResult, noPaging);
	}

	/**
	 * 设置参数
	 * 
	 * @param groupId
	 * @param searchContent
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	private void setVariable(String groupId, String searchContent,
			String startRecord, String maxResult, boolean noPaging) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("searchContent", searchContent);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResult", maxResult);
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("method", "groupSearchMember");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
