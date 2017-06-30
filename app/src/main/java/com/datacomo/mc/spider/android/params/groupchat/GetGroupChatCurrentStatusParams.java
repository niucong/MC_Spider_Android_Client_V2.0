package com.datacomo.mc.spider.android.params.groupchat;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取圈聊当前状态
 * 
 * @author datacomo-287
 * 
 */
public class GetGroupChatCurrentStatusParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param groupId
	 */
	public GetGroupChatCurrentStatusParams(Context context, String groupId) {
		super(context);
		setVariable(groupId);
	}

	/**
	 * 参数设置
	 * 
	 * @param groupId
	 */
	private void setVariable(String groupId) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("method", "getGroupChatCurrentStatus");
		setVariable(true);

	}

	@Override
	public String getParams() {
		return strParams;
	}

}
