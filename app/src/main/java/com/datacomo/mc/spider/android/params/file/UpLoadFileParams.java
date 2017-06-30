package com.datacomo.mc.spider.android.params.file;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

public class UpLoadFileParams extends BasicParams {

	public UpLoadFileParams(Context context,String fileName) {
		super(context);
		setVariable(fileName);
	}
	
	/**
	 * 参数设置
	 * 
	 * @param uploadName
	 */
	private void setVariable(String fileName) {
		paramsMap.put("uploadName", fileName);
		paramsMap.put("method", "fileUpload");
		
		setVariable(true);

	}

	@Override
	public String getParams() {
		return strParams;
	}

}
