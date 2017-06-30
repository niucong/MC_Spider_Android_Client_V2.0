package com.datacomo.mc.spider.android.params.msg;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取我与某人的短信列表
 * 
 * @author datacomo-160
 * 
 */
public class ContactMemberMessagesParams extends BasicParams {

	/**
	 * 获取我与某人的短信列表参数设置
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param maxResults
	 */
	public ContactMemberMessagesParams(Context context, String method,
			String memberId, String startRecord, String maxResults,
			String startMessagesId) {
		super(context);
		setVariable(method, memberId, startRecord, maxResults, startMessagesId);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String method, String memberId,
			String startRecord, String maxResults, String startMessagesId) {
		// paramsMap.put("contactLeaguerId", contactLeaguerId);
		paramsMap.put("memberId", memberId);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("startMessagesId", startMessagesId);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", method);
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
