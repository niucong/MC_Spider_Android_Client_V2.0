package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * replyCollaborate回应圈子合作申请 replySubGroupApply回应下级圈子申请 replyCombine回应合并申请
 * 
 * @author datacomo-160
 * 
 */
public class ReplyGroupOperParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param method
	 * @param srcGroupId
	 * @param destGroupId
	 * @param isAgree
	 */
	public ReplyGroupOperParams(Context context, String method,
			String srcGroupId, String destGroupId, boolean isAgree) {
		super(context);
		setVariable(method, srcGroupId, destGroupId, isAgree);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String method, String srcGroupId,
			String destGroupId, boolean isAgree) {
		paramsMap.put("method", method);
		paramsMap.put("srcGroupId", srcGroupId);
		paramsMap.put("destGroupId", destGroupId);
		paramsMap.put("isAgree", isAgree + "");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
