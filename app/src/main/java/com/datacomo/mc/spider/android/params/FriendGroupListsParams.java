package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获得朋友圈列表
 * 
 * @author datacomo-160
 * 
 */
public class FriendGroupListsParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param pointId
	 */
	public FriendGroupListsParams(Context context) {
		super(context);
		setVariable();
	}

	/**
	 * 设置参数
	 */
	private void setVariable() {
		paramsMap.put("method", "groupList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
