package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 添加、取消星标
 * 
 * @author datacomo-160
 * 
 */
public class AddOrDeleteStarMarkParams extends BasicParams {

	/**
	 * 添加、取消星标
	 * 
	 * @param context
	 * @param noteId
	 * @param isAddStarMark
	 *            1 添加星标 2 取消星标
	 */
	public AddOrDeleteStarMarkParams(Context context, String noteId,
			String isAddStarMark) {
		super(context);
		setVariable(noteId, isAddStarMark);
	}

	/**
	 * 设置参数
	 * 
	 * @param noteId
	 * @param isAddStarMark
	 */
	private void setVariable(String noteId, String isAddStarMark) {
		paramsMap.put("noteId", noteId);
		paramsMap.put("isAddStarMark", isAddStarMark);
		paramsMap.put("method", "addOrDeleteStarMark");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
