package com.datacomo.mc.spider.android.params.note;

import java.util.HashMap;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 分享笔记到圈子
 * 
 * @author datacomo-160
 * 
 */
public class ShareDiaryToGroupParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param diaryId
	 * @param groupIds
	 */
	public ShareDiaryToGroupParams(Context context, String diaryId,
			String[] groupIds) {
		super(context);
		setVariable(diaryId, groupIds);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String diaryId, String[] groupIds) {
		paramsMap.put("diaryId", diaryId);

		mHashMap = new HashMap<String, String[]>();
		mHashMap.put("groupIds", groupIds);
		paramsMap.put("groupIds", "");

		paramsMap.put("method", "shareDiaryToGroup");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
