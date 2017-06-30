package com.datacomo.mc.spider.android.params.file;

import android.content.Context;

import com.datacomo.mc.spider.android.enums.FileListTypeEnum;
import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取我与某人分享的文件列表
 * 
 */
public class ShareFilesByMemberIdParam extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param memberId
	 * @param fileListType
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	public ShareFilesByMemberIdParam(Context context, String memberId,
			FileListTypeEnum fileListType, int startRecord, int maxResult,
			boolean noPaging) {
		super(context);
		setVariable(memberId, fileListType, startRecord, maxResult, noPaging);
	}

	/**
	 * 设置参数
	 * 
	 * @param memberId
	 * @param fileListType
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	private void setVariable(String memberId, FileListTypeEnum fileListType,
			int startRecord, int maxResult, boolean noPaging) {
		paramsMap.put("memberId", memberId);
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
		case RECEIVE_FILE:
			paramsMap.put("fileListType", "RECEIVE_FILE");
			break;
		}
		paramsMap.put("startRecord", String.valueOf(startRecord));
		paramsMap.put("maxResults", String.valueOf(maxResult));
		paramsMap.put("noPaging", String.valueOf(noPaging));
		paramsMap.put("method", "shareFilesByMemberId");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
