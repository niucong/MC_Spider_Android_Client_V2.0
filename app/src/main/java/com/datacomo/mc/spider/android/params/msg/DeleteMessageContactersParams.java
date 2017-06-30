package com.datacomo.mc.spider.android.params.msg;

import java.util.HashMap;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 删除与某人之间秘信
 * 
 * @author datacomo-160
 * 
 */
public class DeleteMessageContactersParams extends BasicParams {

	/**
	 * 删除通知参数设置
	 * 
	 * @param context
	 * @param contactLeaguerIds
	 */
	public DeleteMessageContactersParams(Context context, String[] contactLeaguerIds) {
		super(context);
		setVariable(contactLeaguerIds);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String[] contactLeaguerIds) {
		mHashMap = new HashMap<String, String[]>();
		mHashMap.put("contactLeaguerIds", contactLeaguerIds);
		paramsMap.put("contactLeaguerIds", "");
		paramsMap.put("method", "deleteContactMembers");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
