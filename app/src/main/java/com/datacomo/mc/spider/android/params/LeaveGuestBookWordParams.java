package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 留言
 * 
 * @author datacomo-160
 * 
 */
public class LeaveGuestBookWordParams extends BasicParams {

	/**
	 * 留言参数设置
	 * 
	 * @param context
	 * @param memberId
	 * @param content
	 */
	public LeaveGuestBookWordParams(Context context, String memberId,
			String content) {
		super(context);
		setVariable(memberId, content);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String memberId, String content) {
		paramsMap.put("memberId", memberId);
		paramsMap.put("content", content);
		paramsMap.put("method", "leaveGuestBookWord");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
