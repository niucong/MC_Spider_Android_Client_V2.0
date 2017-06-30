package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 赞资源
 * 
 * @author datacomo-160
 * 
 */
public class PraiseResourceParams extends BasicParams {

	/**
	 * 
	 * @param context
	 * @param groupId
	 * @param quuboId
	 * @param objectType
	 */
	public PraiseResourceParams(Context context, String groupId,
			String quuboId, String objectType) {
		super(context);
		setVariable(groupId, quuboId, objectType);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String groupId, String quuboId, String objectType) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("quuboId", quuboId);
		paramsMap.put("objectType", objectType);
		paramsMap.put("method", "praiseResource");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
