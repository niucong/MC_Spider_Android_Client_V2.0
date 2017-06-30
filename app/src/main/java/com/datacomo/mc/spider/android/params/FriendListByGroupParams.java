package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 按朋友圈查看朋友列表
 * 
 * @author datacomo-160
 * 
 */
public class FriendListByGroupParams extends BasicParams {

	/**
	 * 按朋友圈查看朋友列表参数设置
	 * 
	 * @param context
	 * @param groupId
	 * @param startRecord
	 * @param maxResult
	 */
	public FriendListByGroupParams(Context context, String groupId,
			String startRecord, String maxResult) {
		super(context);
		setVariable(groupId, startRecord, maxResult);
	}

	/**
	 * 设置参数
	 * 
	 * @param groupId
	 * @param startRecord
	 * @param maxResult
	 */
	private void setVariable(String groupId, String startRecord,
			String maxResult) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResult", maxResult);

		paramsMap.put("method", "friendListByGroup");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
