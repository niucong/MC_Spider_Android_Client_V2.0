package com.datacomo.mc.spider.android.params;

import java.util.HashMap;

import android.content.Context;

/**
 * 评论资源
 * 
 * @author datacomo-160
 * 
 */
public class CommentResourceParams extends BasicParams {

	/**
	 * 评论资源
	 * 
	 * @param context
	 * @param groupId
	 * @param quuboId
	 * @param objectType
	 * @param commentContent
	 * @param receiveReplyIds
	 * @某人（回复）
	 */
	public CommentResourceParams(Context context, String groupId,
			String quuboId, String objectType, String commentContent,
			String[] receiveReplyIds) {
		super(context);
		setVariable(groupId, quuboId, objectType, commentContent,
				receiveReplyIds);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String groupId, String quuboId, String objectType,
			String commentContent, String[] receiveReplyIds) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("quuboId", quuboId);
		paramsMap.put("objectType", objectType);
		paramsMap.put("commentContent", commentContent);

		mHashMap = new HashMap<String, String[]>();
		if (receiveReplyIds != null && receiveReplyIds.length > 0) {
			mHashMap.put("receiveReplyIds", receiveReplyIds);
			paramsMap.put("receiveReplyIds", "");
		}
		paramsMap.put("method", "commentResource");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
