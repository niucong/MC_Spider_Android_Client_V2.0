package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 根据邮箱找回密码
 * 
 */
public class RetrievePasswordEmailParams extends BasicParams {

	/**
	 * 根据邮箱找回密码
	 * 
	 * @param context
	 * @param email
	 */
	public RetrievePasswordEmailParams(Context context, String mail) {
		super(context);
		setVariable(mail);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String mail) {
		paramsMap.put("mail", mail);
		paramsMap.put("method", "retrievePasswordEmail");
		super.setVariable(false);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
