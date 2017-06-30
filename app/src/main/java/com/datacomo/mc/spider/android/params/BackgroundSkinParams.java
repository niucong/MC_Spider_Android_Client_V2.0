package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取圈子、成员皮肤
 * 
 * @author datacomo-160
 * 
 */
public class BackgroundSkinParams extends BasicParams {

	/**
	 * 获取所有文件夹列表参数设置
	 * 
	 * @param context
	 * @param id
	 *            ：memberId、groupId
	 * @param method
	 *            ：memberSkin、groupSkin
	 */
	public BackgroundSkinParams(Context context, String id, String method) {
		super(context);
		setVariable(id, method);
	}

	/**
	 * 设置参数
	 * 
	 * @param id
	 * @param method
	 */
	private void setVariable(String id, String method) {
		if ("memberSkin".equals(method)) {
			if (id == null || "".equals(id))
				id = "0";
			paramsMap.put("memberId", id);
		} else if ("groupSkin".equals(method)) {
			paramsMap.put("groupId", id);
		}
		paramsMap.put("method", method);
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
