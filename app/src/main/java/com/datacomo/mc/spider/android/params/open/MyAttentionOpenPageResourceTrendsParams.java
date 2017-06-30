package com.datacomo.mc.spider.android.params.open;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 获取我关注的开放主页动态（动态产生时间倒序）
 * 
 * @author datacomo-160
 * 
 */
public class MyAttentionOpenPageResourceTrendsParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param objectType
	 *            对象类型
	 * @param isFilterByType
	 *            是否按类型查看 是：true 否：false
	 * @param trendId
	 *            动态编号
	 * @param noPaging
	 *            true分页,false不分页
	 * @param maxResult
	 *            最大记录
	 */
	public MyAttentionOpenPageResourceTrendsParams(Context context,
			String objectType, String isFilterByType, String trendId,
			String noPaging, String maxResults) {
		super(context);
		setVariable(objectType, isFilterByType, trendId, noPaging, maxResults);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String objectType, String isFilterByType,
			String trendId, String noPaging, String maxResults) {
		paramsMap.put("objectType", objectType);
		paramsMap.put("isFilterByType", isFilterByType);
		paramsMap.put("trendId", trendId);
		paramsMap.put("noPaging", noPaging);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", "myAttentionOpenPageResourceTrends");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
