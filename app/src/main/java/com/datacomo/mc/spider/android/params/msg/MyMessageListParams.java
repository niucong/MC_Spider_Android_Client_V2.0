package com.datacomo.mc.spider.android.params.msg;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取我的短信联系人列表
 * 
 * @author datacomo-160
 * 
 */
public class MyMessageListParams extends BasicParams {

	/**
	 * 成员被圈列表参数设置
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param maxResults
	 */
	public MyMessageListParams(Context context, String startRecord,
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
		paramsMap.put("method", "myMessageList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
