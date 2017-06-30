package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 改名
 * 
 * @author datacomo-160
 * 
 */
public class RenameParams extends BasicParams {

	/**
	 * 改名参数设置
	 * 
	 * @param loadName
	 * @param password
	 */
	public RenameParams(Context context, String name) {
		super(context);
		setVariable(name);
	}

	/**
	 * 设置参数
	 * 
	 * @param loadName
	 * @param password
	 */
	private void setVariable(String name) {

		paramsMap.put("memberName", name);
		paramsMap.put("method", "updateMemberName");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
