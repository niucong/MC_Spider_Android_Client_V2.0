package com.datacomo.mc.spider.android.params.file;

import java.util.HashMap;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 云文件的编辑文件
 * 
 * @author datacomo-287
 * 
 */
public class FileEditParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param fileId
	 * @param fileName
	 * @param fileDesc
	 * @param tags
	 */
	public FileEditParams(Context context, int fileId, String fileName,
			String fileDesc, String[] tags) {
		super(context);
		setVariable(fileId, fileName, fileDesc, tags);
	}

	/**
	 * 参数设置
	 * 
	 * @param fileId
	 * @param fileName
	 * @param fileDesc
	 * @param tags
	 */
	private void setVariable(int fileId, String fileName, String fileDesc,
			String[] tags) {
		paramsMap.put("fileId", String.valueOf(fileId));
		paramsMap.put("fileName", fileName);
		if (null != fileDesc && !"".equals(fileDesc)) {
			paramsMap.put("fileDesc", fileDesc);
		}
		if (tags != null && tags.length > 0) {
			mHashMap = new HashMap<String, String[]>();
			mHashMap.put("tags", tags);
			paramsMap.put("tags", "");
		}
		paramsMap.put("method", "editFile");

		setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
