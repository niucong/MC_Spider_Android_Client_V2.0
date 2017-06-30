package com.datacomo.mc.spider.android.params.msg;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取我的短信联系人列表
 * 
 * @author datacomo-160
 * 
 */
public class MyGroupChatListParams extends BasicParams {

	/**
	 * 成员被圈列表参数设置
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param noPaging
	 * @param maxResults
	 */
	public MyGroupChatListParams(Context context, String startRecord,
			String maxResults, String noPaging) {
		super(context);
		setVariable(startRecord, maxResults, noPaging);
	}

	/**
	 * 成员被圈列表参数设置
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param noPaging
	 * @param maxResults
	 */
	public MyGroupChatListParams(Context context, String startRecord,
			String maxResults, String noPaging, long startTime) {
		super(context);
		setVariable(startRecord, maxResults, noPaging, startTime);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String startRecord, String maxResults,
			String noPaging) {
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", noPaging);
		paramsMap.put("method", "contactMembersOfGroupChat");
		super.setVariable(true);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String startRecord, String maxResults,
			String noPaging, long startTime) {
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", noPaging);
		if (startTime != 0)
			paramsMap.put("startTime", startTime + "");
		paramsMap.put("method", "GETCONTACTGROUPS");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
