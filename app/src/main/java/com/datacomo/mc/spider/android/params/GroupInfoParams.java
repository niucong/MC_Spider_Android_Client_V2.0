package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取社员基本信息，可以简单获取显示用户的基本信息包括：头像，昵称，心情用语，性别
 * 
 * @author datacomo-160
 * 
 */
public class GroupInfoParams extends BasicParams {

	private boolean isOpenPage = false;

	/**
	 * 登录参数设置
	 * 
	 * @param groupId
	 */
	public GroupInfoParams(Context context, String groupId) {
		super(context);
		setVariable(groupId);
	}

	/**
	 * 登录参数设置
	 * 
	 * @param groupId
	 */
	public GroupInfoParams(Context context, String groupId, boolean isOpenPage) {
		super(context);
		this.isOpenPage = isOpenPage;
		setVariable(groupId);
	}

	/**
	 * 设置参数
	 * 
	 * @param loadName
	 * @param password
	 */
	private void setVariable(String groupId) {
		if (isOpenPage) {
			paramsMap.put("openPageId", groupId);
			paramsMap.put("method", "openPageInfo");
		} else {
			paramsMap.put("groupId", groupId);
			paramsMap.put("method", "groupInfo");
		}
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
