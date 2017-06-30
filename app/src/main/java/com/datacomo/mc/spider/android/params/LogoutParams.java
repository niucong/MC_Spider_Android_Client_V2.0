package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 退出登录
 * 
 * @author datacomo-160
 * 
 */
public class LogoutParams extends BasicParams {

	/**
	 * 退出参数设置
	 */
	public LogoutParams(Context context) {
		super(context);
		setVariable();
	}

	/**
	 * 设置参数
	 */
	private void setVariable() {
		paramsMap.put("method", "logout");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
