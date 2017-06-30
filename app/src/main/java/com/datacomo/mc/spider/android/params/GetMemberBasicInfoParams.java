package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取社员基本信息，可以简单获取显示用户的基本信息包括：头像，昵称，心情用语，性别
 * 
 * @author datacomo-160
 * 
 */
public class GetMemberBasicInfoParams extends BasicParams {

	/**
	 * 登录参数设置
	 * 
	 * @param memberId
	 *            1、“”查自己，2、查朋友填对应memberId
	 */
	public GetMemberBasicInfoParams(Context context, String memberId) {
		super(context);
		setVariable(memberId);
	}

	/**
	 * 设置参数
	 * 
	 * @param loadName
	 * @param password
	 */
	private void setVariable(String memberId) {
		if (memberId == null || "".equals(memberId))
			memberId = "0";
		paramsMap.put("memberId", memberId);
		paramsMap.put("method", "getMemberInfo");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
