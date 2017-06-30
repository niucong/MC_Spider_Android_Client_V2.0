package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 圈子资源列表
 * 
 * @author datacomo-160
 * 
 */
public class SingleResourceListForGroupParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupId
	 * @param objectType
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	public SingleResourceListForGroupParams(Context context, String groupId,
			boolean isGroup, String objectType, String startRecord,
			String maxResults, boolean noPaging) {
		super(context);
		setVariable(groupId, isGroup, objectType, startRecord, maxResults,
				noPaging);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String groupId, boolean isGroup,
			String objectType, String startRecord, String maxResults,
			boolean noPaging) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("isGroup", isGroup + "");
		paramsMap.put("objectType", objectType);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("method", "singleResourceListForGroup");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
