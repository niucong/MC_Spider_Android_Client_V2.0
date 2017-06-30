package com.datacomo.mc.spider.android.params;

import java.util.HashMap;

import android.content.Context;

/**
 * 推荐朋友圈
 * 
 * @author datacomo-160
 * 
 */
public class RecommendFriendParams extends BasicParams {

	/**
	 * 推荐朋友圈
	 * 
	 * @param context
	 * @param friendIds
	 *            推荐好友编号
	 * @param groupIds
	 *            推荐朋友圈编号
	 * @param isContainFans
	 *            是否推荐给圈我的人，默认false
	 * @param isContainVisiter
	 *            是否添加自己，默认false
	 * @param receiveFriendIds
	 *            接受者编号
	 * @param receiveGroupIds
	 *            接收朋友圈编号
	 * @param sendSms
	 *            是否发送短信，默认false
	 * @param postscript
	 *            附言
	 * 
	 *            响应信息： 0:系统异常1：成功 2：参数不合法
	 */
	public RecommendFriendParams(Context context, String[] friendIds,
			String[] groupIds, String isContainFans, String isContainVisiter,
			String[] receiveFriendIds, String[] receiveGroupIds,
			String sendSms, String postscript) {
		super(context);
		setVariable(friendIds, groupIds, isContainFans, isContainVisiter,
				receiveFriendIds, receiveGroupIds, sendSms, postscript);
	}

	private void setVariable(String[] friendIds, String[] groupIds,
			String isContainFans, String isContainVisiter,
			String[] receiveFriendIds, String[] receiveGroupIds,
			String sendSms, String postscript) {
		mHashMap = new HashMap<String, String[]>();
		if (friendIds != null && friendIds.length > 0) {
			mHashMap.put("friendIds", friendIds);
			paramsMap.put("friendIds", "");
		}
		if (groupIds != null && groupIds.length > 0) {
			mHashMap.put("groupIds", groupIds);
			paramsMap.put("groupIds", "");
		}
		paramsMap.put("isContainFans", isContainFans);
		paramsMap.put("isContainVisiter", isContainVisiter);
		if (receiveFriendIds != null && receiveFriendIds.length > 0) {
			mHashMap.put("receiveFriendIds", receiveFriendIds);
			paramsMap.put("receiveFriendIds", "");
		}
		if (receiveGroupIds != null && receiveGroupIds.length > 0) {
			mHashMap.put("receiveGroupIds", receiveGroupIds);
			paramsMap.put("receiveGroupIds", "");
		}
		paramsMap.put("sendSms", sendSms);
		paramsMap.put("postscript", postscript);
		paramsMap.put("method", "recommendFriend");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
