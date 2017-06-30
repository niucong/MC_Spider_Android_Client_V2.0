package com.datacomo.mc.spider.android.params.groupchat;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 踢出圈聊成员
 * 
 * @author datacomo-287
 * 
 */
public class RemoveMemberFromGroupChatParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupId
	 * @param memberId
	 */
	public RemoveMemberFromGroupChatParams(Context context, int groupId,
			int memberId) {
		super(context);
		setVariable(groupId, memberId);
	}

	/**
	 * 参数设置
	 * 
	 * @param groupId
	 * @param memberId
	 */
	private void setVariable(int groupId, int memberId) {
		paramsMap.put("memberId", String.valueOf(memberId));
		paramsMap.put("chatId", String.valueOf(groupId));
		paramsMap.put("method", "removeMemberFromGroupChat");

		setVariable(true);

	}

	@Override
	public String getParams() {
		return strParams;
	}

}
