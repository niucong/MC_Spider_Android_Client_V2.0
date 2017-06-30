package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 搜索邀请社员列表
 */
public class SearchInviteMemberListParams extends BasicParams {

	/**
	 * 
	 * @param context
	 * @param groupId
	 * @param content
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	public SearchInviteMemberListParams(Context context, String groupId,
			String content, String startRecord, String maxResults,
			boolean noPaging) {
		super(context);
		setVariable(groupId, content, startRecord, maxResults, noPaging);
	}

	private void setVariable(String groupId, String content,
			String startRecord, String maxResults, boolean noPaging) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("content", content);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("method", "searchInviteMemberListForAndroid");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
