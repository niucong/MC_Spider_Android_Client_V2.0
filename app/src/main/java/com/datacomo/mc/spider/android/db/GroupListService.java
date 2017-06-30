package com.datacomo.mc.spider.android.db;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;

import com.datacomo.mc.spider.android.bean.GroupEntity;

public class GroupListService extends SQLiteOpenHelper {

	private static int version = 4;

	// 数据库名字
	private static final String name = "group.db";
	private static String GROUP_LIST = "group_list";
	private static String GROUP_LOCATION = "group_location";

	private static GroupListService service = null;

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "
				+ GROUP_LIST
				+ "(_id integer primary key autoincrement,"
				+ " id varchar(10), name varchar(20), type varchar(1),"
				+ " head varchar(100), openStatus varchar(1), "
				+ " groupNamePy varchar(100), groupNameJp varchar(20),"
				+ " groupProperty integer, groupType integer, contactTime Long)");

		db.execSQL("create table " + GROUP_LOCATION
				+ "(_id integer primary key autoincrement,"
				+ " id varchar(10), location varchar(100), latitude Double, "
				+ " longitude Double)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 3 && newVersion == 4) {
			dbThreeToFour(db);
		} else if (oldVersion == 2 && newVersion == 4) {
			dbTwoToThree(db);
			dbThreeToFour(db);
		} else if (oldVersion == 1 && newVersion == 4) {
			dbOneToTwo(db);
			dbTwoToThree(db);
			dbThreeToFour(db);
		}
	}

	private void dbThreeToFour(SQLiteDatabase db) {
		db.execSQL("create table " + GROUP_LOCATION
				+ "(_id integer primary key autoincrement,"
				+ " id varchar(10), location varchar(100), latitude Double, "
				+ " longitude Double)");
	}

	private void dbTwoToThree(SQLiteDatabase db) {
		db.execSQL("alter table " + GROUP_LIST + " add contactTime Long");
	}

	private void dbOneToTwo(SQLiteDatabase db) {
		// 圈子名字拼音
		db.execSQL("alter table " + GROUP_LIST + " add groupProperty integer");
		// 圈子名字简拼
		db.execSQL("alter table " + GROUP_LIST + " add groupType integer");
	}

	private GroupListService(Context context) {
		super(context, name, null, version);
	}

	public static GroupListService getService(Context context) {
		if (service == null)
			synchronized (GroupListService.class) {
				if (service == null)
					service = new GroupListService(context);
			}
		return service;
	}

	/**
	 * 
	 * @param entitys
	 */
	public void save(ArrayList<GroupEntity> entitys, Handler handler) {
		synchronized (GROUP_LIST) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				if (entitys != null) {
					int allNum = entitys.size();
					for (int i = 0; i < allNum; i++) {
						if (handler != null) {
							Message msg = new Message();
							msg.arg1 = i + 1;
							msg.arg2 = allNum;
							handler.sendMessage(msg);
						}
						GroupEntity entity = entitys.get(i);
						if (entity != null) {
							db.execSQL(
									"insert into "
											+ GROUP_LIST
											+ "(id,name,type,head,openStatus, groupNamePy, groupNameJp, groupProperty, groupType, contactTime) "
											+ "values(?,?,?,?,?,?,?,?,?,?)",
									new Object[] { entity.getId(),
											entity.getName(),
											entity.getGroupType(),
											entity.getHead(),
											entity.getOpenStatus(),
											entity.getGroupNamePy(),
											entity.getGroupNameJp(),
											entity.getGroupProperty(),
											entity.getGroupType(),
											System.currentTimeMillis() });
						}
					}
				}
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	/**
	 * 
	 * @param entitys
	 */
	public boolean save(ArrayList<GroupEntity> entitys) {
		synchronized (GROUP_LIST) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				if (entitys != null) {
					for (int i = 0; i < entitys.size(); i++) {
						GroupEntity entity = entitys.get(i);
						if (entity != null) {
							db.execSQL("delete from " + GROUP_LIST
									+ " where id = '" + entity.getId() + "'");
							db.execSQL(
									"insert into "
											+ GROUP_LIST
											+ "(id,name,type,head,openStatus, groupNamePy, groupNameJp, groupProperty, groupType, contactTime) "
											+ "values(?,?,?,?,?,?,?,?,?,?)",
									new Object[] { entity.getId(),
											entity.getName(),
											entity.getGroupType(),
											entity.getHead(),
											entity.getOpenStatus(),
											entity.getGroupNamePy(),
											entity.getGroupNameJp(),
											entity.getGroupProperty(),
											entity.getGroupType(),
											System.currentTimeMillis() });
						}
					}
				}
				return true;
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
	public void saveContactTime(String[] groupIds) {
		synchronized (GROUP_LIST) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				for (String groupId : groupIds) {
					try {
						String sql = "update " + GROUP_LIST
								+ " set contactTime = "
								+ System.currentTimeMillis() + " where id = "
								+ groupId;
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
	public ArrayList<GroupEntity> queryGroupListsByContactTime() {
		synchronized (GROUP_LIST) {
			Cursor cursor = null;
			ArrayList<GroupEntity> entitys = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select * from  " + GROUP_LIST
						+ " order by contactTime desc limit 0,10", null);
				if (cursor != null && cursor.moveToFirst()) {
					entitys = new ArrayList<GroupEntity>();
					do {
						if (cursor.getLong(10) != 0) {
							boolean type = false;
							if ("1".equals(cursor.getString(3))) {
								type = true;
							}
							GroupEntity groupEntity = new GroupEntity(
									cursor.getString(1), cursor.getString(2),
									"", "", type, false);
							groupEntity.setHead(cursor.getString(4));
							groupEntity.setOpenStatus(cursor.getString(5));
							groupEntity.setGroupNamePy(cursor.getString(6));
							groupEntity.setGroupNameJp(cursor.getString(7));
							groupEntity.setGroupProperty(cursor.getInt(8));
							groupEntity.setGroupType(cursor.getInt(9));
							groupEntity.setRecentCheck(true);
							entitys.add(groupEntity);
						}
					} while (cursor.moveToNext());
				}
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
	 * 删除
	 */
	public void delete() {
		synchronized (GROUP_LIST) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from  " + GROUP_LIST);
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	/**
	 * 删除列表
	 */
	public boolean deleteList(ArrayList<Integer> groupIds) {
		synchronized (GROUP_LIST) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				for (int id : groupIds) {
					db.execSQL("delete from " + GROUP_LIST + " where id = '"
							+ id + "'");
				}
				return true;
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	/**
	 * 查询圈子海报
	 * 
	 * @return
	 */
	public String queryGroupHead(String id) {
		synchronized (GROUP_LIST) {
			Cursor cursor = null;
			String head = "";
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select head from  " + GROUP_LIST
						+ " where id = " + id, null);
				if (cursor != null && cursor.moveToFirst()) {
					head = cursor.getString(0);
				}
			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
			}
			return head;
		}
	}

	/**
	 * 查询圈子私密类型 1 - 公开 2 - 私密 3 - 自定义
	 * 
	 * @return
	 */
	public String queryGroupType(String id) {
		synchronized (GROUP_LIST) {
			Cursor cursor = null;
			String openStatus = "";
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select openStatus from  " + GROUP_LIST
						+ " where id = " + id, null);
				if (cursor != null && cursor.moveToFirst()) {
					openStatus = cursor.getString(0);
				}
			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
			}
			return openStatus;
		}
	}

	/**
	 * 查询圈子列表
	 * 
	 * @return
	 */
	public ArrayList<GroupEntity> queryGroupLists() {
		synchronized (GROUP_LIST) {
			Cursor cursor = null;
			ArrayList<GroupEntity> entitys = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select * from " + GROUP_LIST, null);
				if (cursor != null && cursor.moveToFirst()) {
					entitys = new ArrayList<GroupEntity>();
					do {
						boolean type = false;
						if ("1".equals(cursor.getString(3))) {
							type = true;
						}
						GroupEntity groupEntity = new GroupEntity(
								cursor.getString(1), cursor.getString(2), "",
								"", type, false);
						groupEntity.setHead(cursor.getString(4));
						groupEntity.setOpenStatus(cursor.getString(5));
						groupEntity.setGroupNamePy(cursor.getString(6));
						groupEntity.setGroupNameJp(cursor.getString(7));
						groupEntity.setGroupProperty(cursor.getInt(8));
						groupEntity.setGroupType(cursor.getInt(9));
						entitys.add(groupEntity);
					} while (cursor.moveToNext());
				}
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
	 * 按拼音排序查询圈子列表
	 * 
	 * @return
	 */
	public ArrayList<GroupEntity> queryGroupListsByPy() {
		synchronized (GROUP_LIST) {
			Cursor cursor = null;
			ArrayList<GroupEntity> entitys = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select * from  " + GROUP_LIST
						+ " order by groupNamePy", null);
				if (cursor != null && cursor.moveToFirst()) {
					entitys = new ArrayList<GroupEntity>();
					ArrayList<GroupEntity> nums = new ArrayList<GroupEntity>();
					do {
						boolean type = false;
						if ("1".equals(cursor.getString(3))) {
							type = true;
						}
						GroupEntity groupEntity = new GroupEntity(
								cursor.getString(1), cursor.getString(2), "",
								"", type, false);
						groupEntity.setHead(cursor.getString(4));
						groupEntity.setOpenStatus(cursor.getString(5));
						groupEntity.setGroupNamePy(cursor.getString(6));
						groupEntity.setGroupNameJp(cursor.getString(7));
						groupEntity.setGroupProperty(cursor.getInt(8));
						groupEntity.setGroupType(cursor.getInt(9));

						String py = cursor.getString(6);
						if (py != null && py.length() > 0) {
							char c = py.trim().substring(0, 1).charAt(0);
							Pattern pattern = Pattern.compile("[a-zA-Z]{1}+$");
							if (pattern.matcher(c + "").matches()) {
								entitys.add(groupEntity);
							} else {
								nums.add(groupEntity);
							}
						} else {
							nums.add(groupEntity);
						}
					} while (cursor.moveToNext());
					entitys.addAll(nums);
				}
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
	 * 查询
	 * 
	 * @return
	 */
	public ArrayList<Object> queryGroupObjectLists() {
		synchronized (GROUP_LIST) {
			Cursor cursor = null;
			ArrayList<Object> entitys = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select * from  " + GROUP_LIST, null);
				if (cursor != null && cursor.moveToFirst()) {
					entitys = new ArrayList<Object>();
					do {
						boolean type = false;
						if ("1".equals(cursor.getString(3))) {
							type = true;
						}
						GroupEntity groupEntity = new GroupEntity(
								cursor.getString(1), cursor.getString(2), "",
								"", type, false);
						groupEntity.setHead(cursor.getString(4));
						groupEntity.setOpenStatus(cursor.getString(5));
						groupEntity.setGroupNamePy(cursor.getString(6));
						groupEntity.setGroupNameJp(cursor.getString(7));
						groupEntity.setGroupProperty(cursor.getInt(8));
						groupEntity.setGroupType(cursor.getInt(9));
						entitys.add(groupEntity);
					} while (cursor.moveToNext());
				}
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
	 * 查询所优优工作圈子Id
	 * 
	 * @return
	 */
	public ArrayList<String> queryGroupIds() {
		synchronized (GROUP_LIST) {
			Cursor cursor = null;
			ArrayList<String> ids = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select id from  " + GROUP_LIST, null);
				if (cursor != null && cursor.moveToFirst()) {
					ids = new ArrayList<String>();
					do {
						ids.add(cursor.getString(0));
					} while (cursor.moveToNext());
				}
			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
			}
			return ids;
		}
	}

	/**
	 * 按名字查询圈子列表
	 * 
	 * @return
	 */
	public ArrayList<GroupEntity> searchGroupLists(String key) {
		Cursor cursor = null;
		ArrayList<GroupEntity> entitys = null;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			cursor = db.rawQuery("select * from  " + GROUP_LIST
					+ " where name like '%" + key + "%' or groupNamePy like '%"
					+ key + "%' or groupNameJp like '%" + key + "%'", null);
			if (cursor != null && cursor.moveToFirst()) {
				entitys = new ArrayList<GroupEntity>();
				do {
					boolean type = false;
					if ("1".equals(cursor.getString(3))) {
						type = true;
					}
					GroupEntity groupEntity = new GroupEntity(
							cursor.getString(1), cursor.getString(2), "", "",
							type, false);
					groupEntity.setHead(cursor.getString(4));
					groupEntity.setOpenStatus(cursor.getString(5));
					groupEntity.setGroupNamePy(cursor.getString(6));
					groupEntity.setGroupNameJp(cursor.getString(7));
					groupEntity.setGroupProperty(cursor.getInt(8));
					groupEntity.setGroupType(cursor.getInt(9));
					entitys.add(groupEntity);
				} while (cursor.moveToNext());
			}
		} finally {
			if (cursor != null)
				cursor.close();
			if (db != null)
				db.close();
		}
		return entitys;
	}

	/**
	 * 
	 * @param entitys
	 */
	public void saveLocation(String id, String location, double latitude,
			double longitude) {
		synchronized (GROUP_LOCATION) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("insert into " + GROUP_LOCATION
						+ "(id,location,latitude,longitude) "
						+ "values(?,?,?,?)", new Object[] { id, location,
						latitude, longitude });
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	/**
	 * 
	 * @param entitys
	 */
	public String getLocation(String id) {
		synchronized (GROUP_LOCATION) {
			Cursor cursor = null;
			String location = "";
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select location from  " + GROUP_LOCATION
						+ " where id = " + id, null);
				if (cursor != null && cursor.moveToFirst()) {
					location = cursor.getString(0);
				}
			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
			}
			return location;
		}
	}

}
