package com.datacomo.mc.spider.android.params.msg;

import java.util.HashMap;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

public class SendMessageParams extends BasicParams {

	/**
	 * 发消息
	 * 
	 * @param context
	 * @param friendMemberIds
	 *            可选-接受者的用户的ID
	 * @param friendGroupIds
	 *            可选-朋友分组
	 * @param phones
	 *            可选-接收者的手机号码 接受者如果需要输入名字可以用分隔符分开，例如：13810116246#MC#名字
	 * @param emails
	 *            可选-接收者的email
	 * @param messageContent
	 * @param isAdd
	 *            群发对象是否加为朋友默认为true
	 * @param isPlainText
	 * @param type
	 * @param l
	 * @param uploadName
	 */
	public SendMessageParams(Context context, String[] friendMemberIds,
			String[] friendGroupIds, String[] phones, String[] emails,
			String messageContent, boolean isAdd, String isPlainText,
			String type, String l, String uploadName, String uri, String path) {
		super(context);
		setVariable(friendMemberIds, friendGroupIds, phones, emails,
				messageContent, isAdd, isPlainText, type, l, uploadName, uri,
				path);
	}

	/**
	 * 设置参数
	 * 
	 * @param friendMemberIds
	 * @param friendGroupIds
	 * @param phones
	 * @param emails
	 * @param messageContent
	 * @param isAdd
	 * @param isPlainText
	 * @param type
	 * @param l
	 * @param uploadName
	 */
	private void setVariable(String[] friendMemberIds, String[] friendGroupIds,
			String[] phones, String[] emails, String messageContent,
			boolean isAdd, String isPlainText, String type, String l,
			String uploadName, String uri, String path) {
		if (messageContent != null && !messageContent.equals(""))
			paramsMap.put("content", messageContent);
		paramsMap.put("isPlainText", isPlainText);
		paramsMap.put("type", type);// OBJ_TEXT、OBJ_VOICE、OBJ_PHOTO
		if (uri != null && !uri.equals(""))
			paramsMap.put("uri", uri);
		if (path != null && !path.equals(""))
			paramsMap.put("path", path);
		if (l != null && !l.equals(""))
			paramsMap.put("l", l);
		if (uploadName != null && !uploadName.equals(""))
			paramsMap.put("uploadName", uploadName);

		// paramsMap.put("method", "sendMessage");
		// paramsMap.put("messageContent", messageContent);
		// paramsMap.put("isAdd", isAdd + "");
		mHashMap = new HashMap<String, String[]>();
		if (friendMemberIds != null && friendMemberIds.length > 0) {
			if (friendMemberIds.length > 1) {
				paramsMap.put("method", "sendMessageOfNew");
				mHashMap.put("friendMemberIds", friendMemberIds);
				paramsMap.put("friendMemberIds", "");
			} else {
				paramsMap.put("method", "sendMessageToMemberOfNew");
				mHashMap.put("memberId", friendMemberIds);
				paramsMap.put("memberId", "");
			}
		}

		if (friendGroupIds != null && friendGroupIds.length > 0) {
			mHashMap.put("friendGroupIds", friendGroupIds);
			paramsMap.put("friendGroupIds", "");
		}
		if (phones != null && phones.length > 0) {
			mHashMap.put("phones", phones);
			paramsMap.put("phones", "");
		}
		if (emails != null && emails.length > 0) {
			mHashMap.put("emails", emails);
			paramsMap.put("emails", "");
		}
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
