package com.datacomo.mc.spider.android.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.db.FriendListService;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.FriendSimpleBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.map.MapFriendSimpleBean;
import com.datacomo.mc.spider.android.url.L;

public class UpdateFriendListThread {
	private static final String TAG = "UpdateFriendListThread";

	private static boolean isUpdate = false;
	// private static int allNum = 0;

	private static FriendListService friendListService;

	// private static FriendToGroupService friendToGroupService;

	public static void updateFriendList(final Context context, Handler handler) {
		if (isUpdate)
			return;
		isUpdate = true;

		friendListService = FriendListService.getService(context);
		// friendToGroupService = new FriendToGroupService(context);

		// 数据库读取更新时间
		long startUpdateTime = 0;
		long startDeleteTime = 0;
		try {
			startUpdateTime = App.app.share.getLongMessage("NotificationSetup",
					"startUpdateTimeFriendNew", 0);
			startDeleteTime = App.app.share.getLongMessage("NotificationSetup",
					"startDeleteTimeFriendNew", 0);
			L.d(TAG, "updateFriendList startUpdateTime=" + startUpdateTime
					+ ",startDeleteTime=" + startDeleteTime);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		int num = 0;
		int maxResults = 200;

		// if (startUpdateTime == 0) {
		// try {
		// MCResult mc = APIRequestServers.friendNum(context, "0");
		// if (mc.getResultCode() == 1) {
		// allNum = (Integer) mc.getResult();
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// } else {
		// handler = null;
		// }

		// new Thread() {
		// public void run() {
		try {
			// TODO
			getFriendUpdateList(context, startUpdateTime, startDeleteTime, num,
					maxResults, "true");
		} catch (Exception e) {
			e.printStackTrace();
		}
		isUpdate = false;
		// };
		// }.start();
	}

	private static void getFriendUpdateList(Context context,
			long startUpdateTime, long startDeleteTime, int startRecord,
			int maxResults, String noPaging) throws Exception {
		MCResult mc = APIRequestServers.friendUpdateList(context,
				startUpdateTime + "", startDeleteTime + "", startRecord + "",
				maxResults + "", noPaging);
		if (mc.getResultCode() == 1) {
			MapFriendSimpleBean friendSimpleBean = (MapFriendSimpleBean) mc
					.getResult();
			String FRIEND_DELETE_LIST = friendSimpleBean
					.getFRIEND_DELETE_LIST();
			List<FriendSimpleBean> FRIEND_UPDATE_LIST = friendSimpleBean
					.getFRIEND_UPDATE_LIST();

			if (startRecord == 0) {
				if (FRIEND_DELETE_LIST != null
						&& FRIEND_DELETE_LIST.length() > 2) {
					FRIEND_DELETE_LIST = FRIEND_DELETE_LIST.substring(1,
							FRIEND_DELETE_LIST.length() - 1);
					if (!"".equals(FRIEND_DELETE_LIST)) {
						ArrayList<Integer> memberIds = new ArrayList<Integer>();
						try {
							if (FRIEND_DELETE_LIST.contains(",")) {
								for (String id : FRIEND_DELETE_LIST.split(",")) {
									memberIds.add(Integer.valueOf(id));
								}
							} else {
								memberIds.add(Integer
										.valueOf(FRIEND_DELETE_LIST));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						friendListService.deleteList(memberIds);
						// friendToGroupService.deleteList(memberIds);
					}
				}

				long LAST_DELETE_TIME = friendSimpleBean.getLAST_DELETE_TIME();
				long LAST_UPDATE_TIME = friendSimpleBean.getLAST_UPDATE_TIME();
				long START_SEARCH_TIME = friendSimpleBean
						.getSTART_SEARCH_TIME();

				LAST_DELETE_TIME = Math.max(Math.max(
						Math.max(LAST_UPDATE_TIME, LAST_DELETE_TIME),
						START_SEARCH_TIME), startUpdateTime);

				App.app.share.saveLongMessage("NotificationSetup",
						"startDeleteTimeFriendNew", LAST_DELETE_TIME);
			}

			int temp = 0;
			if (FRIEND_UPDATE_LIST != null) {
				temp = FRIEND_UPDATE_LIST.size();
				// TODO allNum
				friendListService.save(FRIEND_UPDATE_LIST);
				// friendToGroupService.save(FRIEND_UPDATE_LIST);
			}

			startRecord += temp;
			L.d(TAG, "getFriendUpdateList temp=" + temp);
			if (temp == maxResults) {// 获取下一页数据
				L.i(TAG, "getFriendUpdateList startUpdateTime="
						+ startUpdateTime + ",startDeleteTime="
						+ startDeleteTime);
				getFriendUpdateList(context, startUpdateTime, startDeleteTime,
						startRecord, maxResults, noPaging);
			} else {// 保存最后一次更新时间
				L.d(TAG, "getFriendUpdateList startRecord=" + startRecord);
				long LAST_UPDATE_TIME = friendSimpleBean.getLAST_UPDATE_TIME();
				long START_SEARCH_TIME = friendSimpleBean
						.getSTART_SEARCH_TIME();

				LAST_UPDATE_TIME = Math.max(
						Math.max(LAST_UPDATE_TIME, START_SEARCH_TIME),
						startDeleteTime);
				App.app.share.saveLongMessage("NotificationSetup",
						"startUpdateTimeFriendNew", LAST_UPDATE_TIME);
			}
		}
	}
}
