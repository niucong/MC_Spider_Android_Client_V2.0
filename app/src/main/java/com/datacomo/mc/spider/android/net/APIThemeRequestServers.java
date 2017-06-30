package com.datacomo.mc.spider.android.net;

import android.content.Context;

import com.datacomo.mc.spider.android.net.been.GroupThemeBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.map.MapGroupThemeBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceGreatBean;
import com.datacomo.mc.spider.android.params.theme.AttentionOrCancelThemeParams;
import com.datacomo.mc.spider.android.params.theme.QuuboListForGroupByThemeParams;
import com.datacomo.mc.spider.android.params.theme.SearchThemesListParams;
import com.datacomo.mc.spider.android.params.theme.ShareThemeToMemberParams;
import com.datacomo.mc.spider.android.params.theme.ThemeFocusOrBrowseOrShareListParams;
import com.datacomo.mc.spider.android.params.theme.ThemeInfoParams;
import com.datacomo.mc.spider.android.params.theme.ThemesListParams;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.JsonParseTool;
import com.datacomo.mc.spider.android.util.StreamUtil;

public class APIThemeRequestServers {
	private static final String TAG = "APIRequestServers";

	/**
	 * 某个圈子专题列表
	 * 
	 * @param context
	 * @param groupId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult themesList(Context context, String groupId,
			String startRecord, String maxResults) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_TOPIC_JSON;
		String params = new ThemesListParams(context, groupId, startRecord,
				maxResults).getParams();
		L.i(TAG, "themesList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "themesList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapGroupThemeBean mapGroupThemeBean = (MapGroupThemeBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapGroupThemeBean.class);
		mcResult.setResult(mapGroupThemeBean);
		return mcResult;
	}

	/**
	 * 搜索专题
	 * 
	 * @param context
	 * @param groupId
	 *            圈子（当isGroup为false时不起作用）
	 * @param objectName
	 *            搜索条件（可以传专题发布者的名字或专题名称）
	 * @param startRecord
	 * @param maxResults
	 * @param isGroup
	 *            判断是否是单个圈子（true false）
	 * @return
	 * @throws Exception
	 */
	public static MCResult searchThemesList(Context context, String groupId,
			String objectName, String startRecord, String maxResults,
			boolean isGroup) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_TOPIC_JSON;
		String params = new SearchThemesListParams(context, groupId,
				objectName, startRecord, maxResults, isGroup).getParams();
		L.i(TAG, "themesList url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "themesList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapGroupThemeBean mapGroupThemeBean = (MapGroupThemeBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapGroupThemeBean.class);
		mcResult.setResult(mapGroupThemeBean);
		return mcResult;
	}

	/**
	 * 获取圈子中某个话题的圈博列表
	 * 
	 * @param context
	 * @param groupId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult quuboListForGroupByTheme(Context context,
			String groupId, String themeId, String startRecord,
			String maxResults) throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.TREND_JSON;
		String params = new QuuboListForGroupByThemeParams(context, groupId,
				themeId, startRecord, maxResults).getParams();
		L.i(TAG, "quuboListForGroupByTheme url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "quuboListForGroupByTheme", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapResourceBean mapResourceBean = (MapResourceBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapResourceBean.class);
		mcResult.setResult(mapResourceBean);
		return mcResult;
	}

	/**
	 * 获取专题详情
	 * 
	 * @param context
	 * @param groupId
	 * @param startRecord
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult themeInfo(Context context, String themeId)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_TOPIC_JSON;
		String params = new ThemeInfoParams(context, themeId).getParams();
		L.i(TAG, "themeInfo url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "themeInfo", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		GroupThemeBean groupThemeBean = (GroupThemeBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						GroupThemeBean.class);
		mcResult.setResult(groupThemeBean);
		return mcResult;
	}

	/**
	 * 关注或取消关注话题
	 * 
	 * @param context
	 * @param themeId
	 * @param operateType
	 *            操作类型：1.关注话题 2.取消关注话题，默认1
	 * @return
	 * @throws Exception
	 */
	public static MCResult attentionOrCancelTheme(Context context,
			String themeId, String operateType) throws Exception {
		String url = URLProperties.GROUP_TOPIC_JSON;
		String params = new AttentionOrCancelThemeParams(context, themeId,
				operateType).getParams();
		L.i(TAG, "attentionOrCancelTheme url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "attentionOrCancelTheme", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 获取某个专题关注/看过/分享 的社员记录列表
	 * 
	 * @param context
	 * @param themeId
	 * @param type
	 *            ：1.关注的人2.看过的人3.分享的人，默认为1
	 * @param startRecord
	 * @param maxResults
	 */
	public static MCResult themeFocusOrBrowseOrShareList(Context context,
			String themeId, String type, String startRecord, String maxResults)
			throws Exception {
		MCResult mcResult = null;
		String url = URLProperties.GROUP_TOPIC_JSON;
		String params = new ThemeFocusOrBrowseOrShareListParams(context,
				themeId, type, startRecord, maxResults).getParams();
		L.i(TAG, "themeFocusOrBrowseOrShareList url=" + url + ",params="
				+ params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "themeFocusOrBrowseOrShareList", result);
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		MapResourceGreatBean mapResourceGreatBean = (MapResourceGreatBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapResourceGreatBean.class);
		mcResult.setResult(mapResourceGreatBean);
		return mcResult;
	}

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
	 * @param maxResults
	 */
	public static MCResult shareThemeToMember(Context context, String groupId,
			String themeId, String[] memberIds, String allLeaguers,
			String shareWord) throws Exception {
		String url = URLProperties.GROUP_TOPIC_JSON;
		String params = new ShareThemeToMemberParams(context, groupId, themeId,
				memberIds, allLeaguers, shareWord).getParams();
		L.i(TAG, "shareThemeToMember url=" + url + ",params=" + params);
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));

		L.getLongLog(TAG, "shareThemeToMember", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

}
