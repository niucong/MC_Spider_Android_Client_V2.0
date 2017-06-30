package com.datacomo.mc.spider.android.params.theme;

import com.datacomo.mc.spider.android.params.BasicParams;

import android.content.Context;

/**
 * 获取某个专题关注/看过/分享 的社员记录列表
 * 
 * @author datacomo-160
 * 
 */
public class ThemeFocusOrBrowseOrShareListParams extends BasicParams {

	/**
	 * 获取某个专题关注/看过/分享 的社员记录列表
	 * 
	 * @param context
	 * @param themeId
	 * @param type
	 *            ：1.关注的人2.看过的人3.分享的人，默认为1
	 * @param startRecord
	 * @param maxResults
	 */
	public ThemeFocusOrBrowseOrShareListParams(Context context, String themeId,
			String type, String startRecord, String maxResults) {
		super(context);
		setVariable(themeId, type, startRecord, maxResults);
	}

	private void setVariable(String themeId, String type, String startRecord,
			String maxResults) {
		paramsMap.put("themeId", themeId);
		paramsMap.put("type", type);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", "false");
		paramsMap.put("method", "themeFocusOrBrowseOrShareList");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
