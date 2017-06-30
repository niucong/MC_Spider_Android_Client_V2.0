package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 从某个朋友圈移除朋友
 * 
 * @author datacomo-160
 * 
 */
public class RemoveFriendFromGroupParams extends BasicParams {

	/**
	 * 从某个朋友圈移除朋友参数设置
	 * 
	 * @param context
	 * @param memberId
	 * @param groupId
	 *            0为解除朋友关系
	 */
	public RemoveFriendFromGroupParams(Context context, String memberId,
			String groupId) {
		super(context);
		setVariable(memberId, groupId);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String memberId, String groupId) {
		paramsMap.put("memberId", memberId);
		if (groupId != null && !"0".equals(groupId))
			paramsMap.put("groupId", groupId);
		paramsMap.put("method", "removeFriendFromGroup");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
