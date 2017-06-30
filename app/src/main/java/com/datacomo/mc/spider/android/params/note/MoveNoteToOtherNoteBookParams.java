package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 移动笔记到一个笔记本
 * 
 * @author datacomo-160
 * 
 */
public class MoveNoteToOtherNoteBookParams extends BasicParams {

	/**
	 * 移动笔记到一个笔记本
	 * 
	 * @param context
	 * @param noteId
	 * @param notebookId
	 */
	public MoveNoteToOtherNoteBookParams(Context context, String noteId,
			String notebookId) {
		super(context);
		setVariable(noteId, notebookId);
	}

	/**
	 * 设置参数
	 * 
	 * @param noteId
	 * @param notebookId
	 */
	private void setVariable(String noteId, String notebookId) {
		paramsMap.put("noteId", noteId);
		paramsMap.put("notebookId", notebookId);
		paramsMap.put("method", "moveNoteToOtherNoteBook");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
