package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 笔记详情
 * 
 * @author datacomo-160
 * 
 */
public class NoteInfoParams extends BasicParams {

	/**
	 * 
	 * @param context
	 * @param noteId
	 */
	public NoteInfoParams(Context context, String noteId) {
		super(context);
		setVariable(noteId);
	}

	/**
	 * 设置参数
	 * 
	 * @param noteId
	 */
	private void setVariable(String noteId) {
		paramsMap.put("noteId", noteId);
		paramsMap.put("method", "getNoteInfo");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
