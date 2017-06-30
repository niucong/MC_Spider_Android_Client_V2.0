package com.datacomo.mc.spider.android.params.theme;

import com.datacomo.mc.spider.android.params.BasicParams;

import android.content.Context;

/**
 * 关注或取消关注话题
 * 
 * @author datacomo-160
 * 
 */
public class AttentionOrCancelThemeParams extends BasicParams {

	/**
	 * 关注或取消关注话题
	 * 
	 * @param themeId
	 * @param operateType
	 *            操作类型：1.关注话题 2.取消关注话题，默认1
	 */
	public AttentionOrCancelThemeParams(Context context, String themeId,
			String operateType) {
		super(context);
		setVariable(themeId, operateType);
	}

	private void setVariable(String themeId, String operateType) {
		paramsMap.put("themeId", themeId);
		paramsMap.put("operateType", operateType);
		paramsMap.put("method", "attentionOrCancelTheme");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
