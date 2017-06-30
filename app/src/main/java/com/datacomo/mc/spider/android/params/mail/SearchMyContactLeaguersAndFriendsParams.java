package com.datacomo.mc.spider.android.params.mail;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 搜索我的邮件联系人
 * 
 * @author datacomo-160
 * 
 */
public class SearchMyContactLeaguersAndFriendsParams extends BasicParams {

	/**
	 * 搜索我的邮件联系人参数设置
	 * 
	 * @param context
	 * @param content
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	public SearchMyContactLeaguersAndFriendsParams(Context context, String content,
			String startRecord, String maxResult, String noPaging) {
		super(context);
		setVariable(content, startRecord, maxResult, noPaging);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String content, String startRecord,
			String maxResult, String noPaging) {
		paramsMap.put("content", content);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResult", maxResult);
		paramsMap.put("noPaging", noPaging);
		paramsMap.put("method", "SEARCHMYCONTACTLEAGUERSANDFRIENDS");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
