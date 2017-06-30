package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 搜索笔记
 * 
 * @author datacomo-160
 * 
 */
public class SearchDiaryParams extends BasicParams {

	/**
	 * 搜索笔记参数设置
	 * 
	 * @param context
	 * @param text
	 * @param noPaging
	 * @param startRecord
	 * @param maxResults
	 */
	public SearchDiaryParams(Context context, String text, String noPaging,
			String startRecord, String maxResults) {
		super(context);
		setVariable(text, noPaging, startRecord, maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String text, String noPaging, String startRecord,
			String maxResults) {
		paramsMap.put("text", text);
		paramsMap.put("noPaging", noPaging);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", "searchDiary");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
