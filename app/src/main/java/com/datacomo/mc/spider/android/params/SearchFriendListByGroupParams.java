package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 根据手机号/名字搜索朋友圈中的朋友
 * 
 * @author datacomo-160
 * 
 */
public class SearchFriendListByGroupParams extends BasicParams {

	/**
	 * 
	 * @param context
	 * @param method
	 * @param groupId
	 * @param keyWord
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	public SearchFriendListByGroupParams(Context context, String method,
			String groupId, String keyWord, String startRecord,
			String maxResult, String noPaging) {
		super(context);
		setVariable(method, groupId, keyWord, startRecord, maxResult, noPaging);
	}

	/**
	 * 设置参数
	 * 
	 * @param method
	 * @param groupId
	 * @param keyWord
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	private void setVariable(String method, String groupId, String keyWord,
			String startRecord, String maxResult, String noPaging) {
		paramsMap.put("groupId", groupId);
		paramsMap.put(keyWord, keyWord);
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
