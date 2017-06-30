package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 根据注册手机号随即产生验证码（忘记密码修改密码时用）
 * 
 * @author datacomo-160
 * 
 */
public class CreateVerifyCodeByPhoneParams extends BasicParams {

	/**
	 * 根据注册手机号随即产生验证码（忘记密码修改密码时用）
	 * 
	 * @param context
	 * @param phone
	 */
	public CreateVerifyCodeByPhoneParams(Context context, String phone) {
		super(context);
		setVariable(phone);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String phone) {
		paramsMap.put("phone", phone);
		paramsMap.put("method", "createVerifyCodeByPhone");
		super.setVariable(false);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
