package com.datacomo.mc.spider.android.params.msg;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

public class SearchNoticesParams extends BasicParams {

	/**
	 * 搜索消息
	 * 
	 * @param context
	 * @param text
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	public SearchNoticesParams(Context context, String noticeTitle,
			String startRecord, String maxResults, String noPaging) {
		super(context);
		setVariable(noticeTitle, startRecord, maxResults, noPaging);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String noticeTitle, String startRecord,
			String maxResults, String noPaging) {
		paramsMap.put("method", "searchNotices");
		paramsMap.put("noticeTitle", noticeTitle);
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
