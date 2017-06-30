package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 搜索成员
 * 
 * @author datacomo-160
 * 
 */
public class SearchMembersByNameParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param name
	 * @param startRecord
	 * @param maxResults
	 */
	public SearchMembersByNameParams(Context context, String name,
			String startRecord, String maxResults) {
		super(context);
		setVariable(name, startRecord, maxResults);
	}

	/**
	 * 设置参数
	 * 
	 * @param name
	 * @param startRecord
	 * @param maxResults
	 */
	private void setVariable(String name, String startRecord, String maxResults) {
		paramsMap.put("name", name);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", "searchMembersByName");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
