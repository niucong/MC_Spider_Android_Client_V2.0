package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 发布心情
 * 
 * @author datacomo-160
 * 
 */
public class CreateMoodContentParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param moodContent
	 */
	public CreateMoodContentParams(Context context, String moodContent) {
		super(context);
		setVariable(moodContent);
	}

	/**
	 * 设置参数
	 * 
	 * @param moodContent
	 */
	private void setVariable(String moodContent) {

		paramsMap.put("moodContent", moodContent);
		paramsMap.put("method", "createMoodContent");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
