package com.datacomo.mc.spider.android.params.theme;

import com.datacomo.mc.spider.android.params.BasicParams;

import android.content.Context;

/**
 * 获取圈子中的话题列表
 * 
 * @author datacomo-160
 * 
 */
public class ThemesListParams extends BasicParams {

	/**
	 * 获取圈子中的话题列表
	 * 
	 * @param groupId
	 *            ：圈子ID，默认0
	 * @param startRecord
	 *            ：分页参数
	 * @param maxResults
	 *            ：分页大小，默认20
	 */
	public ThemesListParams(Context context, String groupId,
			String startRecord, String maxResults) {
		super(context);
		setVariable(groupId, startRecord, maxResults);
	}

	private void setVariable(String groupId, String startRecord,
			String maxResults) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", "false");
		paramsMap.put("method", "getThemesList");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
