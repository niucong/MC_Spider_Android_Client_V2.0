package com.datacomo.mc.spider.android.db;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.datacomo.mc.spider.android.net.been.FriendSimpleBean;

public class FriendListService extends SQLiteOpenHelper {

	private static int version = 2;

	// 数据库名字
	private static final String name = "friend.db";

	private static String FRIEND_LIST = "friend_list";
	// private static String FRIEND_TO_GROUP = "friend_to_group";

	private static FriendListService service = null;

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 朋友列表
		db.execSQL("create table " + FRIEND_LIST
				+ "(_id integer primary key autoincrement,"
				+ " memberId integer," + " memberName varchar(100),"
				+ " memberNamePY varchar(500)," + " memberNameJP varchar(100),"
				+ " memberHeadUrl varchar(100),"
				+ " memberHeadPath varchar(500)," + " memberSex integer,"
				+ " memberMood varchar(500)," + " memberPhone varchar(20),"
				+ " friendName varchar(100)," + " friendNamePY varchar(500),"
				+ " friendNameJP varchar(100),"
				+ " friendGroupId varchar(500), contactTime Long)");

		// 朋友圈-朋友
		// db.execSQL("create table " + FRIEND_TO_GROUP
		// + "(_id integer primary key autoincrement,"
		// + " memberId integer," + " friendGroupId integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1 && newVersion == 2) {
			db.execSQL("alter table " + FRIEND_LIST + " add contactTime Long");
		}
	}

	private FriendListService(Context context) {
		super(context, name, null, version);
	}

	public static FriendListService getService(Context context) {
		if (service == null)
			synchronized (FriendListService.class) {
				if (service == null)
					service = new FriendListService(context);
			}
		return service;
	}

	/**
	 * 
	 * @param entitys
	 */
	public void save(List<FriendSimpleBean> entitys) {
		synchronized (FRIEND_LIST) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				for (int i = 0; i < entitys.size(); i++) {
					FriendSimpleBean entity = entitys.get(i);
					if (entity != null) {
						// memberId,memberName,memberNamePY,memberNameJP,memberHeadUrl,memberHeadPath,memberSex,memberMood,friendName,friendNamePY,friendNameJP
						db.execSQL("delete from " + FRIEND_LIST
								+ " where memberId = '" + entity.getMemberId()
								+ "'");
						String friendName = entity.getFriendName();
						if (friendName == null || "".equals(friendName))
							friendName = entity.getMemberName();
						String friendNamePY = entity.getFriendNamePY();
						if (friendNamePY == null || "".equals(friendNamePY))
							friendNamePY = entity.getMemberNamePY();
						String friendNameJP = entity.getFriendNameJP();
						if (friendNameJP == null || "".equals(friendNameJP))
							friendNameJP = entity.getMemberNameJP();

						db.execSQL(
								"insert into "
										+ FRIEND_LIST
										+ "(memberId,memberName,memberNamePY,memberNameJP,memberHeadUrl,memberHeadPath,memberSex,memberMood,memberPhone,friendName,friendNamePY,friendNameJP,friendGroupId,contactTime) "
										+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
								new Object[] { entity.getMemberId(),
										entity.getMemberName(),
										entity.getMemberNamePY(),
										entity.getMemberNameJP(),
										entity.getMemberHeadUrl(),
										entity.getMemberHeadPath(),
										entity.getMemberSex(),
										entity.getMemberMood(),
										entity.getMemberPhone(), friendName,
										friendNamePY, friendNameJP,
										entity.getFriendGroupId(),
										System.currentTimeMillis() });
					}
				}
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	/**
	 * 保存最近联系时间
	 * 
	 * @param memberIds
	 */
	public void saveContactTime(String[] memberIds) {
		synchronized (FRIEND_LIST) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				for (String memberId : memberIds) {
					try {
						String sql = "update " + FRIEND_LIST
								+ " set contactTime = "
								+ System.currentTimeMillis()
								+ " where memberId = " + memberId;
						db.execSQL(sql);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	/**
	 * 最近联系的5条记录
	 * 
	 * @return
	 */
	public ArrayList<FriendSimpleBean> queryFriendListsByContactTime() {
		synchronized (FRIEND_LIST) {
			Cursor cursor = null;
			ArrayList<FriendSimpleBean> entitys = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select * from  " + FRIEND_LIST
						+ " order by contactTime desc limit 0,10", null);
				if (cursor != null && cursor.moveToFirst()) {
					entitys = new ArrayList<FriendSimpleBean>();
					do {
						if (cursor.getLong(14) != 0) {
							FriendSimpleBean bean = new FriendSimpleBean();
							bean.setMemberId(cursor.getInt(1));
							bean.setMemberName(cursor.getString(2));
							bean.setMemberNamePY(cursor.getString(3));
							bean.setMemberNameJP(cursor.getString(4));
							bean.setMemberHeadUrl(cursor.getString(5));
							bean.setMemberHeadPath(cursor.getString(6));
							bean.setMemberSex(cursor.getInt(7));
							bean.setMemberMood(cursor.getString(8));
							bean.setMemberPhone(cursor.getString(9));
							bean.setFriendName(cursor.getString(10));
							bean.setFriendNamePY(cursor.getString(11));
							bean.setFriendNameJP(cursor.getString(12));
							bean.setFriendGroupId(cursor.getString(13));
							bean.setRecentCheck(true);
							entitys.add(bean);
						}
					} while (cursor.moveToNext());
				}
			} catch (Exception e) {

			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
			}
			return entitys;
		}
	}

	/**
	 * 删除列表
	 */
	public void deleteList(ArrayList<Integer> memberIds) {
		synchronized (FRIEND_LIST) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				for (int memberId : memberIds) {
					db.execSQL("delete from " + FRIEND_LIST
							+ " where memberId = '" + memberId + "'");
				}
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	/**
	 * 删除所有
	 */
	public void deleteAll() {
		synchronized (FRIEND_LIST) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from  " + FRIEND_LIST);
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	/**
	 * 获取注册和未注册数量
	 * 
	 * @param registerStatus
	 * @return
	 */
	public int getCount() {
		Cursor cursor = null;
		int count = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			cursor = db.rawQuery("select count(0) from " + FRIEND_LIST, null);
			cursor.moveToFirst();
			count = Integer.parseInt(cursor.getString(0));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (db != null)
				db.close();
		}
		return count;
	}

	/**
	 * 查询所有朋友列表
	 * 
	 * @return
	 */
	public ArrayList<FriendSimpleBean> queryFriendListsByPy(int id) {
		synchronized (FRIEND_LIST) {
			Cursor cursor = null;
			ArrayList<FriendSimpleBean> entitys = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select * from  " + FRIEND_LIST
						+ " order by friendNamePy", null);
				if (cursor != null && cursor.moveToFirst()) {
					entitys = new ArrayList<FriendSimpleBean>();
					ArrayList<FriendSimpleBean> nums = new ArrayList<FriendSimpleBean>();
					do {
						if (id == 0 || id != cursor.getInt(1)) {
							FriendSimpleBean bean = new FriendSimpleBean();
							bean.setMemberId(cursor.getInt(1));
							bean.setMemberName(cursor.getString(2));
							bean.setMemberNamePY(cursor.getString(3));
							bean.setMemberNameJP(cursor.getString(4));
							bean.setMemberHeadUrl(cursor.getString(5));
							bean.setMemberHeadPath(cursor.getString(6));
							bean.setMemberSex(cursor.getInt(7));
							bean.setMemberMood(cursor.getString(8));
							bean.setMemberPhone(cursor.getString(9));
							bean.setFriendName(cursor.getString(10));
							bean.setFriendNamePY(cursor.getString(11));
							bean.setFriendNameJP(cursor.getString(12));
							bean.setFriendGroupId(cursor.getString(13));

							String py = cursor.getString(11);
							if (py != null && py.length() > 0) {
								char c = py.trim().substring(0, 1).charAt(0);
								Pattern pattern = Pattern
										.compile("[a-zA-Z]{1}+$");
								if (pattern.matcher(c + "").matches()) {
									entitys.add(bean);
								} else {
									nums.add(bean);
								}
							} else {
								nums.add(bean);
							}
						}
					} while (cursor.moveToNext());
					entitys.addAll(nums);
				}
			} catch (Exception e) {

			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
			}
			return entitys;
		}
	}
	//
	// /**
	// * 查询某些朋友圈朋友列表
	// *
	// * @return
	// */
	// public List<FriendSimpleBean> queryFriendListsByIds(int[] memberIds) {
	// synchronized (FRIEND_LIST) {
	// Cursor cursor = null;
	// List<FriendSimpleBean> entitys = null;
	// SQLiteDatabase db = this.getWritableDatabase();
	// try {
	// entitys = new ArrayList<FriendSimpleBean>();
	// for (int memberId : memberIds) {
	// cursor = db.rawQuery("select * from  " + FRIEND_LIST
	// + " where memberId = '" + memberId + "'", null);
	// if (cursor != null && cursor.moveToFirst()) {
	// do {
	// FriendSimpleBean bean = new FriendSimpleBean();
	// bean.setMemberId(cursor.getInt(1));
	// bean.setMemberName(cursor.getString(2));
	// bean.setMemberNamePY(cursor.getString(3));
	// bean.setMemberNameJP(cursor.getString(4));
	// bean.setMemberHeadUrl(cursor.getString(5));
	// bean.setMemberHeadPath(cursor.getString(6));
	// bean.setMemberSex(cursor.getInt(7));
	// bean.setMemberMood(cursor.getString(8));
	// bean.setMemberPhone(cursor.getString(9));
	// bean.setFriendName(cursor.getString(10));
	// bean.setFriendNamePY(cursor.getString(11));
	// bean.setFriendNameJP(cursor.getString(12));
	// bean.setFriendGroupId(cursor.getString(13));
	// entitys.add(bean);
	// } while (cursor.moveToNext());
	// }
	// }
	// } finally {
	// if (cursor != null)
	// cursor.close();
	// if (db != null)
	// db.close();
	// }
	// Collections.sort(entitys, new Comparator<FriendSimpleBean>() {
	// @Override
	// public int compare(FriendSimpleBean bean1,
	// FriendSimpleBean bean2) {
	// return bean1.getMemberNamePY().toLowerCase()
	// .compareTo(bean2.getMemberNamePY().toLowerCase());
	// }
	// });
	// return entitys;
	// }
	// }
	//
	// /**
	// * 查询某个朋友
	// *
	// * @return
	// */
	// public FriendSimpleBean queryFriendById(int memberId) {
	// synchronized (FRIEND_LIST) {
	// Cursor cursor = null;
	// FriendSimpleBean bean = null;
	// SQLiteDatabase db = this.getWritableDatabase();
	// try {
	// cursor = db.rawQuery("select * from  " + FRIEND_LIST
	// + " where memberId = '" + memberId + "'", null);
	// if (cursor != null && cursor.moveToFirst()) {
	// do {
	// bean = new FriendSimpleBean();
	// bean.setMemberId(cursor.getInt(1));
	// bean.setMemberName(cursor.getString(2));
	// bean.setMemberNamePY(cursor.getString(3));
	// bean.setMemberNameJP(cursor.getString(4));
	// bean.setMemberHeadUrl(cursor.getString(5));
	// bean.setMemberHeadPath(cursor.getString(6));
	// bean.setMemberSex(cursor.getInt(7));
	// bean.setMemberMood(cursor.getString(8));
	// bean.setMemberPhone(cursor.getString(9));
	// bean.setFriendName(cursor.getString(10));
	// bean.setFriendNamePY(cursor.getString(11));
	// bean.setFriendNameJP(cursor.getString(12));
	// bean.setFriendGroupId(cursor.getString(13));
	// } while (cursor.moveToNext());
	// }
	// } finally {
	// if (cursor != null)
	// cursor.close();
	// if (db != null)
	// db.close();
	// }
	// return bean;
	// }
	// }
	//
	// /**
	// * 查询某个朋友所在的朋友圈
	// *
	// * @return
	// */
	// public ArrayList<String> queryFriendGroupIds(int memberId) {
	// synchronized (FRIEND_LIST) {
	// Cursor cursor = null;
	// SQLiteDatabase db = this.getWritableDatabase();
	// ArrayList<String> Idlist = new ArrayList<String>();
	// String friendGroupIds = null;
	// try {
	// cursor = db.rawQuery("select friendGroupId from  "
	// + FRIEND_LIST + " where memberId = '" + memberId + "'",
	// null);
	// if (cursor != null && cursor.moveToFirst()) {
	// do {
	// friendGroupIds = cursor.getString(0);
	// } while (cursor.moveToNext());
	// }
	// } finally {
	// if (cursor != null)
	// cursor.close();
	// if (db != null)
	// db.close();
	// }
	//
	// if (friendGroupIds != null && friendGroupIds.length() > 2) {
	// friendGroupIds = friendGroupIds.substring(1,
	// friendGroupIds.length() - 1);
	// if (friendGroupIds.contains(",")) {
	// for (String friendGroupId : friendGroupIds.split(",")) {
	// Idlist.add(friendGroupId);
	// }
	// } else if (!"".equals(friendGroupIds)) {
	// Idlist.add(friendGroupIds);
	// }
	// }
	// return Idlist;
	// }
	// }
	//
	// /**
	// * 查询所有朋友列表
	// *
	// * @return
	// */
	// public Object[] searchFriendLists(String key) {
	// Cursor cursor = null;
	// ArrayList<FriendSimpleBean> entitys = null;
	// ArrayList<String> nameList = null;
	// Object[] objs = new Object[2];
	// SQLiteDatabase db = this.getWritableDatabase();
	// try {
	// cursor = db.rawQuery("select * from  " + FRIEND_LIST
	// + " where memberName like '%" + key
	// + "%' or memberNamePY like '%" + key
	// + "%' or memberNameJP like '%" + key
	// + "%' or friendName like '%" + key
	// + "%' or friendNamePY like '%" + key
	// + "%' or friendNameJP like '%" + key + "%'", null);
	// if (cursor != null && cursor.moveToFirst()) {
	// entitys = new ArrayList<FriendSimpleBean>();
	// nameList = new ArrayList<String>();
	// do {
	// FriendSimpleBean bean = new FriendSimpleBean();
	// bean.setMemberId(cursor.getInt(1));
	// bean.setMemberName(cursor.getString(2));
	// bean.setMemberNamePY(cursor.getString(3));
	// bean.setMemberNameJP(cursor.getString(4));
	// bean.setMemberHeadUrl(cursor.getString(5));
	// bean.setMemberHeadPath(cursor.getString(6));
	// bean.setMemberSex(cursor.getInt(7));
	// bean.setMemberMood(cursor.getString(8));
	// bean.setMemberPhone(cursor.getString(9));
	// String friendName = cursor.getString(10);
	// bean.setFriendName(friendName);
	// bean.setFriendNamePY(cursor.getString(11));
	// bean.setFriendNameJP(cursor.getString(12));
	// bean.setFriendGroupId(cursor.getString(13));
	// entitys.add(bean);
	// if (friendName != null && !"".equals(friendName)) {
	// nameList.add(cursor.getString(2) + "（" + friendName
	// + "）");
	// } else {
	// nameList.add(cursor.getString(2));
	// }
	// } while (cursor.moveToNext());
	// objs[0] = entitys;
	// objs[1] = nameList;
	// }
	// } catch (Exception e) {
	//
	// } finally {
	// if (cursor != null)
	// cursor.close();
	// if (db != null)
	// db.close();
	// }
	// return objs;
	// }
	//
	// /**
	// *
	// * @param entitys
	// */
	// public void saveTo(List<FriendSimpleBean> entitys) {
	// SQLiteDatabase db = this.getWritableDatabase();
	// try {
	// if (entitys != null && entitys.size() > 0) {
	// for (int i = 0; i < entitys.size(); i++) {
	// FriendSimpleBean entity = entitys.get(i);
	// if (entity != null) {
	// int memberId = entity.getMemberId();
	// db.execSQL("delete from " + FRIEND_TO_GROUP
	// + " where memberId = " + memberId);
	// String friendGroupIds = entity.getFriendGroupId();
	//
	// if (friendGroupIds != null
	// && friendGroupIds.length() > 2) {
	// friendGroupIds = friendGroupIds.substring(1,
	// friendGroupIds.length() - 1);
	// if (friendGroupIds.contains(",")) {
	// for (String friendGroupId : friendGroupIds
	// .split(",")) {
	// db.execSQL(
	// "insert into "
	// + FRIEND_TO_GROUP
	// + "(memberId,friendGroupId) "
	// + "values(?,?)",
	// new Object[] {
	// memberId,
	// Integer.valueOf(friendGroupId) });
	// }
	// } else if (!"".equals(friendGroupIds)) {
	// db.execSQL(
	// "insert into " + FRIEND_TO_GROUP
	// + "(memberId,friendGroupId) "
	// + "values(?,?)",
	// new Object[] { memberId,
	// Integer.valueOf(friendGroupIds) });
	// }
	// }
	// }
	// }
	// }
	// } finally {
	// if (db != null)
	// db.close();
	// }
	// }
	//
	// /**
	// * 删除列表
	// */
	// public void deleteListTo(ArrayList<Integer> memberIds) {
	// SQLiteDatabase db = this.getWritableDatabase();
	// try {
	// for (int memberId : memberIds) {
	// db.execSQL("delete from " + FRIEND_TO_GROUP
	// + " where memberId = " + memberId);
	// }
	// } finally {
	// if (db != null)
	// db.close();
	// }
	// }
	//
	// /**
	// * 删除所有
	// */
	// public void deleteAllTo() {
	// SQLiteDatabase db = this.getWritableDatabase();
	// try {
	// db.execSQL("delete from  " + FRIEND_TO_GROUP);
	// } finally {
	// if (db != null)
	// db.close();
	// }
	// }
	//
	// /**
	// * 查询某个朋友圈朋友Id列表
	// *
	// * @return
	// */
	// public int[] queryFriendIdsTo(int friendGroupId) {
	// SQLiteDatabase db = this.getWritableDatabase();
	// Cursor cursor = null;
	// try {
	// cursor = db.rawQuery("select * from  " + FRIEND_TO_GROUP
	// + " where friendGroupId = " + friendGroupId, null);
	// int[] friendIds = new int[cursor.getCount()];
	// if (cursor != null && cursor.moveToFirst()) {
	// do {
	// friendIds[cursor.getPosition()] = cursor.getInt(1);
	// } while (cursor.moveToNext());
	// }
	// return friendIds;
	// } finally {
	// if (cursor != null)
	// cursor.close();
	// if (db != null)
	// db.close();
	// }
	// }
}
