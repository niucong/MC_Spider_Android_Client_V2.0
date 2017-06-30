package com.datacomo.mc.spider.android.params.note;

import java.util.HashMap;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 创建云笔记
 * 
 * @author datacomo-160
 * 
 */
public class CreateCloudNoteParams extends BasicParams {

	/**
	 * 创建云笔记
	 * 
	 * @param context
	 * @param oweToNoteBook
	 *            是否属于笔记本
	 * @param noteBookId
	 *            笔记本Id,当oweToNoteBook为false时，该字段为默认笔记本，即所谓的不放入笔记本，
	 *            如果为true时传笔记本的Id
	 * @param title
	 *            笔记标题
	 * @param content
	 *            笔记内容
	 * @param sourceType
	 *            笔记来源 FROM_CREATE 来自创建 FROM_YOUDAO 来自有道云笔记 FROM_EVERNOTE
	 *            来自印象笔记FROM_MKNOTE 来自麦库笔记 FROM_SHARE 来自分享
	 * @param attachments
	 *            上传返回JSON 附件：视频、音频、文件、图片
	 * @param statTarget
	 *            星标记： 1、是星标记 2、不 是星标记
	 * @param updateAddress
	 *            更新地点
	 * @param firstPhotoPath
	 *            第一张图片地址
	 */
	public CreateCloudNoteParams(Context context, String oweToNoteBook,
			String noteBookId, String title, String content, String sourceType,
			String[] attachments, String statTarget, String updateAddress,
			String firstPhotoPath) {
		super(context);
		setVariable(oweToNoteBook, noteBookId, title, content, sourceType,
				attachments, statTarget, updateAddress, firstPhotoPath);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String oweToNoteBook, String noteBookId,
			String title, String content, String sourceType,
			String[] attachments, String statTarget, String updateAddress,
			String firstPhotoPath) {
		paramsMap.put("oweToNoteBook", oweToNoteBook);
		paramsMap.put("noteBookId", noteBookId);
		paramsMap.put("title", title);
		paramsMap.put("content", content);
		paramsMap.put("sourceType", sourceType);
		if (attachments != null && attachments.length > 0) {
			mHashMap = new HashMap<String, String[]>();
			mHashMap.put("attachments", attachments);
			paramsMap.put("attachments", "");
		}

		paramsMap.put("statTarget", statTarget);
		paramsMap.put("updateAddress", updateAddress);
		paramsMap.put("firstPhotoPath", firstPhotoPath);
		paramsMap.put("method", "createCloudNote");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
