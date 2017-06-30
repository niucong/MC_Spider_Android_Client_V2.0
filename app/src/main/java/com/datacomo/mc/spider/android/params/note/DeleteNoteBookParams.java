package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 删除笔记本
 * 
 * @author datacomo-160
 * 
 */
public class DeleteNoteBookParams extends BasicParams {

	/**
	 * 删除笔记本
	 * 
	 * @param context
	 * @param notebookId
	 */
	public DeleteNoteBookParams(Context context, String notebookId) {
		super(context);
		setVariable(notebookId);
	}

	/**
	 * 设置参数
	 * 
	 * @param notebookId
	 */
	private void setVariable(String notebookId) {
		paramsMap.put("notebookId", notebookId);
		paramsMap.put("method", "deleteNoteBook");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
