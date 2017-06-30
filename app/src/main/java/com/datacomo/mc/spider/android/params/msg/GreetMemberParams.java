package com.datacomo.mc.spider.android.params.msg;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 打招呼
 * 
 * @author datacomo-160
 * 
 */
public class GreetMemberParams extends BasicParams {

	/**
	 * 打招呼参数设置
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param maxResults
	 */
	public GreetMemberParams(Context context, String memberId,
			String noticeGreetId, String greetConfigId) {
		super(context);
		setVariable(memberId, noticeGreetId, greetConfigId);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String memberId, String noticeGreetId,
			String greetConfigId) {
		paramsMap.put("memberId", memberId);
		paramsMap.put("noticeGreetId", noticeGreetId);
		paramsMap.put("greetConfigId", greetConfigId);
		paramsMap.put("method", "greetMember");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
