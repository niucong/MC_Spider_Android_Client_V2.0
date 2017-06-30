package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 搜索圈子资源列表
 * 
 * @author datacomo-160
 * 
 */
public class SingleResourceListForGroupSearchParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupId
	 *            圈子编号
	 * @param objectName
	 *            搜索条件（可以传操作者姓名（全称、拼音、简拼）、圈博的名称模糊搜索）
	 * @param objectType
	 *            对象的类型（为圈博类型）
	 * @param startRecord
	 *            开始记录
	 * @param maxResults
	 *            分页大小
	 * @param noPaging
	 */
	public SingleResourceListForGroupSearchParams(Context context,
			String groupId, String objectName, String objectType,
			String startRecord, String maxResults, boolean noPaging) {
		super(context);
		setVariable(groupId, objectName, objectType, startRecord, maxResults,
				noPaging);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String groupId, String objectName,
			String objectType, String startRecord, String maxResults,
			boolean noPaging) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("objectName", objectName);
		paramsMap.put("objectType", objectType);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("method", "searchSingleResourceListForGroup");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
