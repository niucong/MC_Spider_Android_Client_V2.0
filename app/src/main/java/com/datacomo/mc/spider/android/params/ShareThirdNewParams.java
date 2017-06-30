package com.datacomo.mc.spider.android.params;

import java.util.HashMap;

import android.content.Context;

/**
 * 一键分享到第三方
 * 
 * @author datacomo-160
 * 
 */
public class ShareThirdNewParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param content
	 * @param oldContent
	 * @param objectPic
	 * @param thirdIds
	 * @param temp
	 * @param groupId
	 * @param objectId
	 */
	public ShareThirdNewParams(Context context, String content,
			String objectPic, String[] thirdIds, String temp, String groupId,
			String objectId) {
		super(context);
		setVariable(content, objectPic, thirdIds, temp, groupId, objectId);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String content, String objectPic,
			String[] thirdIds, String temp, String groupId, String objectId) {
		mHashMap = new HashMap<String, String[]>();
		mHashMap.put("thirdIds", thirdIds);
		paramsMap.put("thirdIds", "");

		if (content == null || "".equals(content)) {
			content = "优优工作圈网";
		}
		paramsMap.put("content", content);
		if (objectPic != null && !"".equals(objectPic)) {
			paramsMap.put("objectPic", objectPic);
		}
		if (temp == null || "".equals(temp)) {
			temp = "1";
		}
		paramsMap.put("temp", temp);
		paramsMap.put("groupId", groupId);
		paramsMap.put("objectId", objectId);

		paramsMap.put("method", "shareThirdNew");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
