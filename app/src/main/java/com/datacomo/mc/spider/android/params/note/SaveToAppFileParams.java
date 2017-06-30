package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 转存附件到云文件
 * 
 * @author datacomo-160
 * 
 */
public class SaveToAppFileParams extends BasicParams {

	/**
	 * 转存附件到云文件
	 * 
	 * @param context
	 * @param adjunctPath
	 *            附件path
	 * @param maxResults
	 */
	public SaveToAppFileParams(Context context, String adjunctPath) {
		super(context);
		setVariable(adjunctPath);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String adjunctPath) {
		paramsMap.put("adjunctPath", adjunctPath);
		paramsMap.put("method", "saveToAppFile");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
