package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 上传图片
 * 
 * @author datacomo-160
 * 
 */
public class UploadAttachmentImgeParams extends BasicParams {

	/**
	 * 上传图片
	 * 
	 * @param context
	 * @param upload
	 *            文件file
	 * @param uploadName
	 *            文件名
	 */
	public UploadAttachmentImgeParams(Context context, String upload,
			String uploadName) {
		super(context);
		setVariable(upload, uploadName);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String upload, String uploadName) {
		paramsMap.put("upload", upload);
		paramsMap.put("uploadName", uploadName);
		paramsMap.put("method", "uploadAttachmentImge");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
