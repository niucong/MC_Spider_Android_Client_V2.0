package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取某个社员 朋友圈列表
 * 
 * @author datacomo-160
 * 
 */
public class GroupListParams extends BasicParams {

	/**
	 * 获取某个社员 朋友圈列表参数设置
	 * 
	 * @param loadName
	 * @param password
	 */
	public GroupListParams(Context context) {
		super(context);
		setVariable();
	}

	/**
	 * 设置参数
	 * 
	 * @param loadName
	 * @param password
	 */
	private void setVariable() {

		paramsMap.put("method", "grouplist");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
