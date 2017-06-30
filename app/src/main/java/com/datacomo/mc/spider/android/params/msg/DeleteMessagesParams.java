package com.datacomo.mc.spider.android.params.msg;

import java.util.HashMap;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 删除私信
 * 
 * @author datacomo-160
 * 
 */
public class DeleteMessagesParams extends BasicParams {

	/**
	 * 删除通知参数设置
	 * 
	 * @param context
	 * @param noticeId
	 */
	public DeleteMessagesParams(Context context, String[] messageIds) {
		super(context);
		setVariable(messageIds);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String[] messageIds) {
		mHashMap = new HashMap<String, String[]>();
		mHashMap.put("messageIds", messageIds);
		paramsMap.put("messageIds", "");

		paramsMap.put("method", "deleteMessages");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
