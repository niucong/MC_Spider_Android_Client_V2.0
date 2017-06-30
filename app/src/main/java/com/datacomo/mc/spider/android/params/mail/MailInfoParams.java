package com.datacomo.mc.spider.android.params.mail;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 邮件详情
 * 
 * @author datacomo-160
 * 
 */
public class MailInfoParams extends BasicParams {

	/**
	 * 邮件详情参数设置
	 * 
	 * @param context
	 * @param memberId
	 * @param startRecord
	 * @param maxResults
	 */
	public MailInfoParams(Context context, String mailId, String recordId) {
		super(context);
		setVariable(mailId, recordId);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String mailId, String recordId) {
		paramsMap.put("mailId", mailId);
		paramsMap.put("recordId", recordId);
		paramsMap.put("method", "mailInfo");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
