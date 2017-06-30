package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取系统推荐的未加入的圈子列表（随机排列）
 * 
 * @author datacomo-160
 * 
 */
public class RecommendGroupListParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param pointId
	 */
	public RecommendGroupListParams(Context context) {
		super(context);
		setVariable();
	}

	/**
	 * 设置参数
	 */
	private void setVariable() {
		paramsMap.put("method", "recommendGroupList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
