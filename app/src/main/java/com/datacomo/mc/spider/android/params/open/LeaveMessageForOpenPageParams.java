package com.datacomo.mc.spider.android.params.open;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 给开放主页留言
 * 
 * @author datacomo-160
 * 
 */
public class LeaveMessageForOpenPageParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param openPageId
	 * @param messageContent
	 * @param messageType
	 *            留言类型：1-留言； 2-咨询； 3-投诉； 4-表扬
	 */
	public LeaveMessageForOpenPageParams(Context context, String openPageId,
			String messageContent, String messageType) {
		super(context);
		setVariable(openPageId, messageContent, messageType);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String openPageId, String messageContent,
			String messageType) {
		paramsMap.put("openPageId", openPageId);
		paramsMap.put("messageContent", messageContent);
		paramsMap.put("messageType", messageType);
		paramsMap.put("method", "leaveMessageForOpenPage");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
