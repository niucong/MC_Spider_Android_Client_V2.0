package com.datacomo.mc.spider.android.params.theme;

import com.datacomo.mc.spider.android.params.BasicParams;

import android.content.Context;

/**
 * 搜索专题
 * 
 * @author datacomo-160
 * 
 */
public class SearchThemesListParams extends BasicParams {

	/**
	 * 
	 * @param context
	 * @param groupId
	 *            圈子（当isGroup为false时不起作用）
	 * @param objectName
	 *            搜索条件（可以传专题发布者的名字或专题名称）
	 * @param startRecord
	 * @param maxResults
	 * @param isGroup
	 *            判断是否是单个圈子（true false）
	 */
	public SearchThemesListParams(Context context, String groupId,
			String objectName, String startRecord, String maxResults,
			boolean isGroup) {
		super(context);
		setVariable(groupId, objectName, startRecord, maxResults, isGroup);
	}

	private void setVariable(String groupId, String objectName,
			String startRecord, String maxResults, boolean isGroup) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("objectName", objectName);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", "false");
		paramsMap.put("isGroup", "" + isGroup);
		paramsMap.put("method", "searchThemesList");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
