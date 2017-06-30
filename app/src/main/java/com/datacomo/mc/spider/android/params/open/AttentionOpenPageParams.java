package com.datacomo.mc.spider.android.params.open;

import android.content.Context;

import com.datacomo.mc.spider.android.params.BasicParams;

/**
 * 关注开放主页
 * 
 * @author datacomo-160
 * 
 */
public class AttentionOpenPageParams extends BasicParams {

	/**
	 * 参数设置
	 * @param context
	 * @param openPageId
	 * @param isAttention true：加关注，false：取消关注
	 */
	public AttentionOpenPageParams(Context context, String openPageId,
			boolean isAttention) {
		super(context);
		setVariable(openPageId, isAttention);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String openPageId, boolean isAttention) {
		paramsMap.put("openPageId", openPageId);
		if (isAttention) {
			paramsMap.put("method", "attentionOpenPage");
		} else {
			paramsMap.put("method", "cancelAttentionOpenPage");
		}
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
