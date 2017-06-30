package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 注册验证
 * 
 * @author datacomo-160
 * 
 */
public class RegisterVerifyCodeParams extends BasicParams {

	/**
	 * 注册参数设置
	 * 
	 * @param loadName
	 * @param password
	 */
	public RegisterVerifyCodeParams(Context context, String phone,
			String verifyCode) {
		super(context);
		setVariable(phone, verifyCode);
	}

	/**
	 * 设置参数
	 * 
	 * @param loadName
	 * @param password
	 */
	private void setVariable(String phone, String verifyCode) {

		paramsMap.put("phone", phone);
		paramsMap.put("verifyCode", verifyCode);
		paramsMap.put("method", "verifyCode");

		super.setVariable(false);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
