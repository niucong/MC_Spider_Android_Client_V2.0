package com.datacomo.mc.spider.android.params.groupchat;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * enter a groupchat
 * 
 * @author datacomo-287
 * 
 */
public class EnterGroupChatParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param groupId
	 */
	public EnterGroupChatParams(Context context, String groupId) {
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
		paramsMap.put("method", "enterGroupChat");
		setVariable(true);

	}

	@Override
	public String getParams() {
		return strParams;
	}

}
