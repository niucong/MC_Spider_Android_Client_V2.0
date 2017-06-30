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
public class UpdateVersionRecordParams extends BasicParams {

	/**
	 * 版本升级参数设置
	 */
	public UpdateVersionRecordParams(Context context) {
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
		SoftPhoneInfo softPhoneInfo = new SoftPhoneInfo(context);

		paramsMap.put("method", "escalate");
		paramsMap.put("phoneMark", softPhoneInfo.getPhoneMark());
		paramsMap.put("clientMark", ConstantUtil.ANDROID_COMMON);
		paramsMap.put("versionType", ConstantUtil.VERSION_TYPE);
		paramsMap.put("versionName", softPhoneInfo.getVersionName());
		paramsMap.put("internetWay", softPhoneInfo.getNetworkType());
		paramsMap.put("phoneModel", softPhoneInfo.getPhoneModel());
		paramsMap.put("meid", softPhoneInfo.getMeid());
		paramsMap.put("imsi", softPhoneInfo.getImsi());
		paramsMap.put("deviceNumber", softPhoneInfo.getDeviceNumber());

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
