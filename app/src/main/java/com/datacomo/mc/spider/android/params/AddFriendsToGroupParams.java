package com.datacomo.mc.spider.android.params;

import java.util.HashMap;

import android.content.Context;

/**
 * 向指定朋友圈添加朋友
 * 
 * @author datacomo-160
 * 
 */
public class AddFriendsToGroupParams extends BasicParams {

	/**
	 * 向指定朋友圈添加朋友
	 * 
	 * @param context
	 * @param friendIds
	 *            社员编号
	 * @param groupIds
	 *            朋友圈编号
	 * @param isForRecommond
	 *            是否在推荐好友通知中点同意时调用（推荐好友通知中点同意会给推荐人发一条回执通知） true:是；false:否
	 */
	public AddFriendsToGroupParams(Context context, String[] friendIds,
			String[] groupIds, String isForRecommond) {
		super(context);
		setVariable(friendIds, groupIds, isForRecommond);
	}

	private void setVariable(String[] friendIds, String[] groupIds,
			String isForRecommond) {
		mHashMap = new HashMap<String, String[]>();
		mHashMap.put("groupIds", groupIds);
		paramsMap.put("groupIds", "");
		mHashMap.put("friendIds", friendIds);
		paramsMap.put("friendIds", "");
		paramsMap.put("isForRecommond", isForRecommond);
		paramsMap.put("method", "addFriendsToGroup");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
