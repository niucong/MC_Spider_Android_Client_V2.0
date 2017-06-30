package com.datacomo.mc.spider.android.params.groupchat;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * get GroupChatMessage
 * 
 * @author datacomo-287
 * 
 */
public class OtherMemberNewMessageParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupId
	 * @param messageId
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	public OtherMemberNewMessageParams(Context context, int groupId,
			int messageId, int startRecord, int maxResults, boolean noPaging) {
		super(context);
		setVariable(groupId, messageId, startRecord, maxResults, noPaging);
	}

	/**
	 * 参数设置
	 * 
	 * @param groupId
	 * @param messageId
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	private void setVariable(int groupId, int messageId, int startRecord,
			int maxResults, boolean noPaging) {
		paramsMap.put("groupId", String.valueOf(groupId));
		paramsMap.put("messageId", String.valueOf(messageId));
		paramsMap.put("startRecord", String.valueOf(startRecord));
		paramsMap.put("maxResults", String.valueOf(maxResults));
		paramsMap.put("noPaging", String.valueOf(noPaging));
		paramsMap.put("method", "otherMemberNewMessage");

		setVariable(true);

	}

	@Override
	public String getParams() {
		return strParams;
	}

}
