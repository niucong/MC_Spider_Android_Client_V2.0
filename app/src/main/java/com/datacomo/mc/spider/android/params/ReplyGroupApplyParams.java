package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 回应加入圈子申请、回应管理员申请
 * 
 * @author datacomo-160
 * 
 */
public class ReplyGroupApplyParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param method
	 * @param groupId
	 * @param memberId
	 * @param replyType
	 */
	public ReplyGroupApplyParams(Context context, String method, String groupId,
			String memberId, int replyType) {
		super(context);
		setVariable(method, groupId, memberId, replyType);
	}

	/**
	 * 设置参数
	 * 
	 * @param method
	 * @param groupId
	 * @param memberId
	 * @param replyType
	 */
	private void setVariable(String method, String groupId, String memberId,
			int replyType) {

		paramsMap.put("groupId", groupId);
		paramsMap.put("memberId", memberId);
		paramsMap.put("replyType", replyType + "");
		paramsMap.put("method", method);

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
