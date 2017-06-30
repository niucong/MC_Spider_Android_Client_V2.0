package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 忘记重置密码
 * 
 * @author datacomo-160
 * 
 */
public class ResetPasswordParams extends BasicParams {

	/**
	 * 忘记重置密码
	 * 
	 * @param context
	 * @param phone
	 * @param newPassword
	 */
	public ResetPasswordParams(Context context, String phone, String newPassword) {
		super(context);
		setVariable(phone, newPassword);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String phone, String newPassword) {
		paramsMap.put("phone", phone);
		paramsMap.put("newPassword", newPassword);
		paramsMap.put("method", "resetPassword");
		super.setVariable(false);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
