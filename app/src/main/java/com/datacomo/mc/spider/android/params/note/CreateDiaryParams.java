package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 创建笔记
 * 
 * @author datacomo-160
 * 
 */
public class CreateDiaryParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param title
	 * @param content
	 */
	public CreateDiaryParams(Context context, String title, String content) {
		super(context);
		setVariable(title, content);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String title, String content) {
		paramsMap.put("title", title);
		paramsMap.put("content", content);
		paramsMap.put("method", "createDiary");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
