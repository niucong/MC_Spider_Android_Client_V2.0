package com.datacomo.mc.spider.android.params.file;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取某个文件的分享社员列表
 * 
 */
public class FileShareMembersParam extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param fileId
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	public FileShareMembersParam(Context context, String fileId,
			int startRecord, int maxResult, boolean noPaging) {
		super(context);
		setVariable(fileId, startRecord, maxResult, noPaging);
	}

	/**
	 * 设置参数
	 * 
	 * @param fileId
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	private void setVariable(String fileId, int startRecord, int maxResult,
			boolean noPaging) {
		paramsMap.put("fileId", fileId);
		paramsMap.put("startRecord", String.valueOf(startRecord));
		paramsMap.put("maxResults", String.valueOf(maxResult));
		paramsMap.put("noPaging", String.valueOf(noPaging));
		paramsMap.put("method", "fileShareMembers");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
