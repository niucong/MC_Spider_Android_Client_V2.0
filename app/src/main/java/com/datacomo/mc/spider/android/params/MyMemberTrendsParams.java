package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 我的动态
 * 
 * @author datacomo-160
 * 
 */
public class MyMemberTrendsParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param trendId
	 * @param objectType
	 * @param maxResults
	 * @param noPaging
	 */
	public MyMemberTrendsParams(Context context, String trendId,
			String objectType, String maxResults, boolean noPaging) {
		super(context);
		setVariable(trendId, objectType, maxResults, noPaging);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String trendId, String objectType,
			String maxResults, boolean noPaging) {
		if (trendId != null && !trendId.equals(""))
			paramsMap.put("trendId", trendId);
		paramsMap.put("objectType", objectType);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("method", "myMemberTrends");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
