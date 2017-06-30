package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取邀请社员列表
 */
public class InviteMemberListParams extends BasicParams {

	/**
	 * 
	 * @param context
	 * @param groupId
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	public InviteMemberListParams(Context context, String groupId,
			String startRecord, String maxResults, boolean noPaging) {
		super(context);
		setVariable(groupId, startRecord, maxResults, noPaging);
	}

	private void setVariable(String groupId, String startRecord,
			String maxResults, boolean noPaging) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("method", "inviteMemberList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
