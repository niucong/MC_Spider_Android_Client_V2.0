package com.datacomo.mc.spider.android.params;

import android.content.Context;

public class ValidatePhoneForInviteRegisterParams extends BasicParams {

	public ValidatePhoneForInviteRegisterParams(Context context, String phones) {
		super(context);
		setVariable(phones);
	}

	/**
	 * 设置参数
	 * 
	 * @param searchName
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 */
	private void setVariable(String phones) {
		paramsMap.put("phones", phones);
		paramsMap.put("method", "validatePhoneForInviteRegister");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
