package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 编辑笔记本
 * 
 * @author datacomo-160
 * 
 */
public class EditNoteBookParams extends BasicParams {

	/**
	 * 编辑笔记本
	 * 
	 * @param context
	 * @param notebookId
	 * @param noteBookName
	 */
	public EditNoteBookParams(Context context, String notebookId,
			String noteBookName) {
		super(context);
		setVariable(notebookId, noteBookName);
	}

	/**
	 * 设置参数
	 * 
	 * @param notebookId
	 * @param noteBookName
	 */
	private void setVariable(String notebookId, String noteBookName) {
		paramsMap.put("notebookName", noteBookName);
		paramsMap.put("notebookId", notebookId);
		paramsMap.put("method", "editNoteBook");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
