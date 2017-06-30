package com.datacomo.mc.spider.android.params;

import android.content.Context;

/**
 * 创建圈子
 * 
 * @author datacomo-160
 * 
 */
public class CreateGroupParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupName
	 * @param groupDesc
	 * @param tags
	 * @param privacySetting
	 */
	public CreateGroupParams(Context context, String groupName,
			String groupDesc, String tags, String privacySetting) {
		super(context);
		setVariable(groupName, groupDesc, tags, privacySetting);
	}

	/**
	 * 设置参数
	 * 
	 * @param groupName
	 * @param groupDesc
	 * @param tags
	 * @param privacySetting
	 */
	private void setVariable(String groupName, String groupDesc, String tags,
			String privacySetting) {

		paramsMap.put("groupName", groupName);
		if (groupDesc != null && !"".equals(groupDesc)) {
			paramsMap.put("groupDesc", groupDesc);
		}
		if (tags != null && !"".equals(tags)) {
			paramsMap.put("tags", tags);
		}

		paramsMap.put("privacySetting", privacySetting);
		paramsMap.put("method", "createGroup");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
