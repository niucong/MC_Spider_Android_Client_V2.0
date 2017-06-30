package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获得所优优工作圈子动态墙列表
 * 
 * @author datacomo-160
 * 
 */
public class GetAllGroupTrendListParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param pointId
	 */
	public GetAllGroupTrendListParams(Context context, String trendId,
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
		paramsMap.put("method", "allGroupResourceTrends");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
