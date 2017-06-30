package com.datacomo.mc.spider.android.params.groupchat;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * get ContactMembers for groupchat
 * 
 * @author datacomo-287
 * 
 */
public class ContactMembersForGroupChatParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	public ContactMembersForGroupChatParams(Context context,
			String startRecord, String maxResults, boolean noPaging) {
		super(context);
		setVariable(startRecord, maxResults, noPaging);
	}

	/**
	 * 参数设置
	 * 
	 * @param startRecord
	 * @param maxResults
	 * @param isPaging
	 */
	private void setVariable(String startRecord, String maxResults,
			boolean noPaging) {
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", String.valueOf(noPaging));
		paramsMap.put("method", "contactMembersForGroupChat");

		setVariable(true);

	}

	@Override
	public String getParams() {
		return strParams;
	}
}