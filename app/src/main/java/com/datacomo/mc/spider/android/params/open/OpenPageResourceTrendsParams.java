package com.datacomo.mc.spider.android.params.open;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取开放主页动态
 * 
 * @author datacomo-160
 * 
 */
public class OpenPageResourceTrendsParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param objectType
	 *            对象类型
	 * @param isFilterByType
	 *            是否按类型查看 是：true 否：false
	 * @param openPageId
	 *            开放主页编号
	 * @param isAllOpenPage
	 *            是否是所有开放主页
	 * @param trendId
	 *            动态编号
	 * @param noPaging
	 *            true分页,false不分页
	 * @param maxResults
	 *            最大记录
	 */
	public OpenPageResourceTrendsParams(Context context, String objectType,
			String isFilterByType, String openPageId, String isAllOpenPage,
			String trendId, String noPaging, String maxResults) {
		super(context);
		setVariable(objectType, isFilterByType, openPageId, isAllOpenPage,
				trendId, noPaging, maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String objectType, String isFilterByType,
			String openPageId, String isAllOpenPage, String trendId,
			String noPaging, String maxResults) {
		paramsMap.put("objectType", objectType);
		paramsMap.put("isFilterByType", isFilterByType);
		paramsMap.put("openPageId", openPageId);
		paramsMap.put("isAllOpenPage", isAllOpenPage);
		paramsMap.put("trendId", trendId);
		paramsMap.put("noPaging", noPaging);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", "openPageResourceTrends");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
