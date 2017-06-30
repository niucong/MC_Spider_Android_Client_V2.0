package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取朋友有更新的列表
 * 
 * @author datacomo-160
 * 
 */
public class FriendGroupUpdateListParams extends BasicParams {

	/**
	 * 获取朋友有更新的列表
	 * 
	 * @param context
	 * @param method
	 * @param startUpdateTime
	 * @param groupIdss
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	public FriendGroupUpdateListParams(Context context, String method,
			String startUpdateTime, String startDeleteTime, String startRecord,
			String maxResults, String noPaging, String groupIdss) {
		super(context);
		setVariable(method, startUpdateTime, startDeleteTime, startRecord,
				maxResults, noPaging, groupIdss);
	}

	/**
	 * 设置参数
	 * 
	 * @param method
	 * @param startUpdateTime
	 * @param startDeleteTime
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 * @param groupIdss
	 */
	private void setVariable(String method, String startUpdateTime,
			String startDeleteTime, String startRecord, String maxResults,
			String noPaging, String groupIdss) {
		paramsMap.put("startDeleteTime", startDeleteTime);
		paramsMap.put("startUpdateTime", startUpdateTime);
		if ("myUpdateGroupsForClient".equals(method)) {
			if (groupIdss == null || "".equals(groupIdss))
				groupIdss = ",";
			paramsMap.put("groupIdss", groupIdss);
		}
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", noPaging);
		paramsMap.put("method", method);
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
