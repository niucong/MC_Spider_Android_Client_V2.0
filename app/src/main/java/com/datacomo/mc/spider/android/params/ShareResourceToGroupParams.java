package com.datacomo.mc.spider.android.params;

import java.util.HashMap;

import android.content.Context;

/**
 * 分享资源到圈子
 * 
 * @author datacomo-160
 * 
 */
public class ShareResourceToGroupParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupId
	 * @param objectId
	 * @param objectType
	 * @param receiveGroupIds
	 */
	public ShareResourceToGroupParams(Context context, String groupId,
			String objectId, String objectType, String[] receiveGroupIds) {
		super(context);
		setVariable(groupId, objectId, objectType, receiveGroupIds);
	}

	/**
	 * 设置参数
	 * 
	 * @param groupId
	 * @param objectId
	 * @param objectType
	 * @param receiveGroupIds
	 */
	private void setVariable(String groupId, String objectId,
			String objectType, String[] receiveGroupIds) {

		paramsMap.put("groupId", groupId);
		paramsMap.put("objectId", objectId);
		paramsMap.put("objectType", objectType);

		mHashMap = new HashMap<String, String[]>();
		if (receiveGroupIds != null && receiveGroupIds.length > 0) {
			mHashMap.put("receiveGroupIds", receiveGroupIds);
			paramsMap.put("receiveGroupIds", "");
		}
		paramsMap.put("method", "shareResourceToGroup");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
