package com.datacomo.mc.spider.android.params.mail;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取我的邮件联系人
 * 
 * @author datacomo-160
 * 
 */
public class ContactLeaguersParams extends BasicParams {

	/**
	 * 获取我的邮件联系人参数设置
	 * 
	 * @param context
	 * @param startRecord
	 * @param pageSize
	 */
	public ContactLeaguersParams(Context context, String startRecord,
			String pageSize) {
		super(context);
		setVariable(startRecord, pageSize);
	}

	/**
	 * 设置参数
	 * 
	 * @param startRecord
	 * @param pageSize
	 */
	private void setVariable(String startRecord, String pageSize) {
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("pageSize", pageSize);
		paramsMap.put("method", "contactLeaguers");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
