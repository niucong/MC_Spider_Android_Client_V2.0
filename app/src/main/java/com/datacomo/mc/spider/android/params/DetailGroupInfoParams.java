package com.datacomo.mc.spider.android.params;

import android.content.Context;

public class DetailGroupInfoParams extends BasicParams {

	/**
	 * 获取圈子信息 包括圈子背景图，圈子信息，圈子设置信息
	 * 
	 * @param groupId
	 */
	public DetailGroupInfoParams(Context context, String groupId) {
		super(context);
		setVariable(groupId);
	}

	private void setVariable(String groupId) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("method", "detailGroupInfo");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
