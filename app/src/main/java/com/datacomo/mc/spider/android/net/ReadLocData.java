package com.datacomo.mc.spider.android.net;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.db.GroupTrendListService;
import com.datacomo.mc.spider.android.db.MessageService;
import com.datacomo.mc.spider.android.enums.ResourceTypeEnum;
import com.datacomo.mc.spider.android.net.been.DiaryInfoBean;
import com.datacomo.mc.spider.android.net.been.MailContactBean;
import com.datacomo.mc.spider.android.net.been.MessageContacterBean;
import com.datacomo.mc.spider.android.net.been.MessageGreetBean;
import com.datacomo.mc.spider.android.net.been.MessageNoticeBean;
import com.datacomo.mc.spider.android.net.been.ResourceTrendBean;
import com.datacomo.mc.spider.android.net.been.map.MapMailContactBean;
import com.datacomo.mc.spider.android.net.been.map.MapMessageNoticeBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.JsonParseTool;

public class ReadLocData {
	private static final String TAG = "ReadLocData";

	/**
	 * 获取本地圈子动态
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<ResourceTrendBean> getLocTrends(Context context,
			String type) throws Exception {
		GroupTrendListService listService = new GroupTrendListService(context);
		String session_key = App.app.share.getSessionKey();
		String result = listService.queryTrends(session_key, type);
		// L.getLongLog(TAG, "getLocTrends", result);
		if (result == null) {
			return null;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(result,
				ResourceTrendBean.class);
		ArrayList<ResourceTrendBean> trendBeans = new ArrayList<ResourceTrendBean>();
		for (Object object : objectList) {
			trendBeans.add((ResourceTrendBean) object);
		}
		return trendBeans;
	}

	/**
	 * 获取本地某一类型的收藏列表
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static MapResourceBean getLocCollection(Context context,
			ResourceTypeEnum resourceTypeEnum) {
		GroupTrendListService listService = new GroupTrendListService(context);
		String session_key = App.app.share.getSessionKey();
		String type = "";

		switch (resourceTypeEnum) {
		case OBJ_GROUP_QUUBO:
			type = "3";
			break;
		case OBJ_GROUP_FILE:
			type = "4";
			break;
		case OBJ_GROUP_PHOTO:
			type = "5";
			break;
		default:
			break;
		}
		String result = listService.queryTrends(session_key, type);
		if (result == null) {
			return null;
		}
		try {
			return (MapResourceBean) JsonParseTool.dealComplexResult(result,
					MapResourceBean.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取本地通知
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static List<MessageNoticeBean> getLocNotices(Context context)
			throws Exception {
		MessageService listService = new MessageService(context);
		String session_key = App.app.share.getSessionKey();
		String result = listService.queryTrends(session_key, "0");
		// L.getLongLog(TAG, "getLocNotices", result);
		if (result == null) {
			return null;
		}
		MapMessageNoticeBean mapBean = (MapMessageNoticeBean) JsonParseTool
				.dealComplexResult(result, MapMessageNoticeBean.class);
		return mapBean.getLIST();
	}

	/**
	 * 获取本地私信联系人
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<MessageContacterBean> getLocMessages(Context context)
			throws Exception {
		MessageService listService = new MessageService(context);
		String session_key = App.app.share.getSessionKey();
		String result = listService.queryTrends(session_key, "1");
		if (result == null) {
			return null;
		}
		L.getLongLog(TAG, "getLocMessages", result);
		ArrayList<Object> objectList = JsonParseTool.dealListResult(result,
				MessageContacterBean.class);
		ArrayList<MessageContacterBean> beans = new ArrayList<MessageContacterBean>();
		for (Object object : objectList) {
			beans.add((MessageContacterBean) object);
		}
		return beans;
	}

	/**
	 * 获取本地招呼
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<MessageGreetBean> getLocGreets(Context context)
			throws Exception {
		MessageService listService = new MessageService(context);
		String session_key = App.app.share.getSessionKey();
		String result = listService.queryTrends(session_key, "2");
		if (result == null) {
			return null;
		}
		L.getLongLog(TAG, "getLocMessages", result);
		ArrayList<Object> objectList = JsonParseTool.dealListResult(result,
				MessageGreetBean.class);
		ArrayList<MessageGreetBean> beans = new ArrayList<MessageGreetBean>();
		for (Object object : objectList) {
			beans.add((MessageGreetBean) object);
		}
		return beans;
	}

	/**
	 * 获取本地邮件联系人
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static List<MailContactBean> getLocMails(Context context)
			throws Exception {
		MessageService listService = new MessageService(context);
		String session_key = App.app.share.getSessionKey();
		String result = listService.queryTrends(session_key, "3");
		if (result == null) {
			return null;
		}
		// L.getLongLog(TAG, "getLocMails", result);
		MapMailContactBean mapBean = (MapMailContactBean) JsonParseTool
				.dealComplexResult(result, MapMailContactBean.class);
		return mapBean.getCONTACTLIST();
	}

	/**
	 * 获取本地某一类型的笔记列表
	 * 
	 * @param context
	 * @param diaryType
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<DiaryInfoBean> getLocNotes(Context context,
			String diaryType) throws Exception {
		MessageService listService = new MessageService(context);
		String session_key = App.app.share.getSessionKey();
		String result = null;
		if ("1".equals(diaryType)) {
			result = listService.queryTrends(session_key, "4");
		} else {
			result = listService.queryTrends(session_key, "5");
		}
		if (result == null) {
			return null;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(result,
				DiaryInfoBean.class);
		ArrayList<DiaryInfoBean> beans = new ArrayList<DiaryInfoBean>();
		for (Object object : objectList) {
			beans.add((DiaryInfoBean) object);
		}
		return beans;
	}

}
