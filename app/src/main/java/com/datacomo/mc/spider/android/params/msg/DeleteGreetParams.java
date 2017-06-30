package com.datacomo.mc.spider.android.params.msg;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 删除招呼
 * 
 * @author datacomo-160
 * 
 */
public class DeleteGreetParams extends BasicParams {

	/**
	 * 删除通知参数设置
	 * 
	 * @param context
	 * @param noticeGreetId
	 */
	public DeleteGreetParams(Context context, String noticeGreetId) {
		super(context);
		setVariable(noticeGreetId);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String noticeGreetId) {
		paramsMap.put("noticeGreetId", noticeGreetId);
		paramsMap.put("method", "deleteGreetInfo");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
