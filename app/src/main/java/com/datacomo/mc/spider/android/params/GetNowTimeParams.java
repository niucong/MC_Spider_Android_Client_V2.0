package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获得服务器当前时间
 * 
 * @author datacomo-160
 * 
 */
public class GetNowTimeParams extends BasicParams {

	/**
	 * 获得服务器当前时间
	 * 
	 * @param context
	 */
	public GetNowTimeParams(Context context) {
		super(context);
		setVariable();
	}

	/**
	 * 添加朋友到朋友圈
	 */
	private void setVariable() {
		paramsMap.put("method", "getNowTime");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
