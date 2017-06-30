package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 成员被圈个数
 * 
 * @author datacomo-160
 * 
 */
public class FansNumParams extends BasicParams {

	/**
	 * 成员被圈个数参数设置
	 * 
	 * @param context
	 * @param memberId
	 */
	public FansNumParams(Context context, String memberId) {
		super(context);
		setVariable(memberId);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String memberId) {
		paramsMap.put("memberId", memberId);
		paramsMap.put("method", "fansNum");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
