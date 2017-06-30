package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 按某个备份时间点获取/删除通讯录
 * 
 * @author datacomo-160
 * 
 */
public class GetContactsByPointIdParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param method
	 * @param pointId
	 */
	public GetContactsByPointIdParams(Context context, String method,
			String pointId) {
		super(context);
		setVariable(method, pointId);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String method, String pointId) {

		paramsMap.put("pointId", pointId);
		paramsMap.put("method", method);
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
