package com.datacomo.mc.spider.android.params.groupchat;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * get the memberlist who can be invited in groupchat
 * 
 * @author datacomo-287
 * 
 */
public class InvitableGroupChatMemberList extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param chatId
	 * @param groupId
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	public InvitableGroupChatMemberList(Context context, int chatId,
			int groupId, int startRecord, int maxResults, boolean noPaging) {
		super(context);
		setVariable(chatId, groupId, startRecord, maxResults, noPaging);
	}

	/**
	 * 参数设置
	 * 
	 * @param chatId
	 * @param groupId
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	private void setVariable(int chatId, int groupId, int startRecord,
			int maxResults, boolean noPaging) {
		paramsMap.put("groupId", String.valueOf(groupId));
		paramsMap.put("startRecord", String.valueOf(startRecord));
		paramsMap.put("maxResults", String.valueOf(maxResults));
		paramsMap.put("noPaging", String.valueOf(noPaging));

		paramsMap.put("method", "invitableGroupChatMemberList");

		setVariable(true);

	}

	@Override
	public String getParams() {
		return strParams;
	}

}
