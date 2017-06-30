package com.datacomo.mc.spider.android.params;

import android.content.Context;

import com.datacomo.mc.spider.android.util.SoftPhoneInfo;

/**
 * 单点登录登出
 * 
 * @author datacomo-160
 * 
 */
public class SingleInfoParams extends BasicParams {

	/**
	 * 登录参数设置
	 * 
	 * @param loadName
	 * @param password
	 */
	public SingleInfoParams(Context context) {
		super(context);
		setVariable();
	}

	/**
	 * 设置参数
	 */
	private void setVariable() {
		paramsMap.put("deviceToken", new SoftPhoneInfo(context).getPhoneMark());
		paramsMap.put("method", "singleInfo");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
