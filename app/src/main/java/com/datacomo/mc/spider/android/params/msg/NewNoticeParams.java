package com.datacomo.mc.spider.android.params.msg;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取通知列表
 * 
 * @author datacomo-160
 * 
 */
public class NewNoticeParams extends BasicParams {

	/**
	 * 通知列表参数设置
	 * 
	 * @param context
	 * @param synchronize
	 *            是否与动态同步 1 同步0 不同步 默认值:0
	 * @param startRecord
	 * @param maxResults
	 * @param listType
	 *            所有类型:all,申请类型：apply，分享类型：share，未读通知：new。默认值：all
	 * @param time
	 *            开始时间时间戳
	 */
	public NewNoticeParams(Context context, String synchronize,
			String noPaging, String startRecord, String maxResults,
			String listType, String time) {
		super(context);
		setVariable(synchronize, noPaging, startRecord, maxResults, listType,
				time);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String synchronize, String noPaging,
			String startRecord, String maxResults, String listType, String time) {
		paramsMap.put("synchronize", synchronize);
		paramsMap.put("noPaging", noPaging);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("listType", listType);
		paramsMap.put("time", time);
		paramsMap.put("method", "newnotices");// noticesOfNew
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
