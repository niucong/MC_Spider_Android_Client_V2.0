package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取笔记分享者列表
 * 
 * @author datacomo-160
 * 
 */
public class ShareMemberListParams extends BasicParams {

	/**
	 * 获取笔记分享者列表
	 * 
	 * @param context
	 * @param noteId
	 * @param startRecord
	 * @param maxResults
	 */
	public ShareMemberListParams(Context context, String noteId,
			String startRecord, String maxResults) {
		super(context);
		setVariable(noteId, startRecord, maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String noteId, String startRecord,
			String maxResults) {
		paramsMap.put("noteId", noteId);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", "true");
		paramsMap.put("method", "getShareMemberList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
