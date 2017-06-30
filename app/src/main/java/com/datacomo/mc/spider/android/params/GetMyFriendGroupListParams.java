package com.datacomo.mc.spider.android.params;

import android.content.Context;

public class GetMyFriendGroupListParams extends BasicParams {
	/**
	 * 设置参数
	 * 
	 * @param memberId
	 */
	public GetMyFriendGroupListParams(Context context,int memberId) {
		super(context);
		setVariable(memberId);
	}

	/**
	 * 设置参数
	 * 
	 * @param memberId
	 */
	private void setVariable(int memberId) {
		paramsMap.put("memberId", String.valueOf(memberId));
		paramsMap.put("isPage", "false");	
		paramsMap.put("maxRecord", "0");
		paramsMap.put("method", "getMyFriendGroupList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
