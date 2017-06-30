package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 最近访客列表
 * 
 * @author datacomo-160
 * 
 */
public class VisitFriendListForIPhoneParams extends BasicParams {

	/**
	 * 登录参数设置
	 * 
	 * @param memberId
	 *            1、“”查自己，2、查朋友填对应memberId
	 */
	public VisitFriendListForIPhoneParams(Context context, String memberId,
			String startRecord, String maxResults) {
		super(context);
		setVariable(memberId, startRecord, maxResults);
	}

	/**
	 * 设置参数
	 * 
	 * @param loadName
	 * @param password
	 */
	private void setVariable(String memberId, String startRecord,
			String maxResults) {
		if (memberId == null || "".equals(memberId))
			memberId = "0";
		paramsMap.put("memberId", memberId);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", "visitFriendListForIPhone");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
