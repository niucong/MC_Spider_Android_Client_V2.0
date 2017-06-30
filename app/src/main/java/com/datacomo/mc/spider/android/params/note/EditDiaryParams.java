package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 编辑笔记
 * 
 * @author datacomo-160
 * 
 */
public class EditDiaryParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param diaryId
	 * @param title
	 * @param content
	 */
	public EditDiaryParams(Context context, String diaryId, String title,
			String content) {
		super(context);
		setVariable(diaryId, title, content);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String diaryId, String title, String content) {
		paramsMap.put("diaryId", diaryId);
		paramsMap.put("title", title);
		paramsMap.put("content", content);
		paramsMap.put("method", "editDiary");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
