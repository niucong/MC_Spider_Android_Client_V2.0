package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取笔记列表
 * 
 * @author datacomo-160
 * 
 */
public class DiaryListParams extends BasicParams {

	/**
	 * 笔记列表参数设置
	 * 
	 * @param context
	 * @param diaryType
	 * @param noPaging
	 * @param startRecord
	 * @param maxResults
	 */
	public DiaryListParams(Context context, String diaryType, String noPaging,
			String startRecord, String maxResults) {
		super(context);
		setVariable(diaryType, noPaging, startRecord, maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String diaryType, String noPaging,
			String startRecord, String maxResults) {
		paramsMap.put("diaryType", diaryType);
		paramsMap.put("noPaging", noPaging);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", "getCloudNoteList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
