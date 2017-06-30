package com.datacomo.mc.spider.android.params.groupchat;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * search memberlist who can invited in groupchat
 * 
 * @author datacomo-287
 * 
 */
public class SearchInvitableGroupChatMemberListParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param chatId
	 * @param groupId
	 * @param nameOrPhone
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	public SearchInvitableGroupChatMemberListParams(Context context,
			int chatId, int groupId, String nameOrPhone, int startRecord,
			int maxResults, boolean noPaging) {
		super(context);
		setVariable(chatId, groupId, nameOrPhone, startRecord, maxResults,
				noPaging);
	}

	/**
	 * 参数设置
	 * 
	 * @param chatId
	 * @param groupId
	 * @param nameOrPhone
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	private void setVariable(int chatId, int groupId, String nameOrPhone,
			int startRecord, int maxResults, boolean noPaging) {
		paramsMap.put("groupId", String.valueOf(groupId));
		paramsMap.put("nameOrPhone", nameOrPhone);
		paramsMap.put("startRecord", String.valueOf(startRecord));
		paramsMap.put("maxResults", String.valueOf(maxResults));
		paramsMap.put("noPaging", String.valueOf(noPaging));
		paramsMap.put("method", "searchInvitableGroupChatMemberList");

		setVariable(true);

	}

	@Override
	public String getParams() {
		return strParams;
	}

}
