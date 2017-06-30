package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 笔记详情
 * 
 * @author datacomo-160
 * 
 */
public class DiaryInfoParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param diaryId
	 */
	public DiaryInfoParams(Context context, String diaryId) {
		super(context);
		setVariable(diaryId);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String diaryId) {
		paramsMap.put("diaryId", diaryId);
		paramsMap.put("method", "getDiaryInfo");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
