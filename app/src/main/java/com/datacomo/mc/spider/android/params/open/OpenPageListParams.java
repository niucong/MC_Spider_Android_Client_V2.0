package com.datacomo.mc.spider.android.params.open;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取开放主页列表
 * 
 * @author datacomo-160
 * 
 */
public class OpenPageListParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param orderType
	 *            1、最新入驻2、热门的3、我关注的
	 * @param noPaging
	 * @param startRecord
	 * @param maxResults
	 */
	public OpenPageListParams(Context context, String orderType,
			String noPaging, String startRecord, String maxResults) {
		super(context);
		setVariable(orderType, noPaging, startRecord, maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String orderType, String noPaging,
			String startRecord, String maxResults) {
		paramsMap.put("orderType", orderType);
		paramsMap.put("noPaging", noPaging);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", "openPageList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
