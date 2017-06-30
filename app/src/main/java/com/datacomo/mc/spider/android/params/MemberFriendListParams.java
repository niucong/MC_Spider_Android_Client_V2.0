package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 成员朋友列表
 * 
 * @author datacomo-160
 * 
 */
public class MemberFriendListParams extends BasicParams {

	/**
	 * 社员朋友列表参数设置
	 * 
	 * @param context
	 * @param memberId
	 * @param orderType
	 * @param startRecord
	 * @param maxResults
	 */
	public MemberFriendListParams(Context context, String memberId,
			String orderType, String startRecord, String maxResults) {
		super(context);
		setVariable(memberId, orderType, startRecord, maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String memberId, String orderType,
			String startRecord, String maxResults) {
		paramsMap.put("memberId", memberId);
		paramsMap.put("orderType", orderType);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", "friendList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
