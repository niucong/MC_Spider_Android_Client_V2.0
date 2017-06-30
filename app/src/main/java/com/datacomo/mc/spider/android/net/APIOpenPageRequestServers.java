package com.datacomo.mc.spider.android.net;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.datacomo.mc.spider.android.net.been.GroupBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceTrendBean;
import com.datacomo.mc.spider.android.net.been.map.MapOpenPageFanBean;
import com.datacomo.mc.spider.android.params.GroupInfoParams;
import com.datacomo.mc.spider.android.params.open.AttentionOpenPageParams;
import com.datacomo.mc.spider.android.params.open.LeaveMessageForOpenPageParams;
import com.datacomo.mc.spider.android.params.open.MyAttentionOpenPageResourceTrendsParams;
import com.datacomo.mc.spider.android.params.open.OpenPageFansListParams;
import com.datacomo.mc.spider.android.params.open.OpenPageListParams;
import com.datacomo.mc.spider.android.params.open.OpenPageResourceTrendsParams;
import com.datacomo.mc.spider.android.params.open.PopOpenPageResourceTrendsParams;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.JsonParseTool;
import com.datacomo.mc.spider.android.util.StreamUtil;

public class APIOpenPageRequestServers {
	private static final String TAG = "APIRequestServers";

	/**
	 * 获取开放主页动态
	 * 
	 * @param context
	 * @param objectType
	 * @param isFilterByType
	 *            是否按类型查看 是：true 否：false
	 * @param openPageId
	 * @param isAllOpenPage
	 *            是否是所有开放主页true：所有，false：某个圈子
	 * @param trendId
	 * @param noPaging
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public static MCResult openPageResourceTrends(final Context context,
			String objectType, String isFilterByType, String openPageId,
			String isAllOpenPage, String trendId, String noPaging,
			String maxResults) throws Exception {
		L.d(TAG, "openPageResourceTrends openPageId=" + openPageId);
		String url = URLProperties.TREND_JSON;
		String params = new OpenPageResourceTrendsParams(context, objectType,
				isFilterByType, openPageId, isAllOpenPage, trendId, noPaging,
				maxResults).getParams();
		L.i(TAG, "openPageResourceTrends url=" + url + ",params=" + params);

		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "openPageResourceTrends", result);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		List<Object> objectList = JsonParseTool.dealListResult(trends,
				ResourceTrendBean.class);
		mcResult.setResult(objectList);

		if ((trendId == null || "".equals(trendId) || "0".equals(trendId))
				&& objectList != null && objectList.size() > 0) {
			LocalDataService.getInstense().save(context,
					LocalDataService.TXT_OPENALL, trends);
		}
		return mcResult;
	}

	/**
	 * 获取我关注的开放主页动态（动态产生时间倒序）
	 * 
	 * @param context
	 * @param trendId
	 * @param maxResults
	 * @param noPaging
	 *            true分页,false不分页
	 * @return
	 * @throws Exception
	 */
	public static MCResult myAttentionOpenPageResourceTrends(
			final Context context, String trendId, String maxResults,
			boolean noPaging) throws Exception {
		String url = URLProperties.TREND_JSON;
		String params = new MyAttentionOpenPageResourceTrendsParams(context,
				"OBJ_GROUP_ALL", "false", trendId, noPaging + "", maxResults)
				.getParams();
		L.i(TAG, "myAttentionOpenPageResourceTrends url=" + url + ",params="
				+ params);

		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "myAttentionOpenPageResourceTrends", result);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		ArrayList<Object> objectList = JsonParseTool.dealListResult(trends,
				ResourceTrendBean.class);
		mcResult.setResult(objectList);

		if ((trendId == null || "".equals(trendId) || "0".equals(trendId))
				&& objectList != null && objectList.size() > 0) {
			LocalDataService.getInstense().save(context,
					LocalDataService.TXT_OPENATTEN, trends);
		}
		return mcResult;
	}

	/**
	 * 获取热门的开放主页动态(资源数量倒序)
	 * 
	 * @param context
	 * @param trendId
	 * @param maxResults
	 * @param noPaging
	 *            true分页,false不分页
	 * @return
	 * @throws Exception
	 */
	public static MCResult popOpenPageResourceTrends(final Context context,
			String trendId, String maxResults, boolean noPaging)
			throws Exception {
		String url = URLProperties.TREND_JSON;
		String params = new PopOpenPageResourceTrendsParams(context,
				"OBJ_GROUP_ALL", "false", trendId, noPaging + "", maxResults)
				.getParams();
		L.i(TAG, "openPageResourceTrends url=" + url + ",params=" + params);

		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "openPageResourceTrends", result);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		ArrayList<Object> objectList = JsonParseTool.dealListResult(trends,
				ResourceTrendBean.class);
		mcResult.setResult(objectList);
		if ((trendId == null || "".equals(trendId) || "0".equals(trendId))
				&& objectList != null && objectList.size() > 0) {
			LocalDataService.getInstense().save(context,
					LocalDataService.TXT_OPENHOT, trends);
		}
		return mcResult;
	}

	/**
	 * 获取开放主页列表
	 * 
	 * @param context
	 * @param orderType
	 *            1、最新入驻2、热门的3、我关注的
	 * @param startRecord
	 * @param maxResults
	 * @param noPaging
	 *            true不分页,false分页
	 * @return
	 * @throws Exception
	 */
	public static MCResult openPageList(final Context context,
			String orderType, String startRecord, String maxResults,
			boolean noPaging) throws Exception {
		String url = URLProperties.GROUP_JSON;
		String params = new OpenPageListParams(context, orderType, noPaging
				+ "", startRecord, maxResults).getParams();
		L.i(TAG, "openPageList url=" + url + ",params=" + params);

		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "openPageList", result);
		MCResult mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}

		final String trends = mcResult.getResult().toString();
		ArrayList<Object> objectList = JsonParseTool.dealListResult(trends,
				GroupBean.class);
		mcResult.setResult(objectList);
		if ((startRecord == null || "".equals(startRecord) || "0"
				.equals(startRecord))
				&& objectList != null
				&& objectList.size() > 0) {
			if ("1".equals(orderType)) {
				LocalDataService.getInstense().save(context,
						LocalDataService.TXT_OPENGROUPNEW, trends);
			} else if ("2".equals(orderType)) {
				LocalDataService.getInstense().save(context,
						LocalDataService.TXT_OPENGROUPHOT, trends);
			} else if ("3".equals(orderType)) {
				LocalDataService.getInstense().save(context,
						LocalDataService.TXT_OPENGROUPATTEN, trends);
			}
		}
		return mcResult;
	}

	/**
	 * 获取开放主页圈子详情
	 * 
	 * @param context
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public static MCResult groupInfo(Context context, String groupId)
			throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.GROUP_JSON;
		String params = new GroupInfoParams(context, groupId, true).getParams();
		L.i(TAG, "groupInfo url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "groupInfo", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		// 解析GroupBean
		GroupBean groupBean = (GroupBean) JsonParseTool.dealComplexResult(
				mcResult.getResult().toString(), GroupBean.class);
		mcResult.setResult(groupBean);
		return mcResult;
	}

	/**
	 * 关注开放主页
	 * 
	 * @param context
	 * @param openPageId
	 * @param isAttention
	 *            true：加关注，false：取消关注
	 * @return
	 * @throws Exception
	 */
	public static MCResult attentionOpenPage(Context context,
			String openPageId, boolean isAttention) throws Exception {
		// 拼接请求参数
		String url = URLProperties.GROUP_JSON;
		String params = new AttentionOpenPageParams(context, openPageId,
				isAttention).getParams();
		L.i(TAG, "attentionOpenPage url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "attentionOpenPage", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

	/**
	 * 获取开放主页粉丝列表
	 * 
	 * @param context
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	public static MCResult openPageFansList(Context context, String openPageId,
			String noPaging, String startRecord, String maxResults)
			throws Exception {
		MCResult mcResult = null;
		// 拼接请求参数
		String url = URLProperties.GROUP_JSON;
		String params = new OpenPageFansListParams(context, openPageId,
				noPaging, startRecord, maxResults).getParams();
		L.i(TAG, "openPageFansList url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "openPageFansList", result);
		// 解析MCResult
		mcResult = (MCResult) JsonParseTool.dealSingleResult(result,
				MCResult.class);
		if (mcResult == null || mcResult.getResultCode() != 1) {
			return mcResult;
		}
		// 解析GroupBean
		MapOpenPageFanBean groupBean = (MapOpenPageFanBean) JsonParseTool
				.dealComplexResult(mcResult.getResult().toString(),
						MapOpenPageFanBean.class);
		mcResult.setResult(groupBean);
		return mcResult;
	}

	/**
	 * 给开放主页留言
	 * 
	 * @param context
	 * @param openPageId
	 * @param messageContent
	 * @param messageType
	 * 
	 * @return
	 * @throws Exception
	 */
	public static MCResult leaveMessageForOpenPage(Context context,
			String openPageId, String messageContent, String messageType)
			throws Exception {
		// 拼接请求参数
		String url = URLProperties.GROUP_JSON;
		String params = new LeaveMessageForOpenPageParams(context, openPageId,
				messageContent, messageType).getParams();
		L.i(TAG, "leaveMessageForOpenPage url=" + url + ",params=" + params);
		// 获取数据
		String result = StreamUtil.readData(new HttpRequestServers()
				.getRequest(url + "?" + params));
		L.getLongLog(TAG, "leaveMessageForOpenPage", result);
		return (MCResult) JsonParseTool
				.dealSingleResult(result, MCResult.class);
	}

}
