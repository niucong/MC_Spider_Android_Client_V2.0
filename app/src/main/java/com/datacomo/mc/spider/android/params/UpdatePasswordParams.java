package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 登录
 * 
 * @author datacomo-160
 *
 */
public class UpdatePasswordParams extends BasicParams {
	
	/**
	 * 社员修改密码参数设置
	 * 
	 * @param loadName
	 * @param password
	 */
	public UpdatePasswordParams(Context context, String oldPassword, String newPassword) {
		super(context);
		setVariable(oldPassword, newPassword);
	}
	
	/**
	 * 设置参数
	 * member.json?method=updatePassword&newPassword=1233&ldPasswrod=111111
	 * 
	 * @param loadName
	 * @param password
	 */
	private void setVariable(String oldPassword, String newPassword) {
		
		paramsMap.put("oldPassword", oldPassword);
		paramsMap.put("newPassword", newPassword);
		paramsMap.put("method", "updatePassword");
		
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
