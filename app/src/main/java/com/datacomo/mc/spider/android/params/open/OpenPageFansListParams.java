package com.datacomo.mc.spider.android.params.open;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取开放主页粉丝列表
 * 
 * @author datacomo-160
 * 
 */
public class OpenPageFansListParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param openPageId
	 * @param noPaging
	 * @param startRecord
	 * @param maxResults
	 */
	public OpenPageFansListParams(Context context, String openPageId, String noPaging,
			String startRecord, String maxResults) {
		super(context);
		setVariable(openPageId, noPaging, startRecord, maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String openPageId, String noPaging,
			String startRecord, String maxResults) {
		paramsMap.put("openPageId", openPageId);
		paramsMap.put("noPaging", noPaging);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", "openPageFansList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
