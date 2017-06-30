package com.datacomo.mc.spider.android.params.groupchat;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 退出圈聊
 * 
 * @author datacomo-287
 * 
 */
public class UpdateGroupChatListenStatusParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupId
	 * @param listenStatus
	 */
	public UpdateGroupChatListenStatusParams(Context context, int groupId,
			int listenStatus) {
		super(context);
		setVariable(groupId, listenStatus);
	}

	/**
	 * 参数设置
	 * 
	 * @param groupId
	 * @param listenStatus
	 */
	private void setVariable(int groupId, int listenStatus) {
		paramsMap.put("groupId", String.valueOf(groupId));
		paramsMap.put("listenStatus", String.valueOf(listenStatus));
		paramsMap.put("method", "updateGroupChatListenStatus");

		setVariable(true);

	}

	@Override
	public String getParams() {
		return strParams;
	}

}
