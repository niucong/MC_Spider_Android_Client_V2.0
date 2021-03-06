package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 搜索云笔记
 * 
 * @author datacomo-160
 * 
 */
public class SearchCloudNoteParams extends BasicParams {

	/**
	 * 搜索云笔记
	 * 
	 * @param context
	 * @param noteType
	 *            1:自己创建的、2:社员分享、3:第三方同步过来的 如 “有道”等、4:所有的（创建+分享+第三方同步）、5:星标笔记
	 * @param isNoteBook
	 *            是否获取某个笔记本下的所有笔记 true：是（获取某个笔记本下的笔记）false：否（获取所有笔记）
	 * @param noteBookId
	 *            笔记本id 如果isNoteBook为true，则笔记本id不能为空，此查询就为查询某个笔记本下的所有笔记
	 * @param noteTitle
	 *            笔记标题  按笔记标题进行模糊搜索
	 * @param startRecord
	 * @param maxResults
	 */
	public SearchCloudNoteParams(Context context, String noteType,
			String isNoteBook, String noteBookId, String noteTitle,
			String startRecord, String maxResults) {
		super(context);
		setVariable(noteType, isNoteBook, noteBookId, noteTitle, startRecord,
				maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String noteType, String isNoteBook,
			String noteBookId, String noteTitle, String startRecord,
			String maxResults) {
		paramsMap.put("orderByType", "EDIT_TIME_DESC");
		paramsMap.put("noteType", noteType);
		paramsMap.put("isNoteBook", isNoteBook);
		paramsMap.put("noteBookId", noteBookId);
		paramsMap.put("noteTitle", noteTitle);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", "false");
		paramsMap.put("method", "searchCloudNote");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
