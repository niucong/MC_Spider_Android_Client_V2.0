package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取推荐的人的列表
 * 
 * @author datacomo-160
 * 
 */
public class RecommendListParams extends BasicParams {

	/**
	 * 获取推荐的人的列表参数设置
	 * 
	 * @param context
	 * @param groupId
	 * @param recommendId
	 */
	public RecommendListParams(Context context, String groupId,
			String recommendId, String startRecord, String maxResults) {
		super(context);
		setVariable(groupId, recommendId, startRecord, maxResults);
	}

	/**
	 * 设置参数
	 * 
	 * @param groupId
	 * @param recommendId
	 */
	private void setVariable(String groupId, String recommendId,
			String startRecord, String maxResults) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("recommendId", recommendId);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", "recommendList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
