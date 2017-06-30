package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 登录
 * 
 * @author datacomo-160
 * 
 */
public class LoginParams extends BasicParams {

	Context context;

	/**
	 * 登录参数设置
	 * 
	 * @param loadName
	 * @param password
	 */
	public LoginParams(Context context, String loadName, String password) {
		super(context);
		this.context = context;
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
		// paramsMap.put("memberPhone", loadName);

		// paramsMap.put("versionName", App.getVersionName());
		// paramsMap.put("phoneMark", PhoneInfo.getPhoneMark());// IMEI
		// paramsMap.put("internetWay", GetNetworkInfo.getNetworkType());// 联网方式
		//
		// paramsMap.put("clientMark", ConstantUtil.CLIENTMARK);
		// paramsMap.put("versionType", ConstantUtil.VERSION_TYPE);

		// paramsMap.put("phoneModel", "HTCDesireS");
		// paramsMap.put("meid", "");
		// paramsMap.put("imsi", "460011301672483");
		// paramsMap.put("deviceNumber", "");

		super.setVariable(false);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
