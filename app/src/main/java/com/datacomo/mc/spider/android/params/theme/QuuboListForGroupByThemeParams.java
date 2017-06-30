package com.datacomo.mc.spider.android.params.theme;

import com.datacomo.mc.spider.android.params.BasicParams;

import android.content.Context;

/**
 * 获取圈子中某个话题的圈博列表
 * 
 * @author datacomo-160
 * 
 */
public class QuuboListForGroupByThemeParams extends BasicParams {

	/**
	 * 获取圈子中某个话题的圈博列表
	 * 
	 * @param themeId
	 *            ：专题ID
	 * @param groupId
	 *            ：圈子ID，默认0
	 * @param startRecord
	 *            ：分页参数
	 * @param maxResults
	 *            ：分页大小，默认20
	 * @param noPaging
	 *            ：是否分页，默认false分页
	 */
	public QuuboListForGroupByThemeParams(Context context, String groupId,
			String themeId, String startRecord, String maxResults) {
		super(context);
		setVariable(groupId, themeId, startRecord, maxResults);
	}

	private void setVariable(String groupId, String themeId,
			String startRecord, String maxResults) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("themeId", themeId);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", "getQuuboListForGroupByTheme");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
