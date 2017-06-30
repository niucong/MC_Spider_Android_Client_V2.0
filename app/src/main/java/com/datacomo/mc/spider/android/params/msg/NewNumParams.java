package com.datacomo.mc.spider.android.params.msg;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;
import com.datacomo.mc.spider.android.util.SoftPhoneInfo;

/**
 * 获取新数量-通知、秘信
 * 
 * @author datacomo-160
 * 
 */
public class NewNumParams extends BasicParams {

	/**
	 * 获取新数量参数设置
	 * 
	 * @param context
	 * @param method
	 * @param temp
	 */
	public NewNumParams(Context context, String method, String temp) {
		super(context);
		setVariable(method);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String method) {
		paramsMap.put("deviceToken", new SoftPhoneInfo(context).getPhoneMark());
		paramsMap.put("method", method);
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
