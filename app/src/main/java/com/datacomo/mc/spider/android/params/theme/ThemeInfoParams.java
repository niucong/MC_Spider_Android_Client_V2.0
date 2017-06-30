package com.datacomo.mc.spider.android.params.theme;

import com.datacomo.mc.spider.android.params.BasicParams;

import android.content.Context;

/**
 * 获取专题详情
 * 
 * @author datacomo-160
 * 
 */
public class ThemeInfoParams extends BasicParams {

	/**
	 * 获取专题详情
	 * 
	 * @param themeId
	 */
	public ThemeInfoParams(Context context, String themeId) {
		super(context);
		setVariable(themeId);
	}

	private void setVariable(String themeId) {
		paramsMap.put("themeId", themeId);
		paramsMap.put("method", "getThemeInfo");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
