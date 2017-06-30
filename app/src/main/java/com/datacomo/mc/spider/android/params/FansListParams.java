package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 成员被圈列表
 * 
 * @author datacomo-160
 * 
 */
public class FansListParams extends BasicParams {

	/**
	 * 成员被圈列表参数设置
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param maxResults
	 */
	public FansListParams(Context context, String memberId, String startRecord,
			String maxResults) {
		super(context);
		setVariable(memberId, startRecord, maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String memberId, String startRecord,
			String maxResults) {
		// method=friendList&memberId=0&startRecord=0&maxResults=20
		paramsMap.put("memberId", memberId);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", "fansList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
