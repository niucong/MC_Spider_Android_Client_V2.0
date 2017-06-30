package com.datacomo.mc.spider.android.params.mail;

import java.util.HashMap;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 邮件删除到回收站
 * 
 * @author datacomo-160
 * 
 */
public class MailDeleteToRecycleParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param recordIds
	 */
	public MailDeleteToRecycleParams(Context context, String[] recordIds) {
		super(context);
		setVariable(recordIds);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String[] recordIds) {
		mHashMap = new HashMap<String, String[]>();
		mHashMap.put("recordIds", recordIds);
		paramsMap.put("recordIds", "");
		paramsMap.put("method", "deleteSomeMailToMidden");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
