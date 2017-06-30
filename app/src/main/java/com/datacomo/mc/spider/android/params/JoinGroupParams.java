package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 加入圈子
 */
public class JoinGroupParams extends BasicParams {

	/**
	 * 加入圈子参数设置
	 * 
	 * @param groupId
	 */
	public JoinGroupParams(Context context, String groupId) {
		super(context);
		setVariable(groupId);
	}

	/**
	 * 设置参数
	 * 
	 * @param loadName
	 * @param password
	 */
	private void setVariable(String groupId) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("method", "applyJoinGroup");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
