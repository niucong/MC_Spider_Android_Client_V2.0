package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 朋友动态列表
 * 
 * @author datacomo-160
 * 
 */
public class OtherMemberTrendsParams extends BasicParams {

	/**
	 * 社员朋友列表参数设置
	 * 
	 * @param context
	 * @param memberId
	 * @param objectType
	 */
	public OtherMemberTrendsParams(Context context, String memberId,
			String objectType, String isFilterByType, boolean noPaging,
			String trendId, String createTime, String maxResults) {
		super(context);
		setVariable(memberId, objectType, isFilterByType, noPaging, trendId,
				createTime, maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String memberId, String objectType,
			String isFilterByType, boolean noPaging, String trendId,
			String createTime, String maxResults) {
		if (memberId == null || "".equals(memberId))
			memberId = "0";
		paramsMap.put("memberId", memberId);
		paramsMap.put("objectType", objectType);
		paramsMap.put("isFilterByType", isFilterByType);
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("trendId", trendId);
		paramsMap.put("createTime", createTime);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", "otherMemberTrends");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
