package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 分享资源到开放主页
 * 
 * @author datacomo-160
 * 
 */
public class ForwardToOpenPageParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupId
	 * @param objectId
	 * @param objectType
	 * @param openPageId
	 */
	public ForwardToOpenPageParams(Context context, String groupId,
			String objectId, String objectType, String openPageId) {
		super(context);
		setVariable(groupId, objectId, objectType, openPageId);
	}

	/**
	 * 设置参数
	 * 
	 * @param groupId
	 * @param objectId
	 * @param objectType
	 * @param openPageId
	 */
	private void setVariable(String groupId, String objectId,
			String objectType, String openPageId) {

		paramsMap.put("groupId", groupId);
		paramsMap.put("objectId", objectId);
		paramsMap.put("objectType", objectType);
		paramsMap.put("openPageId", openPageId);
		paramsMap.put("method", "forwardToOpenPage");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
