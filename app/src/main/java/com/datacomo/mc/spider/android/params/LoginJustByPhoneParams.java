package com.datacomo.mc.spider.android.params;

import android.content.Context;

import com.datacomo.mc.spider.android.util.SoftPhoneInfo;

/**
 * 手机号登录(不需要输入密码)
 * 
 * @author datacomo-160
 * 
 */
public class LoginJustByPhoneParams extends BasicParams {

	/**
	 * 手机号登录(不需要输入密码)
	 * 
	 * @param context
	 * @param phone
	 * @param verifyCode
	 */
	public LoginJustByPhoneParams(Context context, String phone,
			String verifyCode) {
		super(context);
		setVariable(phone, verifyCode);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String phone, String verifyCode) {
		paramsMap.put("phone", phone);
		paramsMap.put("verifyCode", verifyCode);
		paramsMap.put("deviceToken", new SoftPhoneInfo(context).getPhoneMark());
		paramsMap.put("method", "loginJustByPhone");
		super.setVariable(false);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
