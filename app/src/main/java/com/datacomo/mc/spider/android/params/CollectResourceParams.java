package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 收藏、取消收藏资源
 * 
 * @author datacomo-160
 * 
 */
public class CollectResourceParams extends BasicParams {

	/**
	 * 
	 * @param context
	 * @param method
	 * @param groupId
	 * @param objectId
	 * @param objectType
	 */
	public CollectResourceParams(Context context, String method,
			String groupId, String objectId, String objectType) {
		super(context);
		setVariable(method, groupId, objectId, objectType);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String method, String groupId, String objectId,
			String objectType) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("objectId", objectId);
		paramsMap.put("objectType", objectType);
		paramsMap.put("method", method);

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
