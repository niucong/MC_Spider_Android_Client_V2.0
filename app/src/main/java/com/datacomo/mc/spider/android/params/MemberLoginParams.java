package com.datacomo.mc.spider.android.params;

import android.content.Context;

import com.datacomo.mc.spider.android.util.SoftPhoneInfo;

/**
 * 登录
 * 
 * @author datacomo-160
 * 
 */
public class MemberLoginParams extends BasicParams {

	/**
	 * 登录参数设置
	 * 
	 * @param loadName
	 * @param password
	 */
	public MemberLoginParams(Context context, String loadName, String password) {
		super(context);
		setVariable(loadName, password);
	}

	/**
	 * 设置参数
	 * 
	 * @param loadName
	 * @param password
	 */
	private void setVariable(String loadName, String password) {
		paramsMap.put("u", loadName);
		paramsMap.put("p", password);
		paramsMap.put("deviceToken", new SoftPhoneInfo(context).getPhoneMark());

		paramsMap.put("method", "login");
		super.setVariable(false);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
