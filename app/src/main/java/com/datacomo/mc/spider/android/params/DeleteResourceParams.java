package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 删除资源
 * 
 * @author datacomo-160
 * 
 */
public class DeleteResourceParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupId
	 * @param objectId
	 * @param objectType
	 */
	public DeleteResourceParams(Context context, String groupId,
			String objectId, String objectType) {
		super(context);
		setVariable(groupId, objectId, objectType);
	}

	/**
	 * 设置参数
	 * 
	 * @param groupId
	 * @param objectId
	 * @param objectType
	 */
	private void setVariable(String groupId, String objectId, String objectType) {

		paramsMap.put("groupId", groupId);
		paramsMap.put("objectId", objectId);
		paramsMap.put("objectType", objectType);
		paramsMap.put("method", "deleteResource");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
