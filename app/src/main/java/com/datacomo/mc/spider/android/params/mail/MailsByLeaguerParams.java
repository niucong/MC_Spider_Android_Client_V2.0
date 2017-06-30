package com.datacomo.mc.spider.android.params.mail;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取我与某个联系人之间的邮件联系记录
 * 
 * @author datacomo-160
 * 
 */
public class MailsByLeaguerParams extends BasicParams {

	/**
	 * 获取我与某个联系人之间的邮件联系记录参数设置
	 * 
	 * @param context
	 * @param leaguerId
	 * @param isUpdateRead
	 * @param isRead
	 * @param startRecord
	 * @param pageSize
	 */
	public MailsByLeaguerParams(Context context, String leaguerId,
			String isUpdateRead, String isRead, String startRecord,
			String pageSize) {
		super(context);
		setVariable(leaguerId, isUpdateRead, isRead, startRecord, pageSize);
	}

	/**
	 * 设置参数
	 * 
	 * @param leaguerId
	 * @param isUpdateRead
	 * @param isRead
	 * @param startRecord
	 * @param pageSize
	 */
	private void setVariable(String leaguerId, String isUpdateRead,
			String isRead, String startRecord, String pageSize) {
		paramsMap.put("leaguerId", leaguerId);
		paramsMap.put("isUpdateRead", isUpdateRead);
		paramsMap.put("isRead", isRead);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("pageSize", pageSize);
		paramsMap.put("method", "mailsByLeaguer");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
