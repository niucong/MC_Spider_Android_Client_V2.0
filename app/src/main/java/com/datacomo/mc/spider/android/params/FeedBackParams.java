package com.datacomo.mc.spider.android.params;

import android.content.Context;

import com.datacomo.mc.spider.android.util.SoftPhoneInfo;

/**
 * 创建圈子话题-快速发布
 * 
 * @author datacomo-160
 * 
 */
public class FeedBackParams extends BasicParams {

	private SoftPhoneInfo phoneInfo;

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param pointId
	 */
	public FeedBackParams(Context context, String feedbackContent) {
		super(context);
		phoneInfo = new SoftPhoneInfo(context);
		setVariable(feedbackContent);
	}

	/**
	 * 设置参数
	 * member.json?method=feedback&deviceName=设备名字&imei=设备标识&feedbackContent
	 * =反馈内容（不能为空）
	 */
	private void setVariable(String feedbackContent) {
		paramsMap.put("feedbackContent", feedbackContent);
		paramsMap.put("deviceName", phoneInfo.getPhoneModel());
		paramsMap.put("imei", phoneInfo.getPhoneMark());
		paramsMap.put("phoneSysVersion", phoneInfo.getAndroidVersion());
		paramsMap.put("clientVersion", phoneInfo.getVersionName());
		paramsMap.put("method", "feedback");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
