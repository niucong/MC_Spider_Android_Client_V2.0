package com.datacomo.mc.spider.android.params.groupchat;

import java.util.HashMap;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * invite a leaguer enter a chat
 * 
 * @author datacomo-287
 * 
 */
public class InviteLeaguerEnterChat extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param memberIds
	 * @param chatId
	 * @param groupId
	 */
	public InviteLeaguerEnterChat(Context context, String[] memberIds,
			int chatId, int groupId) {
		super(context);
		setVariable(memberIds, chatId, groupId);
	}

	/**
	 * 参数设置
	 * 
	 * @param memberIds
	 * @param chatId
	 * @param groupId
	 */
	private void setVariable(String[] memberIds, int chatId, int groupId) {
		mHashMap = new HashMap<String, String[]>();
		if (memberIds != null && memberIds.length > 0) {
			mHashMap.put("memberIds", memberIds);
			paramsMap.put("memberIds", "");
		}

		paramsMap.put("groupId", String.valueOf(groupId));
		paramsMap.put("method", "inviteLeaguerEnterChat");

		setVariable(true);

	}

	@Override
	public String getParams() {
		return strParams;
	}

}
