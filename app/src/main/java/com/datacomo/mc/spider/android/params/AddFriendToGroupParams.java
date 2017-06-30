package com.datacomo.mc.spider.android.params;

import java.util.HashMap;

import android.content.Context;

/**
 * 添加朋友到朋友圈
 * 
 * @author datacomo-160
 * 
 */
public class AddFriendToGroupParams extends BasicParams {

	/**
	 * 添加朋友到朋友圈
	 * 
	 * @param context
	 * @param friendId
	 * @param groupIds
	 */
	public AddFriendToGroupParams(Context context, String friendId,
			String[] groupIds) {
		super(context);
		setVariable(friendId, groupIds);
	}

	private void setVariable(String friendId, String[] groupIds) {
		mHashMap = new HashMap<String, String[]>();
		mHashMap.put("groupIds", groupIds);
		paramsMap.put("groupIds", "");

		paramsMap.put("friendId", friendId);
		paramsMap.put("method", "addFriendToGroup");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
