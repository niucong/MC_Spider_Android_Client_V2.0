package com.datacomo.mc.spider.android.params.groupchat;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * get GroupChatMessage
 * 
 * @author datacomo-287
 * 
 */
public class GroupChatMessageParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param chatId
	 * @param messageId
	 * @param newOrOld
	 * @param maxResults
	 * @param noPaging
	 */
	public GroupChatMessageParams(Context context, int chatId, int messageId,
			int newOrOld, int maxResults, boolean noPaging) {
		super(context);
		setVariable(chatId, messageId, newOrOld, maxResults, noPaging);
	}

	/**
	 * 参数设置
	 * 
	 * @param chatId
	 * @param messageId
	 * @param newOrOld
	 * @param maxResults
	 * @param noPaging
	 */
	private void setVariable(int chatId, int messageId, int newOrOld,
			int maxResults, boolean noPaging) {
		paramsMap.put("groupId", String.valueOf(chatId));
		paramsMap.put("messageId", String.valueOf(messageId));
		paramsMap.put("newOrOld", String.valueOf(newOrOld));
		paramsMap.put("maxResults", String.valueOf(maxResults));
		paramsMap.put("noPaging", String.valueOf(noPaging));
		paramsMap.put("method", "groupChatMessage");

		setVariable(true);

	}

	@Override
	public String getParams() {
		return strParams;
	}

}
