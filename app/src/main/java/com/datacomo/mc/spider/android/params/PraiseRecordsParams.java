package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 资源赞的成员列表
 * 
 * @author datacomo-160
 * 
 */
public class PraiseRecordsParams extends BasicParams {

	/**
	 * 
	 * @param context
	 * @param groupId
	 * @param quuboId
	 * @param objectType
	 * @param noPaging
	 * @param startRecord
	 * @param maxResults
	 */
	public PraiseRecordsParams(Context context, String groupId, String quuboId,
			String objectType, String noPaging, String startRecord,
			String maxResults) {
		super(context);
		setVariable(groupId, quuboId, objectType, noPaging, startRecord,
				maxResults);
	}

	/**
	 * 设置参数
	 * 
	 * @param fileUrl
	 * @param filePath
	 * @param fileName
	 */
	private void setVariable(String groupId, String quuboId, String objectType,
			String noPaging, String startRecord, String maxResults) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("quuboId", quuboId);
		paramsMap.put("objectType", objectType);
		paramsMap.put("noPaging", noPaging);
		paramsMap.put("startRecord", startRecord);
		paramsMap.put("maxResults", maxResults);
		paramsMap.put("method", "praiseRecords");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
