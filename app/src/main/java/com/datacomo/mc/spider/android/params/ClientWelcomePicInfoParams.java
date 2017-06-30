package com.datacomo.mc.spider.android.params;

import android.content.Context;

import com.datacomo.mc.spider.android.enums.ClientWelcomePicEnum;

/**
 * 获取上传的最后一张图片（欢迎页面的当前图片）
 * 
 * @author datacomo-160
 * 
 */
public class ClientWelcomePicInfoParams extends BasicParams {

	/**
	 * 欢迎页面
	 * 
	 * @param context
	 * @param method
	 * @param groupId
	 *            顶级认证圈子Id
	 */
	public ClientWelcomePicInfoParams(Context context,
			ClientWelcomePicEnum method, int groupId) {
		super(context);
		setVariable(method, groupId);
	}

	private void setVariable(ClientWelcomePicEnum method, int groupId) {
		if (groupId != 0)
			paramsMap.put("groupId", groupId + "");
		paramsMap.put("method", method.toString());

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
