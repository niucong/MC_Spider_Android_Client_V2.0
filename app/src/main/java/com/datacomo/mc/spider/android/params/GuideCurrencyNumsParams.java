package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 新用户修改信息赠送圈币数量获取API
 */
public class GuideCurrencyNumsParams extends BasicParams {

	/**
	 * 修改信息赠送圈币数量设置
	 * 
	 * @param context
	 * @param method
	 * @param guideType
	 */
	public GuideCurrencyNumsParams(Context context, String method, String guideType) {
		super(context);
		setVariable(method, guideType);
	}

	/**
	 * 设置参数
	 * 
	 * @param method
	 * @param guideType
	 */
	private void setVariable(String method, String guideType) {

		paramsMap.put("guideType", guideType);
		paramsMap.put("method", method);

		super.setVariable(false);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
