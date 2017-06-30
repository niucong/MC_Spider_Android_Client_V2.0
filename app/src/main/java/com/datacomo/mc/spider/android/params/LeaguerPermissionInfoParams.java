package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 访问者和圈子关系
 * 
 * @author datacomo-160
 * 
 */
public class LeaguerPermissionInfoParams extends BasicParams {

	/**
	 * 访问者和圈子关系参数设置
	 * 
	 * @param groupId
	 */
	public LeaguerPermissionInfoParams(Context context, String method,
			String groupId) {
		super(context);
		setVariable(method, groupId);
	}

	/**
	 * 设置参数
	 * 
	 * @param method
	 * @param groupId
	 */
	private void setVariable(String method, String groupId) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("method", method);
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
