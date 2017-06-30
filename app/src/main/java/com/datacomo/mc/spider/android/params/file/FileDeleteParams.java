package com.datacomo.mc.spider.android.params.file;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 云文件的删除文件
 * 
 * @author datacomo-287
 * 
 */
public class FileDeleteParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param fileId
	 */
	public FileDeleteParams(Context context, int fileId) {
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
		paramsMap.put("method", "deleteFile");
		
		setVariable(true);

	}

	@Override
	public String getParams() {
		return strParams;
	}

}
