package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 资源详情
 * 
 */
public class ResourceInfoParams extends BasicParams {

	/**
	 * 
	 * @param context
	 * @param groupId
	 * @param objectId
	 * @param objectType
	 */
	public ResourceInfoParams(Context context, String groupId, String objectId,
			String objectType) {
		super(context);
		setVariable(groupId, objectId, objectType);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String groupId, String objectId, String objectType) {
		if (groupId != null && !"".equals(groupId)) {
			paramsMap.put("groupId", groupId);
		}
		paramsMap.put("objectId", objectId);
		paramsMap.put("objectType", objectType);
		paramsMap.put("method", "resourceInfo");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
