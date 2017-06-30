package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 搜索我与某个人的分享笔记列表
 * 
 * @author datacomo-160
 * 
 */
public class SearchShareCloudNoteListParams extends BasicParams {

	/**
	 * 搜索我与某个人的分享笔记列表
	 * 
	 * @param context
	 * @param content
	 *            搜索内容，根据笔记标题搜索
	 * @param memberId
	 *            分享笔记联系人
	 * @param allNotes
	 *            是否是所有笔记 true 是，此时查询我与某一好友的所有来往笔记 false
	 *            否，此时根据需要传参，我分享出去的/别人分享给我的
	 * @param shareType
	 *            当allNotes为true时不起作用，该字段传0，我分享给别人的传1，别人分享给我的传2
	 * @param startRecord
	 * @param maxResults
	 */
	public SearchShareCloudNoteListParams(Context context, String memberId,
			String content, String allNotes, String shareType,
			String startRecord, String maxResults) {
		super(context);
		setVariable(memberId, content, allNotes, shareType, startRecord,
				maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String memberId, String content, String allNotes,
			String shareType, String startRecord, String maxResults) {
		paramsMap.put("memberId", memberId);
		paramsMap.put("content", content);
		paramsMap.put("allNotes", allNotes);
		paramsMap.put("shareType", shareType);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", "false");
		paramsMap.put("method", "searchShareCloudNoteList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
