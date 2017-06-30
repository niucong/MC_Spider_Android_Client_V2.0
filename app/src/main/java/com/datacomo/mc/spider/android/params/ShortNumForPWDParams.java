package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 
 * @author datacomo-160
 * 
 */
public class ShortNumForPWDParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 */
	public ShortNumForPWDParams(Context context) {
		super(context);
		setVariable();
	}

	/**
	 * 设置参数
	 */
	private void setVariable() {
		paramsMap.put("method", "getShortNumForPWD");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
