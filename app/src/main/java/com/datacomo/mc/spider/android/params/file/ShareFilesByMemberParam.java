package com.datacomo.mc.spider.android.params.file;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 云文件的文件列表
 * 
 * @author datacomo-287
 * 
 */
public class ShareFilesByMemberParam extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	public ShareFilesByMemberParam(Context context, int startRecord,
			int maxResult, boolean noPaging) {
		super(context);
		setVariable(startRecord, maxResult, noPaging);
	}

	/**
	 * 设置参数
	 * 
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	private void setVariable(int startRecord, int maxResult, boolean noPaging) {
		paramsMap.put("startRecord", String.valueOf(startRecord));
		paramsMap.put("maxResults", String.valueOf(maxResult));
		paramsMap.put("noPaging", String.valueOf(noPaging));
		paramsMap.put("method", "ShareFilesByMember");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
