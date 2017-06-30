package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取加入或踢出成员列表(动态使用)
 * 
 * @author datacomo-160
 * 
 */
public class LeaguresNewOrRemoveParams extends BasicParams {

	/**
	 * 获取加入或踢出成员列表(动态使用)
	 * 
	 * @param context
	 * @param groupId
	 * @param noPaging
	 * @param type
	 *            区分查询加入或踢出成员列表（0：加入的成员列表 1：踢出成员列表（默认值为：0））
	 * @param startRecord
	 * @param maxResults
	 */
	public LeaguresNewOrRemoveParams(Context context, String groupId,
			String noPaging, int type, String startRecord, String maxResults,
			long createTime) {
		super(context);
		setVariable(groupId, noPaging, type, startRecord, maxResults,
				createTime);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String groupId, String noPaging, int type,
			String startRecord, String maxResults, long createTime) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("noPaging", noPaging);
		paramsMap.put("type", type + "");
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("timestamp", createTime + "");
		paramsMap.put("method", "leaguresNewOrRemove");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
