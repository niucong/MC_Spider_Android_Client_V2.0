package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 注册
 * 
 * @author datacomo-160
 * 
 */
public class RegisterParams extends BasicParams {

	/**
	 * 注册参数设置
	 * 
	 * @param loadName
	 * @param password
	 */
	public RegisterParams(Context context, String email, String password,
			String phone) {
		super(context);
		setVariable(email, password, phone);
	}

	/**
	 * 设置参数
	 * 
	 * @param loadName
	 * @param password
	 */
	private void setVariable(String email, String password, String phone) {

		paramsMap.put("email", email);
		paramsMap.put("password", password);
		paramsMap.put("phone", phone);
		paramsMap.put("method", "registerBefore");

		super.setVariable(false);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
