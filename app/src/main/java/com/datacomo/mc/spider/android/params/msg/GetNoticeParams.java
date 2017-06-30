package com.datacomo.mc.spider.android.params.msg;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取新通知
 * 
 * @author datacomo-160
 * 
 */
public class GetNoticeParams extends BasicParams {

	/**
	 * 获取新通知参数设置
	 * 
	 * @param context
	 * @param @param groupId
	 * @param fileId
	 */
	public GetNoticeParams(Context context, String timeStamp) {
		super(context);
		setVariable(timeStamp);
	}

	/**
	 * 设置参数
	 * 
	 * @param timeStamp
	 */
	private void setVariable(String timeStamp) {

		paramsMap.put("timeStamp", timeStamp);
		paramsMap.put("method", "getNewNotice");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
