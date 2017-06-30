package com.datacomo.mc.spider.android.params.file;

import android.content.Context;

import com.datacomo.mc.spider.android.enums.FileListTypeEnum;
import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 搜索文件列表
 * 
 * @author datacomo-287
 * 
 */
public class SearchAllFileListParam extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param searchContent
	 * @param fileListType
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	public SearchAllFileListParam(Context context, String searchContent,
			FileListTypeEnum fileListType, int startRecord, int maxResult,
			boolean noPaging) {
		super(context);
		setVariable(searchContent, fileListType, startRecord, maxResult,
				noPaging);
	}

	/**
	 * 设置参数
	 * 
	 * @param searchContent
	 * @param fileListType
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	private void setVariable(String searchContent,
			FileListTypeEnum fileListType, int startRecord, int maxResult,
			boolean noPaging) {
		paramsMap.put("searchContent", searchContent);
		switch (fileListType) {
		case ALL_FILE:
			paramsMap.put("fileListType", "ALL_FILE");
			break;
		case SHARE_FILE:
			paramsMap.put("fileListType", "SHARE_FILE");
			break;
		case RECEIVE_FILE:
			paramsMap.put("fileListType", "RECEIVE_FILE");
			break;
		}
		paramsMap.put("startRecord", String.valueOf(startRecord));
		paramsMap.put("maxResults", String.valueOf(maxResult));
		paramsMap.put("noPaging", String.valueOf(noPaging));
		paramsMap.put("method", "searchAllFileList");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
