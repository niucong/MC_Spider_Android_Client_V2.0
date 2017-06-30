package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 接收推荐朋友圈
 * 
 * @author datacomo-160
 * 
 */
public class AcceptRecommendedParams extends BasicParams {

	/**
	 * 接收推荐朋友圈
	 * 
	 * @param context
	 * @param groupName
	 *            朋友圈名字
	 * @param groupId
	 *            推荐朋友圈编号
	 * @param recommendId
	 *            推荐编号
	 */
	public AcceptRecommendedParams(Context context, String groupName,
			String groupId, String recommendId) {
		super(context);
		setVariable(groupName, groupId, recommendId);
	}

	private void setVariable(String groupName, String groupId,
			String recommendId) {
		paramsMap.put("groupName", groupName);
		paramsMap.put("groupId", groupId);
		paramsMap.put("recommendId", recommendId);
		paramsMap.put("method", "acceptRecommended");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
