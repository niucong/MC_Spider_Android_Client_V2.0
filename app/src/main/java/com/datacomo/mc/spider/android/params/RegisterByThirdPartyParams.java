package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 第三方帐号注册登录
 * 
 * @author datacomo-160
 * 
 */
public class RegisterByThirdPartyParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param openId
	 * @param thirdPartyType
	 *            （社员来源：1：新浪；2：腾讯；3：人人）
	 * @param name
	 * @param sex
	 * @param oauth_token
	 * @param oauth_token_secret
	 */
	public RegisterByThirdPartyParams(Context context, String openId,
			String thirdPartyType, String name, String sex, String headUrlPath,
			String oauth_token, String oauth_token_secret) {
		super(context);
		setVariable(openId, thirdPartyType, name, sex, headUrlPath,
				oauth_token, oauth_token_secret);
	}

	/**
	 * 设置参数
	 */
	private void setVariable(String openId, String thirdPartyType, String name,
			String sex, String headUrlPath, String oauth_token,
			String oauth_token_secret) {
		paramsMap.put("openId", openId);
		paramsMap.put("thirdPartyType", thirdPartyType);
		paramsMap.put("name", name);
		paramsMap.put("sex", sex);
		paramsMap.put("headUrlPath", headUrlPath);
		paramsMap.put("oauth_token", oauth_token);
		paramsMap.put("oauth_token_secret", oauth_token_secret);
		paramsMap.put("method", "registerByThirdParty");
		super.setVariable(false);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
