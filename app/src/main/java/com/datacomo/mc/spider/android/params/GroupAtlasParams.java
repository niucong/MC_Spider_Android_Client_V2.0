package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 圈子图谱——上级、下级、合作
 * 
 * @author datacomo-160
 * 
 */
public class GroupAtlasParams extends BasicParams {

	/**
	 * 圈子图谱——上级getFatherGroups、下级getSubGroups、合作getCollaborateGroups
	 * 
	 * @param groupId
	 */
	public GroupAtlasParams(Context context, String method, String groupId,
			String startRecord, String maxResults, String noPaging) {
		super(context);
		setVariable(method, groupId, startRecord, maxResults, noPaging);
	}

	/**
	 * 设置参数
	 * 
	 * @param loadName
	 * @param password
	 */
	private void setVariable(String method, String groupId, String startRecord,
			String maxResults, String noPaging) {
		if (startRecord != null && !"".equals(startRecord))
			paramsMap.put("startRecord", startRecord);
		if (maxResults != null && !"".equals(maxResults))
			paramsMap.put("maxResults", maxResults);
		if (noPaging != null && !"".equals(noPaging))
			paramsMap.put("noPaging", noPaging);
		paramsMap.put("groupId", groupId);
		paramsMap.put("method", method);
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
