package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取备份通讯录时间列表
 * 
 * @author datacomo-160
 * 
 */
public class GetRestorePointsParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	public GetRestorePointsParams(Context context, String startRecord,
			String maxResult, String noPaging) {
		super(context);
		setVariable(startRecord, maxResult, noPaging);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String startRecord, String maxResult,
			String noPaging) {

		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResult", maxResult);
		paramsMap.put("noPaging", noPaging);

		paramsMap.put("method", "getrestorepoints");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
