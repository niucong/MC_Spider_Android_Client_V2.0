package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 
 */
public class ApplyToBeManagerParams extends BasicParams {

	/**
	 * 申请成为管理员
	 * 
	 * @param groupId
	 *            0:异常 1：成功 2：社员不存在 3：圈子不存在 4：已经发过管理员申请 5：已经是有管理圈子权限 6：没有申请管理员资格
	 */
	public ApplyToBeManagerParams(Context context, String groupId) {
		super(context);
		setVariable(groupId);
	}

	/**
	 * 设置参数
	 * 
	 * @param loadName
	 * @param password
	 */
	private void setVariable(String groupId) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("method", "applyToBeManager");
		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
