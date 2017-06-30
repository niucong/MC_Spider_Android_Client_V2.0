package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 删除笔记
 * 
 * @author datacomo-160
 * 
 */
public class DeleteCloudNoteParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param noteId
	 */
	public DeleteCloudNoteParams(Context context, String noteId) {
		super(context);
		setVariable(noteId);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String noteId) {
		paramsMap.put("noteId", noteId);
		paramsMap.put("method", "deleteCloudNote");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
