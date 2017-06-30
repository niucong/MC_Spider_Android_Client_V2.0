package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 圈子成员管理
 */
public class GroupLeaguerManageParams extends BasicParams {

	/**
	 * 
	 * @param context
	 * @param method
	 *            移除社员removeFromGroup、任命圈子管理员appointGroupManager、
	 *            撤销管理员revokeManager
	 * @param groupId
	 * @param memberId
	 */
	public GroupLeaguerManageParams(Context context, String method,
			String groupId, String memberId) {
		super(context);
		setVariable(method, groupId, memberId);
	}

	/**
	 * 设置参数
	 * 
	 * @param method
	 * @param groupId
	 * @param memberId
	 */
	private void setVariable(String method, String groupId, String memberId) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("memberId", memberId);
		paramsMap.put("method", method);
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
