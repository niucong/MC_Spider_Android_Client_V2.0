package com.datacomo.mc.spider.android.params;

import java.util.HashMap;

import android.content.Context;

/**
 * 编辑圈子资料
 * 
 * @author datacomo-160
 * 
 */
public class EditGroupBasicInfoParams extends BasicParams {

	/**
	 * 参数设置
	 * 
	 * @param context
	 * @param groupName
	 *            圈子名称
	 * @param isUpdateName
	 *            是否需要更新圈子名称：true-需要；false-不需要
	 * @param groupDesc
	 *            圈子描述
	 * @param isUpdateDesc
	 *            是否需要更新圈子描述：true-需要；false-不需要
	 * @param tags
	 *            圈子标签
	 * @param isUpdateTags
	 *            是否需要更新圈子标签：true-需要；false-不需要
	 * @param privacySetting
	 *            圈子隐私设置：1 - 公开;2 - 私密
	 * @param isUpdatePrivacy
	 *            是否需要更新圈子隐私设置：true-需要；false-不需要
	 */
	public EditGroupBasicInfoParams(Context context, String groupId,
			String groupName, boolean isUpdateName, String groupDesc,
			boolean isUpdateDesc, String tags, boolean isUpdateTags,
			String privacySetting, boolean isUpdatePrivacy) {
		super(context);
		setVariable(groupId, groupName, isUpdateName, groupDesc, isUpdateDesc,
				tags, isUpdateTags, privacySetting, isUpdatePrivacy);
	}

	private void setVariable(String groupId, String groupName,
			boolean isUpdateName, String groupDesc, boolean isUpdateDesc,
			String tags, boolean isUpdateTags, String privacySetting,
			boolean isUpdatePrivacy) {

		paramsMap.put("groupId", groupId);
		paramsMap.put("groupName", groupName);
		paramsMap.put("isUpdateName", isUpdateName + "");
		paramsMap.put("groupDesc", groupDesc);
		paramsMap.put("isUpdateDesc", isUpdateDesc + "");
		// mHashMap = new HashMap<String, String[]>();
		paramsMap.put("tags", tags);
		paramsMap.put("isUpdateTags", isUpdateTags + "");
		paramsMap.put("privacySetting", privacySetting);
		paramsMap.put("isUpdatePrivacy", isUpdatePrivacy + "");

		paramsMap.put("method", "editGroupBasicInfo");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
