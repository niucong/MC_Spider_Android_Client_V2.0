package com.datacomo.mc.spider.android.params.theme;

import java.util.HashMap;

import com.datacomo.mc.spider.android.params.BasicParams;

import android.content.Context;

/**
 * 分享话题
 * 
 * @author datacomo-160
 * 
 */
public class ShareThemeToMemberParams extends BasicParams {

	/**
	 * 分享话题
	 * 
	 * @param context
	 * @param groupId
	 *            圈子编号
	 * @param themeId
	 *            话题编号
	 * @param memberIds
	 *            被分享社员编号
	 * @param allLeaguers
	 *            是否是全部成员
	 * @param shareWord
	 *            分享 附言
	 */
	public ShareThemeToMemberParams(Context context, String groupId,
			String themeId, String[] memberIds, String allLeaguers,
			String shareWord) {
		super(context);
		setVariable(groupId, themeId, memberIds, allLeaguers, shareWord);
	}

	private void setVariable(String groupId, String themeId,
			String[] memberIds, String allLeaguers, String shareWord) {
		paramsMap.put("groupId", groupId);
		paramsMap.put("themeId", themeId);
		mHashMap = new HashMap<String, String[]>();
		mHashMap.put("memberIds", memberIds);
		paramsMap.put("memberIds", "");
		paramsMap.put("allLeaguers", allLeaguers);
		paramsMap.put("shareWord", shareWord);
		paramsMap.put("method", "shareThemeToMember");

		super.setVariable(true);
	}

	@Override
	public String getParams() {
		return strParams;
	}

}
