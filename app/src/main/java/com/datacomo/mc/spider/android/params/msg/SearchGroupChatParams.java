package com.datacomo.mc.spider.android.params.msg;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

public class SearchGroupChatParams extends BasicParams {

	/**
	 * 搜索圈聊联系人
	 * 
	 * @param context
	 * @param text
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	public SearchGroupChatParams(Context context, String text,
			String startRecord, String maxResults, String noPaging) {
		super(context);
		setVariable(text, startRecord, maxResults, noPaging);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String text, String startRecord,
			String maxResults, String noPaging) {
		paramsMap.put("method", "searchContactMembersOfGroupChat");
		paramsMap.put("chatTitle", text);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", noPaging);

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
