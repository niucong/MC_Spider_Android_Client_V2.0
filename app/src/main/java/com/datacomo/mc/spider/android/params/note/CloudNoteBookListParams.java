package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取笔记本列表（不包括未放入笔记本的）
 * 
 * @author datacomo-160
 * 
 */
public class CloudNoteBookListParams extends BasicParams {

	/**
	 * 获取笔记本列表（不包括未放入笔记本的）
	 * 
	 * @param context
	 * @param adjunctPath
	 * @param startRecord
	 * @param maxResults
	 */
	public CloudNoteBookListParams(Context context, String adjunctPath,
			String startRecord, String maxResults) {
		super(context);
		setVariable(adjunctPath, startRecord, maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String adjunctPath, String startRecord,
			String maxResults) {
		paramsMap.put("adjunctPath", adjunctPath);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", "true");
		paramsMap.put("method", "getCloudNoteBookList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
