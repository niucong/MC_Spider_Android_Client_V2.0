package com.datacomo.mc.spider.android.params;

import android.content.Context;

public class SearchFriendsForIPhoneParams extends BasicParams {

	/**
	 * 搜索朋友
	 * 
	 * @param context
	 * @param text
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	public SearchFriendsForIPhoneParams(Context context, String text,
			String startRecord, String maxResults, String noPaging) {
		super(context);
		setVariable(text, startRecord, maxResults, noPaging);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String text, String startRecord,
			String maxResults, String noPaging) {
		paramsMap.put("method", "searchFriendsForIPhone");
		paramsMap.put("text", text);
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
