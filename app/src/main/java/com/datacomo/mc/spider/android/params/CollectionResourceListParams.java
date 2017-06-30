package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 获取某一类型的收藏列表
 * 
 * @author datacomo-160
 * 
 */
public class CollectionResourceListParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param objectType
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	public CollectionResourceListParams(Context context, String objectType,
			String startRecord, String maxResults, boolean noPaging) {
		super(context);
		setVariable(objectType, startRecord, maxResults, noPaging);
	}

	/**
	 * 设置参数
	 * 
	 * @param objectType
	 * @param startRecord
	 * @param maxResult
	 * @param noPaging
	 */
	private void setVariable(String objectType, String startRecord,
			String maxResults, boolean noPaging) {
		paramsMap.put("objectType", objectType);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("noPaging", noPaging + "");
		paramsMap.put("method", "collectionResourceList");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
