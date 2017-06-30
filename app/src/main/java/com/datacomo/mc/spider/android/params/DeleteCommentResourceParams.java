package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 删除评论资源
 * 
 * @author datacomo-160
 * 
 */
public class DeleteCommentResourceParams extends BasicParams {

	/**
	 * 删除评论资源
	 * 
	 * @param context
	 * @param groupId
	 * @param objectId
	 * @param commentId
	 * @param objectType
	 */
	public DeleteCommentResourceParams(Context context, String groupId,
			String objectId, String commentId, String objectType) {
		super(context);
		setVariable(groupId, objectId, commentId, objectType);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String groupId, String objectId, String commentId,
			String objectType) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("objectId", objectId);
		paramsMap.put("commentId", commentId);
		paramsMap.put("objectType", objectType);
		paramsMap.put("method", "deleteCommentResource");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
