package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 第三方分享统计
 * 
 * @author datacomo-160
 * 
 */
public class ShareToThirdRecordParams extends BasicParams {

	/**
	 * 三方分享统计
	 * 
	 * @param context
	 * @param objectType
	 *            1:圈博 2：圈子照片 3：社员心情
	 * @param objectId
	 * @param thirdType
	 *            1:新浪 2：腾讯 3：人人 4：开心 5：搜狐 6：网易 7：豆瓣 8：微信
	 */
	public ShareToThirdRecordParams(Context context, String objectType,
			String objectId, String thirdType) {
		super(context);
		setVariable(objectType, objectId, thirdType);
	}

	private void setVariable(String objectType, String objectId,
			String thirdType) {
		paramsMap.put("objectType", objectType);
		paramsMap.put("objectId", objectId);
		paramsMap.put("thirdType", thirdType);
		paramsMap.put("method", "shareToThirdRecord");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
