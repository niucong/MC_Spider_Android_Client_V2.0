package com.datacomo.mc.spider.android.params.file;

import android.content.Context;

import com.datacomo.mc.spider.android.enums.FileListTypeEnum;
import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 云文件的文件列表
 * 
 * @author datacomo-287
 * 
 */
public class FileListsParam extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param fileListType
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	public FileListsParam(Context context, FileListTypeEnum fileListType,
			int startRecord, int maxResult, boolean noPaging) {
		super(context);
		setVariable(fileListType, startRecord, maxResult, noPaging);
	}

	/**
	 * 设置参数
	 * 
	 * @param fileListType
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	private void setVariable(FileListTypeEnum fileListType, int startRecord,
			int maxResult, boolean noPaging) {
		switch (fileListType) {
		case ALL_FILE:
			paramsMap.put("fileListType", "ALL_FILE");
			break;
		case MY_FILE:
			paramsMap.put("fileListType", "MY_FILE");
			break;
		case SHARE_FILE:
			paramsMap.put("fileListType", "SHARE_FILE");
			break;
		}
		paramsMap.put("startRecord", String.valueOf(startRecord));
		paramsMap.put("maxResults",  String.valueOf(maxResult));
		paramsMap.put("noPaging", String.valueOf(noPaging));
		paramsMap.put("method", "filesList");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
