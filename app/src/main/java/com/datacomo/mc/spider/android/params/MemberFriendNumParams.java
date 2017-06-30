package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 成员朋友个数
 * 
 * @author datacomo-160
 * 
 */
public class MemberFriendNumParams extends BasicParams {

	/**
	 * 社员朋友个数参数设置
	 * 
	 * @param context
	 * @param memberId
	 */
	public MemberFriendNumParams(Context context, String memberId) {
		super(context);
		setVariable(memberId);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String memberId) {
		paramsMap.put("memberId", memberId);
		paramsMap.put("method", "getFriendNum");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
