package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 个人资源列表
 * 
 * @author datacomo-160
 * 
 */
public class SingleResourceListParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param memberId
	 * @param objectType
	 * @param isGroup
	 *            是否按圈子查看
	 * @param groupId
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	public SingleResourceListParams(Context context, String memberId,
			String objectType, boolean isGroup, String groupId,
			String startRecord, String maxResults, boolean noPaging) {
		super(context);
		setVariable(memberId, objectType, isGroup, groupId, startRecord,
				maxResults, noPaging);
	}

	/**
	 * 设置参数
	 * 
	 * @param memberId
	 * @param objectType
	 * @param isGroup
	 * @param groupId
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	private void setVariable(String memberId, String objectType,
			boolean isGroup, String groupId, String startRecord,
			String maxResults, boolean noPaging) {
		// method=singleResourceList&memberId=0&objectType=OBJ_GROUP_PHOTO&
		// isGroup=false&groupId=0&noPaging=true&startRecord=0&maxResults=10
		if (memberId == null || memberId.equals(""))
			memberId = "0";
		paramsMap.put("memberId", memberId);
		paramsMap.put("objectType", objectType);
		paramsMap.put("isGroup", isGroup + "");
		if (groupId == null || groupId.equals(""))
			groupId = "0";
		paramsMap.put("groupId", groupId);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("method", "singleResourceList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
