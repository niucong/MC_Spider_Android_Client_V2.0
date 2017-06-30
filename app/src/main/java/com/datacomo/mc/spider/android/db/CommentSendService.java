package com.datacomo.mc.spider.android.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.datacomo.mc.spider.android.bean.CommentSendBean;
import com.datacomo.mc.spider.android.url.L;

public class CommentSendService extends SQLiteOpenHelper {

	private static int version = 4;

	// 数据库名字
	private static final String name = "comment.db";

	private final String TAG = "CommentSendService";
	private static String COMMENT_LIST = "comment_send_list";

	private static CommentSendService service = null;

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "
				+ COMMENT_LIST
				+ "(_id integer primary key autoincrement,"
				+ " time Long, session_key varchar(30), "
				+ "id integer, name varchar(20), head varchar(100), "
				+ "gid integer, gname varchar(20), "
				+ "mType varchar(20), rid integer, content varchar(10000), "
				+ "at varchar(500), title varchar(100), rname varchar(20), "
				+ "atidname varchar(1000), sendStatus integer,"
				+ "gidname varchar(10000), filePaths varchar(10000), fileTemps varchar(100000))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 3 && newVersion == 4) {
			threeTofour(db);
		} else if (oldVersion == 2 && newVersion == 4) {
			twoToThree(db);
			threeTofour(db);
		} else if (oldVersion == 1 && newVersion == 4) {
			oneToTwo(db);
			twoToThree(db);
			threeTofour(db);
		}
	}

	private void threeTofour(SQLiteDatabase db) {
		db.execSQL("alter table " + COMMENT_LIST
				+ " add gidname varchar(10000)");
		db.execSQL("alter table " + COMMENT_LIST
				+ " add filePaths varchar(10000)");
		db.execSQL("alter table " + COMMENT_LIST
				+ " add fileTemps varchar(100000)");
	}

	private void twoToThree(SQLiteDatabase db) {
		db.execSQL("alter table " + COMMENT_LIST + " add sendStatus integer");
	}

	private void oneToTwo(SQLiteDatabase db) {
		db.execSQL("alter table " + COMMENT_LIST
				+ " add atidname varchar(1000)");
	}

	private CommentSendService(Context context) {
		super(context, name, null, version);
	}

	public static CommentSendService getService(Context context) {
		if (service == null)
			synchronized (CommentSendService.class) {
				if (service == null)
					service = new CommentSendService(context);
			}
		return service;
	}

	/**
	 * 
	 * @param entitys
	 */
	public void save(CommentSendBean csBean) {
		synchronized (COMMENT_LIST) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL(
						"insert into "
								+ COMMENT_LIST
								+ "(time, session_key, id, name, head, gid, gname, "
								+ "mType, rid, content, at, title, rname, atidname, sendStatus, gidname, filePaths, fileTemps) "
								+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						new Object[] { csBean.getTime(),
								csBean.getSession_key(), csBean.getId(),
								csBean.getName(), csBean.getHead(),
								csBean.getGid(), csBean.getGname(),
								csBean.getmType(), csBean.getRid(),
								csBean.getContent(), csBean.getAt(),
								csBean.getTitle(), csBean.getRname(),
								csBean.getAtidname(), csBean.getSendStatus(),
								csBean.getGidname(), csBean.getFilePaths(),
								csBean.getFileTemps() });
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	/**
	 * 修改圈博
	 * 
	 * @param time
	 * @param content
	 * @param updateTime
	 */
	public void updateNote(long time, CommentSendBean csBean) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.execSQL("delete from  " + COMMENT_LIST + " where time = '"
					+ time + "'");
			db.execSQL(
					"insert into "
							+ COMMENT_LIST
							+ "(time, session_key, id, name, head, gid, gname, "
							+ "mType, rid, content, at, title, rname, atidname, sendStatus, gidname, filePaths, fileTemps) "
							+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { csBean.getTime(), csBean.getSession_key(),
							csBean.getId(), csBean.getName(), csBean.getHead(),
							csBean.getGid(), csBean.getGname(),
							csBean.getmType(), csBean.getRid(),
							csBean.getContent(), csBean.getAt(),
							csBean.getTitle(), csBean.getRname(),
							csBean.getAtidname(), csBean.getSendStatus(),
							csBean.getGidname(), csBean.getFilePaths(),
							csBean.getFileTemps() });
		} finally {
			if (db != null)
				db.close();
		}
	}

	/**
	 * 修改发送状态
	 * 
	 * @param time
	 * @param sendStatus
	 */
	public void updateSendStatus(long time, int sendStatus) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.execSQL("update " + COMMENT_LIST
					+ " set sendStatus = ? where time = ?", new Object[] {
					sendStatus, time });
		} finally {
			if (db != null)
				db.close();
		}
	}

	public int getCount(String session_key) {
		L.d(TAG, "session_key=" + session_key);
		synchronized (COMMENT_LIST) {
			Cursor cursor = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select * from  " + COMMENT_LIST
						+ " where session_key = ?",
						new String[] { session_key });
				if (cursor != null)
					return cursor.getCount();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
			}
			return 0;
		}
	}

	public ArrayList<CommentSendBean> queryAll(String session_key) {
		L.d(TAG, "session_key=" + session_key);
		synchronized (COMMENT_LIST) {
			Cursor cursor = null;
			ArrayList<CommentSendBean> entitys = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select * from  " + COMMENT_LIST
						+ " where session_key = ? order by time desc",
						new String[] { session_key });
				if (cursor != null && cursor.moveToFirst()) {
					entitys = new ArrayList<CommentSendBean>();
					do {
						CommentSendBean csBean = new CommentSendBean();
						csBean.setTime(cursor.getLong(1));
						csBean.setSession_key(cursor.getString(2));
						csBean.setId(cursor.getInt(3));
						csBean.setName(cursor.getString(4));
						csBean.setHead(cursor.getString(5));
						csBean.setGid(cursor.getInt(6));
						csBean.setGname(cursor.getString(7));
						csBean.setmType(cursor.getString(8));
						csBean.setRid(cursor.getInt(9));
						csBean.setContent(cursor.getString(10));
						csBean.setAt(cursor.getString(11));
						csBean.setTitle(cursor.getString(12));
						csBean.setRname(cursor.getString(13));
						csBean.setAtidname(cursor.getString(14));
						csBean.setSendStatus(cursor.getInt(15));
						csBean.setGidname(cursor.getString(16));
						csBean.setFilePaths(cursor.getString(17));
						csBean.setFileTemps(cursor.getString(18));
						entitys.add(csBean);
					} while (cursor.moveToNext());
				} else {
					L.d(TAG, "cursor=null,0");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
			}
			return entitys;
		}
	}

	public ArrayList<CommentSendBean> queryById(String session_key, int id,
			int gid, int rid, String mType) {
		L.d(TAG, "session_key=" + session_key + ",id=" + id + ",gid=" + gid
				+ ",rid=" + rid + ",mType=" + mType);
		synchronized (COMMENT_LIST) {
			Cursor cursor = null;
			ArrayList<CommentSendBean> entitys = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db
						.rawQuery(
								"select * from  "
										+ COMMENT_LIST
										+ " where session_key = ? and id = ? and gid = ? and rid = ? and mType = ? order by time desc",
								new String[] { session_key, id + "", gid + "",
										rid + "", mType + "" });
				if (cursor != null && cursor.moveToFirst()) {
					entitys = new ArrayList<CommentSendBean>();
					do {
						CommentSendBean csBean = new CommentSendBean();
						csBean.setTime(cursor.getLong(1));
						csBean.setSession_key(cursor.getString(2));
						csBean.setId(cursor.getInt(3));
						csBean.setName(cursor.getString(4));
						csBean.setHead(cursor.getString(5));
						csBean.setGid(cursor.getInt(6));
						csBean.setGname(cursor.getString(7));
						csBean.setmType(cursor.getString(8));
						csBean.setRid(cursor.getInt(9));
						csBean.setContent(cursor.getString(10));
						csBean.setAt(cursor.getString(11));
						csBean.setTitle(cursor.getString(12));
						csBean.setRname(cursor.getString(13));
						csBean.setAtidname(cursor.getString(14));
						csBean.setSendStatus(cursor.getInt(15));
						entitys.add(csBean);
					} while (cursor.moveToNext());
				} else {
					L.d(TAG, "cursor=null,0");
				}
			} catch (Exception e) {
				e.printStackTrace();
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
	public void deleteComment(long time) {
		synchronized (COMMENT_LIST) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from " + COMMENT_LIST + " where time = '"
						+ time + "'");
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
		synchronized (COMMENT_LIST) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from " + COMMENT_LIST);
			} finally {
				if (db != null)
					db.close();
			}
		}
	}
}
