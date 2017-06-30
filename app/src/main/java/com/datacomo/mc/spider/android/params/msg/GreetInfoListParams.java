package com.datacomo.mc.spider.android.params.msg;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取招呼列表
 * 
 * @author datacomo-160
 * 
 */
public class GreetInfoListParams extends BasicParams {

	/**
	 * 招呼列表参数设置
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param maxResults
	 */
	public GreetInfoListParams(Context context, String startRecord,
			String maxResults) {
		super(context);
		setVariable(startRecord, maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String startRecord, String maxResults) {
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", "greetInfoList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
