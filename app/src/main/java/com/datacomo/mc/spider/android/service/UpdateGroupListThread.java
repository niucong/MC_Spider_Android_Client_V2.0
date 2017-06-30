package com.datacomo.mc.spider.android.service;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.db.GroupListService;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.map.MapGroupSimpleBean;
import com.datacomo.mc.spider.android.url.L;

public class UpdateGroupListThread {
	private static final String TAG = "UpdateGroupListThread";

	private static boolean isUpdate = false;
	// private static int allNum = 0;

	private static GroupListService groupService;

	public static void updateGroupList(final Context context, Handler handler) {
		if (isUpdate)
			return;
		isUpdate = true;

		groupService = GroupListService.getService(context);

		// 数据库读取更新时间
		long startUpdateTime = 0;
		long startDeleteTime = 0;

		// String VersionName = sharedMessage.getStringMessage(
		// "NotificationSetup", "VersionName", "");
		// if ("2.1.6".equals(VersionName)) {
		startUpdateTime = App.app.share.getLongMessage("NotificationSetup",
				"startUpdateTimeGroup", 0);
		startDeleteTime = App.app.share.getLongMessage("NotificationSetup",
				"startDeleteTimeGroup", 0);
		L.d(TAG, "updateFriendList startUpdateTime=" + startUpdateTime
				+ ",startDeleteTime=" + startDeleteTime);
		// } else {
		// groupService.delete();
		// }

		int num = 0;
		int maxResults = 200;

		if (startUpdateTime == 0) {
			// try {
			// MCResult mc = APIRequestServers.friendNum(context, "0");
			// if (mc.getResultCode() == 1) {
			// allNum = (Integer) mc.getResult();
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		} else {
			handler = null;
		}

		// new Thread() {
		// public void run() {
		try {
			getGroupUpdateList(context, startUpdateTime, startDeleteTime, num,
					maxResults, "true");
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
		isUpdate = false;
		// };
		// }.start();
	}

	private static void getGroupUpdateList(Context context,
			long startUpdateTime, long startDeleteTime, int startRecord,
			int maxResults, String noPaging) throws Exception {
		String ids = "";
		ArrayList<String> idlist = groupService.queryGroupIds();
		if (idlist != null) {
			int size = idlist.size();
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					if (i == size - 1) {
						ids += idlist.get(i);
					} else {
						ids += idlist.get(i) + ",";
					}
				}
			} else {
				ids = ",";
			}
		} else {
			ids = ",";
		}
		MCResult mc = APIRequestServers.groupUpdateList(context,
				startUpdateTime + "", startDeleteTime + "", startRecord + "",
				maxResults + "", noPaging, ids);
		if (mc.getResultCode() == 1) {
			MapGroupSimpleBean simpleBean = (MapGroupSimpleBean) mc.getResult();
			String FRIEND_DELETE_LIST = simpleBean.getGROUP_DELETE_LIST();
			ArrayList<GroupEntity> FRIEND_UPDATE_LIST = simpleBean
					.getGroupEntityList();

			boolean deleteSuccess = false;
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
									if (id != null && id.contains("\"")) {
										id = id.replace("\"", "");
									}
									memberIds.add(Integer.valueOf(id));
								}
							} else {
								if (FRIEND_DELETE_LIST != null
										&& FRIEND_DELETE_LIST.contains("\"")) {
									FRIEND_DELETE_LIST = FRIEND_DELETE_LIST
											.replace("\"", "");
								}
								memberIds.add(Integer
										.valueOf(FRIEND_DELETE_LIST));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						deleteSuccess = groupService.deleteList(memberIds);
					}
				}

				long LAST_DELETE_TIME = simpleBean.getLAST_DELETE_TIME();
				long LAST_UPDATE_TIME = simpleBean.getLAST_UPDATE_TIME();
				long START_SEARCH_TIME = simpleBean.getSTART_SEARCH_TIME();

				LAST_DELETE_TIME = Math.max(Math.max(
						Math.max(LAST_UPDATE_TIME, LAST_DELETE_TIME),
						START_SEARCH_TIME), startUpdateTime);
				if (deleteSuccess || startDeleteTime == 0) {
					App.app.share.saveLongMessage("NotificationSetup",
							"startDeleteTimeGroup", LAST_DELETE_TIME);
				} else {
					App.app.share.saveLongMessage("NotificationSetup",
							"startDeleteTimeGroup", startDeleteTime);
				}
			}

			int temp = 0;
			// boolean updateSuccess = false;
			if (FRIEND_UPDATE_LIST != null) {
				temp = FRIEND_UPDATE_LIST.size();
				groupService.save(FRIEND_UPDATE_LIST);
			}

			startRecord += temp;
			L.d(TAG, "getGroupUpdateList temp=" + temp);
			if (temp == maxResults) {// 获取下一页数据
				L.i(TAG, "getGroupUpdateList startUpdateTime="
						+ startUpdateTime + ",startDeleteTime="
						+ startDeleteTime);
				getGroupUpdateList(context, startUpdateTime, startDeleteTime,
						startRecord, maxResults, noPaging);
			} else {// 保存最后一次更新时间
				L.d(TAG, "getGroupUpdateList startRecord=" + startRecord);
				long LAST_UPDATE_TIME = simpleBean.getLAST_UPDATE_TIME();
				long START_SEARCH_TIME = simpleBean.getSTART_SEARCH_TIME();

				LAST_UPDATE_TIME = Math.max(
						Math.max(LAST_UPDATE_TIME, START_SEARCH_TIME),
						startDeleteTime);
				App.app.share.saveLongMessage("NotificationSetup",
						"startUpdateTimeGroup", LAST_UPDATE_TIME);
			}
		}
	}
}
