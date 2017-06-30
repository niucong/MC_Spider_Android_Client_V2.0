package com.datacomo.mc.spider.android.params.msg;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取通知列表
 * 
 * @author datacomo-160
 * 
 */
public class NoticeListParams extends BasicParams {

	/**
	 * 通知列表参数设置
	 * 
	 * @param context
	 * @param synchronize
	 * @param startRecord
	 * @param maxResults
	 */
	public NoticeListParams(Context context, String synchronize,
			String noPaging, String startRecord, String maxResults) {
		super(context);
		setVariable(synchronize, noPaging, startRecord, maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String synchronize, String noPaging,
			String startRecord, String maxResults) {
		paramsMap.put("synchronize", synchronize);
		paramsMap.put("noPaging", noPaging);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", "noticeList");// noticesOfNew
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
