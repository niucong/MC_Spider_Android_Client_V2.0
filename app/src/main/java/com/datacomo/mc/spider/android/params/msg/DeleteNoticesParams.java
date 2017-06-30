package com.datacomo.mc.spider.android.params.msg;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 删除通知
 * 
 * @author datacomo-160
 * 
 */
public class DeleteNoticesParams extends BasicParams {

	/**
	 * 删除通知参数设置
	 * 
	 * @param context
	 * @param noticeId
	 */
	public DeleteNoticesParams(Context context, String noticeId) {
		super(context);
		setVariable(noticeId);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String noticeId) {
		paramsMap.put("notices", noticeId);
		paramsMap.put("method", "deleteNotices");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
