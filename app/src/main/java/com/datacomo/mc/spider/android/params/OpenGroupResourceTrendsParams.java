package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取未登录的圈子动态
 * 
 * @author datacomo-160
 * 
 */
public class OpenGroupResourceTrendsParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param pointId
	 */
	public OpenGroupResourceTrendsParams(Context context, String trendId,
			String maxResults, boolean noPaging) {
		super(context);
		setVariable(trendId, maxResults, noPaging);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String trendId, String maxResult, boolean noPaging) {
		if (trendId != null && !trendId.equals(""))
			paramsMap.put("trendId", trendId);
		paramsMap.put("maxResults", maxResult);
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("method", "openGroupResourceTrends");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
