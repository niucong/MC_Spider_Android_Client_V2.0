package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 搜索笔记分享社员列表
 * 
 * @author datacomo-160
 * 
 */
public class SearchShareFriendListParams extends BasicParams {

	/**
	 * 搜索笔记分享社员列表
	 * 
	 * @param context
	 * @param content
	 * @param startRecord
	 * @param maxResults
	 */
	public SearchShareFriendListParams(Context context, String content,
			String startRecord, String maxResults) {
		super(context);
		setVariable(content, startRecord, maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String content, String startRecord,
			String maxResults) {
		paramsMap.put("content", content);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", "false");
		paramsMap.put("method", "searchShareFriendList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
