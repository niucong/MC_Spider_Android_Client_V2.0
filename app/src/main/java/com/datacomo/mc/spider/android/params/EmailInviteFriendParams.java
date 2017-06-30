package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * email邀请朋友
 */
public class EmailInviteFriendParams extends BasicParams {

	/**
	 * 
	 * @param context
	 * @param emails
	 */
	public EmailInviteFriendParams(Context context, String emails) {
		super(context);
		setVariable(emails);
	}

	private void setVariable(String emails) {
		paramsMap.put("emails", emails);
		paramsMap.put("method", "emailInviteFriend");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}
}
