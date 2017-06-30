package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 某个圈子动态
 * 
 * @author datacomo-160
 * 
 */
public class SingleGroupResourceTrendsParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupId
	 * @param trendId
	 * @param maxResults
	 * @param noPaging
	 */
	public SingleGroupResourceTrendsParams(Context context, String groupId,
			String trendId, String maxResults, boolean noPaging) {
		super(context);
		setVariable(groupId, trendId, maxResults, noPaging);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String groupId, String trendId, String maxResults,
			boolean noPaging) {
		if (trendId != null && !trendId.equals(""))
			paramsMap.put("trendId", trendId);
		paramsMap.put("groupId", groupId);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("method", "singleGroupResourceTrends");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
