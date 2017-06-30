package com.datacomo.mc.spider.android.params;

import java.util.HashMap;

import android.content.Context;

/**
 * 创建朋友圈
 * 
 * @author datacomo-160
 * 
 */
public class CreateFriendGroupParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupName
	 * @param friendIds
	 */
	public CreateFriendGroupParams(Context context, String groupName,
			String[] friendIds) {
		super(context);
		setVariable(groupName, friendIds);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String groupName, String[] friendIds) {
		mHashMap = new HashMap<String, String[]>();
		mHashMap.put("friendIds", friendIds);
		paramsMap.put("friendIds", "");
		
		paramsMap.put("groupName", groupName);
		paramsMap.put("method", "createFriendGroup");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
