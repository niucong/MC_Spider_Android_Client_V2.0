package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取最新动态的数量
 * 
 * @author datacomo-160
 * 
 */
public class GroupResourceTrendsOfNewNumParams extends BasicParams {

	/**
	 * 获取最新动态的数量
	 * 
	 * @param context
	 * @param memberId
	 *            某个人的Id:isCertainMemberId为false时不起作用
	 * @param isOnlyActionMember
	 *            此人是否为发起者
	 * @param groupId
	 *            某些特定圈子Id:isCertainGroup为false时不起作用
	 * @param isCertainGroup
	 *            是否查看某些圈子的动态
	 * @param objectType
	 *            对象类型:isFilterByType为 false时不起作用
	 * @param isFilterByType
	 *            是否按类型查看 是：true 否：false
	 * @param lastRefershTime
	 *            上一次刷新的时间
	 */
	public GroupResourceTrendsOfNewNumParams(Context context, String memberId,
			boolean isOnlyActionMember, String groupId, boolean isCertainGroup,
			String objectType, boolean isFilterByType, long lastRefershTime) {
		super(context);
		setVariable(memberId, isOnlyActionMember, groupId, isCertainGroup,
				objectType, isFilterByType, lastRefershTime);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String memberId, boolean isOnlyActionMember,
			String groupId, boolean isCertainGroup, String objectType,
			boolean isFilterByType, long lastRefershTime) {
		paramsMap.put("memberId", memberId);
		paramsMap.put("isOnlyActionMember", isOnlyActionMember + "");
		paramsMap.put("groupId", groupId);
		paramsMap.put("isCertainGroup", isCertainGroup + "");
		paramsMap.put("objectType", objectType);
		paramsMap.put("isFilterByType", isFilterByType + "");
		paramsMap.put("lastRefershTime", lastRefershTime + "");
		paramsMap.put("method", "groupResourceTrendsOfNewNum");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
