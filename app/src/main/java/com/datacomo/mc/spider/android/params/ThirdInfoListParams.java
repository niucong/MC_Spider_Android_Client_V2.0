package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取第三方应用绑定列表
 * 
 * @author datacomo-160
 * 
 */
public class ThirdInfoListParams extends BasicParams {

	/**
	 * 获取第三方应用绑定列表参数设置
	 * 
	 * @param context
	 * @param method
	 */
	public ThirdInfoListParams(Context context, String method) {
		super(context);
		setVariable(method);
	}

	/**
	 * 设置参数
	 * 
	 * @param id
	 * @param method
	 */
	private void setVariable(String method) {
		paramsMap.put("method", method);
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
