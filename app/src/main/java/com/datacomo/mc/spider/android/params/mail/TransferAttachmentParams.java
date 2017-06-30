package com.datacomo.mc.spider.android.params.mail;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 转存附件到个人云文件
 * 
 * @author datacomo-160
 * 
 */
public class TransferAttachmentParams extends BasicParams {

	/**
	 * 转存附件到个人云文件
	 * 
	 * @param context
	 * @param filePath
	 * @param fileName
	 * @param fileSize
	 */
	public TransferAttachmentParams(Context context, String filePath,
			String fileName, String fileSize) {
		super(context);
		setVariable(filePath, fileName, fileSize);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String filePath, String fileName, String fileSize) {
		paramsMap.put("filePath", filePath);
		paramsMap.put("fileName", fileName);
		paramsMap.put("fileSize", fileSize);
		paramsMap.put("method", "transferAttachment");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
