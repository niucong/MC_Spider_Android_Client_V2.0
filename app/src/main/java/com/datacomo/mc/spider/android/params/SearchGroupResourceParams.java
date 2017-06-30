package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 搜索圈子：成员 groupSearchMember、话题searchTopics、照片searchFiles、文件searchFiles
 */
public class SearchGroupResourceParams extends BasicParams {

	/**
	 * 搜索圈子：成员 groupSearchMember、话题searchTopics、照片searchPhotos、文件searchFiles
	 * 
	 * @param context
	 * @param method
	 * @param groupId
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	public SearchGroupResourceParams(Context context, String method,
			String groupId, String searchContent, String startRecord,
			String maxResults, String noPaging) {
		super(context);
		setVariable(method, groupId, searchContent, startRecord, maxResults,
				noPaging);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String method, String groupId,
			String searchContent, String startRecord, String maxResults,
			String noPaging) {
		paramsMap.put("method", method);
		paramsMap.put("groupId", groupId);
		paramsMap.put("searchContent", searchContent);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", noPaging);

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
