package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 搜索笔记本（不包括未放入笔记本的）
 * 
 * @author datacomo-160
 * 
 */
public class SearchCloudNoteBookListParams extends BasicParams {

	/**
	 * 搜索笔记本（不包括未放入笔记本的）
	 * 
	 * @param context
	 * @param notebookName
	 * @param startRecord
	 * @param maxResults
	 */
	public SearchCloudNoteBookListParams(Context context, String notebookName,
			String startRecord, String maxResults) {
		super(context);
		setVariable(notebookName, startRecord, maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String notebookName, String startRecord,
			String maxResults) {
		paramsMap.put("notebookName", notebookName);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", "false");
		paramsMap.put("method", "searchCloudNoteBookList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
