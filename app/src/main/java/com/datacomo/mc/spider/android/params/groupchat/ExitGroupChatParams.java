package com.datacomo.mc.spider.android.params.groupchat;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 退出圈聊
 * 
 * @author datacomo-287
 * 
 */
public class ExitGroupChatParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupId
	 */
	public ExitGroupChatParams(Context context, int groupId) {
		super(context);
		setVariable(groupId);
	}

	/**
	 * 参数设置
	 * 
	 * @param groupId
	 */
	private void setVariable(int groupId) {
		paramsMap.put("groupId", String.valueOf(groupId));
		paramsMap.put("method", "exitGroupChat");

		setVariable(true);

	}

	@Override
	public String getParams() {
		return strParams;
	}

}
