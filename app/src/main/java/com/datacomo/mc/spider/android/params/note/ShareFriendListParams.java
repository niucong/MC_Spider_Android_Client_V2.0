package com.datacomo.mc.spider.android.params.note;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取笔记分享社员列表
 * 
 * @author datacomo-160
 * 
 */
public class ShareFriendListParams extends BasicParams {

	/**
	 * 获取笔记本列表（不包括未放入笔记本的）
	 * 
	 * @param context
	 * @param startRecord
	 * @param maxResults
	 */
	public ShareFriendListParams(Context context, String startRecord,
			String maxResults) {
		super(context);
		setVariable(startRecord, maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String startRecord, String maxResults) {
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", "true");
		paramsMap.put("method", "getShareFriendList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
