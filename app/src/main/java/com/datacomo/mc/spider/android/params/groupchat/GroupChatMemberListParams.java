package com.datacomo.mc.spider.android.params.groupchat;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * get memberlist from groupchat
 * 
 * @author datacomo-287
 * 
 */
public class GroupChatMemberListParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param chatId
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	public GroupChatMemberListParams(Context context, int chatId,
			int startRecord, int maxResults, boolean noPaging) {
		super(context);
		setVariable(chatId, startRecord, maxResults, noPaging);
	}

	/**
	 * 参数设置
	 * 
	 * @param chatId
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	private void setVariable(int chatId, int startRecord, int maxResults,
			boolean noPaging) {
		paramsMap.put("groupId", String.valueOf(chatId));
		paramsMap.put("startRecord", String.valueOf(startRecord));
		paramsMap.put("maxResults", String.valueOf(maxResults));
		paramsMap.put("noPaging", String.valueOf(noPaging));
		paramsMap.put("method", "groupChatmemberList");

		setVariable(true);

	}

	@Override
	public String getParams() {
		return strParams;
	}
}