package com.datacomo.mc.spider.android.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.datacomo.mc.spider.android.net.been.FriendSimpleBean;

public class FriendToGroupService extends DataBaseService {

	private String TABLE_NAME = DataBaseHelper.FRIEND_TO_GROUP;

	public FriendToGroupService(Context context) {
		super(context);
	}

	/**
	 * 
	 * @param entitys
	 */
	public void save(List<FriendSimpleBean> entitys) {
		try {
			wDB = this.openWDB();
			if (entitys != null && entitys.size() > 0) {
				for (int i = 0; i < entitys.size(); i++) {
					FriendSimpleBean entity = entitys.get(i);
					if (entity != null) {
						int memberId = entity.getMemberId();
						wDB.execSQL("delete from " + TABLE_NAME
								+ " where memberId = " + memberId);
						String friendGroupIds = entity.getFriendGroupId();

						if (friendGroupIds != null
								&& friendGroupIds.length() > 2) {
							friendGroupIds = friendGroupIds.substring(1,
									friendGroupIds.length() - 1);
							if (friendGroupIds.contains(",")) {
								for (String friendGroupId : friendGroupIds
										.split(",")) {
									wDB.execSQL(
											"insert into "
													+ TABLE_NAME
													+ "(memberId,friendGroupId) "
													+ "values(?,?)",
											new Object[] {
													memberId,
													Integer.valueOf(friendGroupId) });
								}
							} else if (!"".equals(friendGroupIds)) {
								wDB.execSQL(
										"insert into " + TABLE_NAME
												+ "(memberId,friendGroupId) "
												+ "values(?,?)",
										new Object[] { memberId,
												Integer.valueOf(friendGroupIds) });
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 删除列表
	 */
	public void deleteList(ArrayList<Integer> memberIds) {
		try {
			wDB = this.openWDB();
			for (int memberId : memberIds) {
				wDB.execSQL("delete from " + TABLE_NAME + " where memberId = "
						+ memberId);
			}
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 删除所有
	 */
	public void deleteAll() {
		try {
			wDB = this.openWDB();
			wDB.execSQL("delete from  " + TABLE_NAME);
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 查询某个朋友圈朋友Id列表
	 * 
	 * @return
	 */
	public int[] queryFriendIds(int friendGroupId) {
		Cursor cursor = null;
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select * from  " + TABLE_NAME
					+ " where friendGroupId = " + friendGroupId, null);
			int[] friendIds = new int[cursor.getCount()];
			if (cursor != null && cursor.moveToFirst()) {
				do {
					friendIds[cursor.getPosition()] = cursor.getInt(1);
				} while (cursor.moveToNext());
			}
			return friendIds;
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
	}
}
