package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 创建笔记本
 * 
 * @author datacomo-160
 * 
 */
public class CreateCloudNoteBookParams extends BasicParams {

	/**
	 * 创建笔记本
	 * 
	 * @param context
	 * @param noteBookName
	 * @param sourceType
	 *            笔记来源 FROM_CREATE 来自创建 FROM_YOUDAO 来自有道云笔记 FROM_EVERNOTE
	 *            来自印象笔记FROM_MKNOTE 来自麦库笔记 FROM_SHARE 来自分享
	 */
	public CreateCloudNoteBookParams(Context context, String noteBookName,
			String sourceType) {
		super(context);
		setVariable(noteBookName, sourceType);
	}

	/**
	 * 设置参数
	 * 
	 * @param noteBookName
	 * @param sourceType
	 */
	private void setVariable(String noteBookName, String sourceType) {
		paramsMap.put("noteBookName", noteBookName);
		paramsMap.put("sourceType", sourceType);
		paramsMap.put("method", "createCloudNoteBook");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
