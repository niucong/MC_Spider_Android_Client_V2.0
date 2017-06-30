package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 退出圈子
 * 
 * @author datacomo-160
 * 
 */
public class ExitGroupParams extends BasicParams {

	/**
	 * 退出圈子参数设置
	 * 
	 * @param groupId
	 */
	public ExitGroupParams(Context context, String groupId) {
		super(context);
		setVariable(groupId);
	}

	/**
	 * 设置参数
	 * 
	 * @param exitGroup
	 */
	private void setVariable(String groupId) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("method", "exitGroup");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
