package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获得圈子列表
 * 
 * @author datacomo-160
 * 
 */
public class CommGroupListsParams extends BasicParams {

	/**
	 * 设置参数
	 * 
	 * @param context
	 * @param searchType
	 *            查询范围【myJoinedGroups(我加入的)、myGroups(我管理的)、all 默认为所有】
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 *            false:分页、true不分页
	 */
	public CommGroupListsParams(Context context, String searchType,
			String startRecord, String maxResult, boolean noPaging) {
		super(context);
		setVariable(searchType, startRecord, maxResult, noPaging);
	}

	/**
	 * 设置参数
	 * 
	 * @param searchType
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	private void setVariable(String searchType, String startRecord,
			String maxResult, boolean noPaging) {
		if (!noPaging) {
			paramsMap.put("startRecord", startRecord);
			paramsMap.put("maxResults", maxResult);
		}
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("searchType", searchType);
		paramsMap.put("method", "groupList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
