package com.datacomo.mc.spider.android.params.groupchat;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 删除圈聊信息
 * 
 * @author datacomo-287
 * 
 */
public class DeleteGroupChatMessageByMemberParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupId
	 * @param chatMessageId
	 */
	public DeleteGroupChatMessageByMemberParams(Context context,
			String groupId, String chatMessageId) {
		super(context);
		setVariable(groupId, chatMessageId);
	}

	/**
	 * 参数设置
	 * 
	 * @param groupId
	 * @param chatMessageId
	 */
	private void setVariable(String groupId, String chatMessageId) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("chatMessageId", chatMessageId);
		paramsMap.put("method", "deleteGroupChatMessageByMember");

		setVariable(true);

	}

	@Override
	public String getParams() {
		return strParams;
	}

}
