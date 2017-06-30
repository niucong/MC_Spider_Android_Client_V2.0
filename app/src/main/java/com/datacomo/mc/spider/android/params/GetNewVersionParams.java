package com.datacomo.mc.spider.android.params;

import android.content.Context;

import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.SoftPhoneInfo;

/**
 * 获取最新版本Andriod、iphone客户端信息
 * 
 * @author datacomo-160
 * 
 */
public class GetNewVersionParams extends BasicParams {

	/**
	 * 版本升级参数设置
	 */
	public GetNewVersionParams(Context context) {
		super(context);
		setVariable();
	}

	/**
	 * 设置参数
	 * 
	 * @param loadName
	 * @param password
	 */
	private void setVariable() {

		paramsMap.put("currentVersionName",
				new SoftPhoneInfo(context).getVersionName());// 当前版本
		paramsMap.put("versionType", ConstantUtil.VERSION_TYPE);// 手机型号
		paramsMap.put("clientMark", ConstantUtil.ANDROID_COMMON);
//		paramsMap.put("market", "Samsung");

		paramsMap.put("method", "getNewestVersion");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
