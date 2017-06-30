package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 搜索圈子
 * 
 * @author datacomo-160
 * 
 */
public class SearchGroupsParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param searchName
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	public SearchGroupsParams(Context context, String searchName,
			String startRecord, String maxResults, boolean noPaging) {
		super(context);
		setVariable(searchName, startRecord, maxResults, noPaging);
	}

	/**
	 * 设置参数
	 * 
	 * @param searchName
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	private void setVariable(String searchName, String startRecord,
			String maxResults, boolean noPaging) {
		paramsMap.put("searchName", searchName);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("method", "searchGroups");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
