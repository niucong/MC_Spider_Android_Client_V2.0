package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获得朋友圈动态墙列表
 * 
 * @author datacomo-160
 * 
 */
public class MyFriendsTrendsParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param pointId
	 */
	public MyFriendsTrendsParams(Context context, String trendId,
			String maxResults, String createTime, boolean noPaging) {
		super(context);
		setVariable(trendId, maxResults, createTime, noPaging);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String trendId, String maxResult,
			String createTime, boolean noPaging) {
		if (trendId != null && !trendId.equals(""))
			paramsMap.put("trendId", trendId);
		paramsMap.put("maxResults", maxResult);
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("createTime", createTime);
		paramsMap.put("method", "myFriendsTrends");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
