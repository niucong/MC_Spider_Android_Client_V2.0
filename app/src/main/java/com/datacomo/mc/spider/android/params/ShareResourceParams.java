package com.datacomo.mc.spider.android.params;

import java.util.HashMap;

import android.content.Context;

/**
 * 分享资源
 * 
 * @author datacomo-160
 * 
 */
public class ShareResourceParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupId
	 * @param objectId
	 * @param objectType
	 * @param isAllMember
	 * @param receiveMemberIds
	 */
	public ShareResourceParams(Context context, String groupId,
			String objectId, String objectType, boolean isAllMember,
			String[] receiveMemberIds) {
		super(context);
		setVariable(groupId, objectId, objectType, isAllMember,
				receiveMemberIds);
	}

	/**
	 * 设置参数
	 * 
	 * @param groupId
	 * @param objectId
	 * @param objectType
	 * @param isAllMember
	 * @param receiveMemberIds
	 */
	private void setVariable(String groupId, String objectId,
			String objectType, boolean isAllMember, String[] receiveMemberIds) {

		paramsMap.put("groupId", groupId);
		paramsMap.put("objectId", objectId);
		paramsMap.put("objectType", objectType);
		paramsMap.put("isAllMember", isAllMember + "");

		mHashMap = new HashMap<String, String[]>();
		if (receiveMemberIds != null && receiveMemberIds.length > 0) {
			mHashMap.put("receiveMemberIds", receiveMemberIds);
			paramsMap.put("receiveMemberIds", "");
		}
		paramsMap.put("method", "shareResource");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
