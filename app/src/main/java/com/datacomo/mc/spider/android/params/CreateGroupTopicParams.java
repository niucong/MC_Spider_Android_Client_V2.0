package com.datacomo.mc.spider.android.params;

import java.util.HashMap;

import android.content.Context;

/**
 * 创建圈子话题-快速发布
 * 
 * @author datacomo-160
 * 
 */
public class CreateGroupTopicParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupIds
	 * @param fileTemps
	 * @param photoTemps
	 * @param fileIds
	 * @param topicTitle
	 * @param content
	 * @param tags
	 */
	public CreateGroupTopicParams(Context context, String[] groupIds,
			String[] fileTemps, String[] photoTemps, String[] fileIds,
			String topicTitle, String content, String tags, String[] fileInfos,
			String[] photoInfos) {
		super(context);
		setVariable(groupIds, fileTemps, photoTemps, fileIds, topicTitle,
				content, tags, fileInfos, photoInfos);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String[] groupIds, String[] fileTemps,
			String[] photoTemps, String[] fileIds, String topicTitle,
			String content, String tags, String[] fileInfos, String[] photoInfos) {
		mHashMap = new HashMap<String, String[]>();
		mHashMap.put("groupIds", groupIds);
		paramsMap.put("groupIds", "");

		if (fileTemps != null && fileTemps.length > 0) {
			mHashMap.put("fileTemps", fileTemps);
			paramsMap.put("fileTemps", "");
		}

		if (photoTemps != null && photoTemps.length > 0) {
			mHashMap.put("photoTemps", photoTemps);
			paramsMap.put("photoTemps", "");
		}

		if (fileIds != null && fileIds.length > 0) {
			mHashMap.put("fileIds", fileIds);
			paramsMap.put("fileIds", "");
		}
		if (fileInfos != null && fileInfos.length > 0) {
			mHashMap.put("fileInfos", fileInfos);
			paramsMap.put("fileInfos", "");
		}

		if (photoInfos != null && photoInfos.length > 0) {
			mHashMap.put("photoInfos", photoInfos);
			paramsMap.put("photoInfos", "");
		}

		if (topicTitle != null && !"".equals(topicTitle)) {
			paramsMap.put("topicTitle", topicTitle);
		}
		paramsMap.put("content", content);
		if (tags != null && !"".equals(tags)) {
			paramsMap.put("tags", tags);
		}
		paramsMap.put("method", "quickCreateTopic");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
