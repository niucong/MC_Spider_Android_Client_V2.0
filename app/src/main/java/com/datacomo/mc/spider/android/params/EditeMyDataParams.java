package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 编辑个人资料
 * 
 * @author datacomo-160
 * 
 */
public class EditeMyDataParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param name
	 * @param sex
	 * @param birthday
	 * @param introduction
	 */
	public EditeMyDataParams(Context context, String name, int sex,
			String birthday, String introduction) {
		super(context);
		setVariable(name, sex, birthday, introduction);
	}

	/**
	 * 设置参数
	 * 
	 * @param name
	 * @param sex
	 * @param birthday
	 * @param introduction
	 */
	private void setVariable(String name, int sex, String birthday,
			String introduction) {

		paramsMap.put("name", name);
		paramsMap.put("sex", sex + "");
		paramsMap.put("birthday", birthday);
		paramsMap.put("isUpdateIntroduction", "true");
		paramsMap.put("introduction", introduction);
		paramsMap.put("method", "editeMyData");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
