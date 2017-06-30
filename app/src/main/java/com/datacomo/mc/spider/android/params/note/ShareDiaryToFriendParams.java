package com.datacomo.mc.spider.android.params.note;

import java.util.HashMap;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 分享笔记给朋友
 * 
 * @author datacomo-160
 * 
 */
public class ShareDiaryToFriendParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param diaryId
	 * @param memberIds
	 * @param shareWord
	 */
	public ShareDiaryToFriendParams(Context context, String diaryId,
			String[] memberIds, String shareWord) {
		super(context);
		setVariable(diaryId, memberIds, shareWord);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String diaryId, String[] memberIds,
			String shareWord) {
		paramsMap.put("noteIds", diaryId);

		mHashMap = new HashMap<String, String[]>();
		mHashMap.put("memberIds", memberIds);
		paramsMap.put("memberIds", "");

		paramsMap.put("shareWord", shareWord);
		paramsMap.put("method", "shareCloudNoteToFriend");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
