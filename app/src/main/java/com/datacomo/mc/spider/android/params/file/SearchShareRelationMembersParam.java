package com.datacomo.mc.spider.android.params.file;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 根据社员名字搜索有文件分享关系的社员列表
 * 
 */
public class SearchShareRelationMembersParam extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param searchContent
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	public SearchShareRelationMembersParam(Context context,
			String searchContent, int startRecord, int maxResult,
			boolean noPaging) {
		super(context);
		setVariable(searchContent, startRecord, maxResult, noPaging);
	}

	/**
	 * 设置参数
	 * 
	 * @param searchContent
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	private void setVariable(String searchContent, int startRecord,
			int maxResult, boolean noPaging) {
		paramsMap.put("searchContent", searchContent);
		paramsMap.put("startRecord", String.valueOf(startRecord));
		paramsMap.put("maxResults", String.valueOf(maxResult));
		paramsMap.put("noPaging", String.valueOf(noPaging));
		paramsMap.put("method", "searchShareRelationMembers");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
