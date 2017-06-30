package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 捐赠圈币给圈子
 */
public class PresentGoldtoGroupParams extends BasicParams {

	/**
	 * 
	 * @param context
	 * @param groupId
	 * @param goldNum
	 */
	public PresentGoldtoGroupParams(Context context, String groupId,
			String goldNum) {
		super(context);
		setVariable(groupId, goldNum);
	}

	/**
	 * 设置参数
	 * 
	 * @param groupId
	 * @param goldNum
	 */
	private void setVariable(String groupId, String goldNum) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("goldNum", goldNum);
		paramsMap.put("method", "presentGoldtoGroup");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
