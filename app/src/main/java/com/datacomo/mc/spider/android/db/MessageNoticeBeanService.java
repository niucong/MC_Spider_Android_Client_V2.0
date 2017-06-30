package com.datacomo.mc.spider.android.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.net.been.MessageNoticeBean;

public class MessageNoticeBeanService extends SQLiteOpenHelper {
	// private final String TAG = "ChatMessageBeanService";
	public static String MSG_NOTICE = "msg_notice";

	private static int version = 1;
	// 数据库名字
	private static final String name = "msg.db";

	private static MessageNoticeBeanService service = null;

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "
				+ MSG_NOTICE
				+ "(_id integer primary key autoincrement,"
				+ " noticeId integer, sendMemberId integer, sendMemberName varchar(100),"
				+ " sendObjectId integer, sendObjectName varchar(100),objectId integer,"
				+ " objectName varchar(100), objectType varchar(100), actionType varchar(100),"
				+ " noticeContent varchar(200), noticeTime varchar(200), isRead varchar(10),"
				+ " sendMemberHeadUrl varchar(100), sendMemberHeadPath varchar(100), objectUrl varchar(100),"
				+ " objectPath varchar(100), remarkContent varchar(200))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1 && newVersion == 2) {

		}
	}

	private MessageNoticeBeanService(Context context) {
		super(context, name, null, version);
	}

	public static MessageNoticeBeanService getService(Context context) {
		if (service == null)
			synchronized (MessageNoticeBeanService.class) {
				if (service == null)
					service = new MessageNoticeBeanService(context);
			}
		return service;
	}

	public void save(List<MessageNoticeBean> list) {
		synchronized (MSG_NOTICE) {
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = null;
			try {
				db.execSQL("delete from  " + MSG_NOTICE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				for (MessageNoticeBean mnb : list) {
					db.execSQL(
							"insert into "
									+ MSG_NOTICE
									+ "(noticeId, sendMemberId, sendMemberName, sendObjectId, sendObjectName,"
									+ " objectId, objectName, objectType, actionType, noticeContent,"
									+ " noticeTime, isRead, sendMemberHeadUrl, sendMemberHeadPath, objectUrl,"
									+ " objectPath, remarkContent) "
									+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
							new Object[] { mnb.getNoticeId(),
									mnb.getSendMemberId(),
									mnb.getSendMemberName(),
									mnb.getSendObjectId(),
									mnb.getSendObjectName(), mnb.getObjectId(),
									mnb.getObjectName(), mnb.getObjectType(),
									mnb.getActionType(),
									mnb.getNoticeContent(),
									mnb.getNoticeTime(), mnb.isRead() + "",
									mnb.getSendMemberHeadUrl(),
									mnb.getSendMemberHeadPath(),
									mnb.getObjectUrl(), mnb.getObjectPath(),
									mnb.getRemarkContent() });
				}

				try {
					JSONObject time = new JSONObject(list.get(0)
							.getNoticeTime());
					App.app.share.saveLongMessage("program", "noticeTime",
							time.getLong("time"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
			}
		}
	}

	public void save(MessageNoticeBean mnb) {
		synchronized (MSG_NOTICE) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				try {
					JSONObject time = new JSONObject(mnb.getNoticeTime());
					App.app.share.saveLongMessage("program", "noticeTime",
							time.getLong("time"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				db.execSQL(
						"insert into "
								+ MSG_NOTICE
								+ "(noticeId, sendMemberId, sendMemberName, sendObjectId, sendObjectName,"
								+ " objectId, objectName, objectType, actionType, noticeContent,"
								+ " noticeTime, isRead, sendMemberHeadUrl, sendMemberHeadPath, objectUrl,"
								+ " objectPath, remarkContent) "
								+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						new Object[] { mnb.getNoticeId(),
								mnb.getSendMemberId(), mnb.getSendMemberName(),
								mnb.getSendObjectId(), mnb.getSendObjectName(),
								mnb.getObjectId(), mnb.getObjectName(),
								mnb.getObjectType(), mnb.getActionType(),
								mnb.getNoticeContent(), mnb.getNoticeTime(),
								mnb.isRead() + "", mnb.getSendMemberHeadUrl(),
								mnb.getSendMemberHeadPath(),
								mnb.getObjectUrl(), mnb.getObjectPath(),
								mnb.getRemarkContent() });
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	public ArrayList<MessageNoticeBean> queryList() {
		synchronized (MSG_NOTICE) {
			ArrayList<MessageNoticeBean> list = null;
			Cursor cursor = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select * from  " + MSG_NOTICE, null);
				if (cursor != null && cursor.moveToFirst()) {
					list = new ArrayList<MessageNoticeBean>();
					do {
						MessageNoticeBean mnb = new MessageNoticeBean();
						mnb.setNoticeId(cursor.getInt(1));
						mnb.setSendMemberId(cursor.getInt(2));
						mnb.setSendMemberName(cursor.getString(3));
						mnb.setSendObjectId(cursor.getInt(4));
						mnb.setSendObjectName(cursor.getString(5));
						mnb.setObjectId(cursor.getInt(6));
						mnb.setObjectName(cursor.getString(7));
						mnb.setObjectType(cursor.getString(8));
						mnb.setActionType(cursor.getString(9));
						mnb.setNoticeContent(cursor.getString(10));
						mnb.setNoticeTime(cursor.getString(11));
						mnb.setIsRead("true".equals(cursor.getString(12)));
						mnb.setSendMemberHeadUrl(cursor.getString(13));
						mnb.setSendMemberHeadPath(cursor.getString(14));
						mnb.setObjectUrl(cursor.getString(15));
						mnb.setObjectPath(cursor.getString(16));
						mnb.setRemarkContent(cursor.getString(17));
						list.add(mnb);
					} while (cursor.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
			}
			return list;
		}
	}

	/**
	 * 删除列表
	 */
	public void delete(String noticeId) {
		synchronized (MSG_NOTICE) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from " + MSG_NOTICE + " where noticeId = '"
						+ noticeId + "'");
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
		synchronized (MSG_NOTICE) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from  " + MSG_NOTICE);
			} finally {
				if (db != null)
					db.close();
			}
		}
	}
}
