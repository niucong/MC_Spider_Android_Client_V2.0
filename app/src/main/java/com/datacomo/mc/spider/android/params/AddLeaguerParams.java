package com.datacomo.mc.spider.android.params;

import java.util.HashMap;

import android.content.Context;

/**
 * 圈人（邀请社员加入圈子）
 * 
 * @author datacomo-160
 * 
 */
public class AddLeaguerParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupId
	 * @param friendIds
	 * @param phones
	 */
	public AddLeaguerParams(Context context, String groupId,
			String[] friendIds, String[] phones) {
		super(context);
		setVariable(groupId, friendIds, phones);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String groupId, String[] friendIds, String[] phones) {
		paramsMap.put("groupId", groupId);
		mHashMap = new HashMap<String, String[]>();
		if (friendIds != null && friendIds.length > 0) {
			mHashMap.put("friends", friendIds);
			paramsMap.put("friends", "");
		}

		if (phones != null && phones.length > 0) {
			mHashMap.put("phones", phones);
			paramsMap.put("phones", "");
		}

		paramsMap.put("method", "addLeaguer");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
