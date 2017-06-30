package com.datacomo.mc.spider.android.params.file;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 云文件的详情
 * 
 * @author datacomo-287
 * 
 */
public class FileInfoParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param fileId
	 */
	public FileInfoParams(Context context, int fileId) {
		super(context);
		setVariable(fileId);
	}

	/**
	 * 参数设置
	 * 
	 * @param fileId
	 */
	private void setVariable(int fileId) {
		paramsMap.put("fileId", String.valueOf(fileId));
		paramsMap.put("method", "getFileInfo");
		setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
