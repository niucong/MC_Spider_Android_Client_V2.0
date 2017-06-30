package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 社员朋友列表，获取通信录
 * 
 * @author datacomo-160
 * 
 */
public class GetFriendListParams extends BasicParams {

	/**
	 * 社员朋友、通信录列表参数设置
	 * 
	 * @param context
	 * @param method
	 *            社区成员列表friendList 获取通信录getPhonebookContacters
	 * @param startRecord
	 *            开始记录
	 * @param maxResult
	 *            结束记录
	 * @param noPaging
	 *            是否不分页 true：不分页 false：分页
	 */
	public GetFriendListParams(Context context, String method,
			String startRecord, String maxResult, String noPaging) {
		super(context);
		setVariable(method, startRecord, maxResult, noPaging);
	}

	/**
	 * 设置参数
	 * 
	 * @param startRecord
	 *            开始记录
	 * @param maxResult
	 *            结束记录
	 * @param noPaging
	 *            是否不分页 true：不分页 false：分页
	 */
	private void setVariable(String method, String startRecord,
			String maxResult, String noPaging) {
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResult", maxResult);
		paramsMap.put("noPaging", noPaging);

		paramsMap.put("method", method);
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
