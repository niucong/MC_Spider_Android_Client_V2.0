package com.datacomo.mc.spider.android.params.groupchat;

import java.util.HashMap;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 发送消息
 */
public class SendGroupMessageParams extends BasicParams {

	/**
	 * 发送消息参数设置
	 * 
	 * @param context
	 * @param groupIds
	 * @param isPlainText
	 * @param content
	 * @param objectInfos
	 * @param memberName
	 * @param chatName
	 * @param type
	 * @param uploadName
	 * @param l
	 * @param uri
	 * @param path
	 */
	public SendGroupMessageParams(Context context, String[] groupIds,
			boolean isPlainText, String content, String[] objectInfos,
			String memberName, String chatName, String type, String uploadName,
			String l, String uri, String path) {
		super(context);
		setVariable(groupIds, isPlainText, content, objectInfos, memberName,
				chatName, type, uploadName, l, uri, path);
	}

	/**
	 * 参数设置
	 */
	private void setVariable(String[] groupIds, boolean isPlainText,
			String content, String[] objectInfos, String memberName,
			String chatName, String type, String uploadName, String l,
			String uri, String path) {
		// paramsMap.put("groupId", String.valueOf(groupId));
		mHashMap = new HashMap<String, String[]>();
		mHashMap.put("groupIds", groupIds);
		paramsMap.put("groupIds", "");

		paramsMap.put("isPlainText", String.valueOf(isPlainText));
		if (isPlainText) {
			paramsMap.put("content", content);
		} else {
			if (objectInfos != null && objectInfos.length > 0) {
				mHashMap = new HashMap<String, String[]>();
				mHashMap.put("objectInfos", objectInfos);
				paramsMap.put("objectInfos", "");
			}
		}
		paramsMap.put("memberName", memberName);
		paramsMap.put("chatName", chatName);

		if (uri != null && !"".equals(uri))
			paramsMap.put("uri", uri);
		if (path != null && !"".equals(path))
			paramsMap.put("path", path);

		if (uploadName != null && !"".equals(uploadName))
			paramsMap.put("uploadName", uploadName);
		if (l != null && !"".equals(l))
			paramsMap.put("l", l);
		paramsMap.put("type", type);

		paramsMap.put("method", "translateGroupChatMessage");
		// paramsMap.put("method", "sendGroupMessageOfNew");
		// paramsMap.put("method", "sendGroupMessage");
		setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}