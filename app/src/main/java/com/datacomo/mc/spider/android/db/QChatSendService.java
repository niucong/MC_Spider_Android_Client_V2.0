package com.datacomo.mc.spider.android.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.datacomo.mc.spider.android.bean.ChatSendBean;
import com.datacomo.mc.spider.android.url.L;

public class QChatSendService extends SQLiteOpenHelper {

	public static int version = 3;

	// 数据库名字
	public static final String name = "chat.db";

	private final String TAG = "QChatSendService";
	public static String CHAT_LIST = "chat_send_list";

	private static QChatSendService service = null;

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "
				+ CHAT_LIST
				+ "(_id integer primary key autoincrement,"
				+ " time Long, session_key varchar(30), id integer, name varchar(20), head varchar(100), cType integer, mType varchar(20), content varchar(10000), tLong Long)");
		oneTwoToThree(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if ((oldVersion == 2 || oldVersion == 1) && newVersion == 3) {
			oneTwoToThree(db);
		}
	}

	private void oneTwoToThree(SQLiteDatabase db) {
		db.execSQL("create table "
				+ ChatGroupMessageBeanService.CHAT_GROUP
				+ "(_id integer primary key autoincrement,"
				+ " messageId integer, messageStatus integer, messageContent varchar(10000),"
				+ " senderId integer, senderName integer, senderHeadUrl varchar(100), senderHeadPath varchar(100),"
				+ " receiverId integer, receiverName integer, receiverHeadUrl varchar(100), receiverHeadPath varchar(100), "
				+ " createTime varchar(200),"
				+ " resourceId integer, rMessageContent varchar(10000), objectType varchar(100), objectName varchar(100),"
				+ " objectUrl varchar(100), objectPath varchar(100), objectSize integer, objectBak1 long, "
				+ " objectBak2 varchar(100), objectBak3 varchar(100), isBreak integer)");

		db.execSQL("create table "
				+ ChatMessageBeanService.CHAT_MSG
				+ "(_id integer primary key autoincrement,"
				+ " messageId integer, messageStatus integer, messageContent varchar(10000),"
				+ " senderId integer, senderName integer, senderHeadUrl varchar(100), senderHeadPath varchar(100),"
				+ " receiverId integer, receiverName integer, receiverHeadUrl varchar(100), receiverHeadPath varchar(100), "
				+ " createTime varchar(200),"
				+ " resourceId integer, rMessageContent varchar(10000), objectType varchar(100), objectName varchar(100),"
				+ " objectUrl varchar(100), objectPath varchar(100), objectSize integer, objectBak1 long, "
				+ " objectBak2 varchar(100), objectBak3 varchar(100), isBreak integer)");
	}

	private QChatSendService(Context context) {
		super(context, name, null, version);
	}

	public static QChatSendService getService(Context context) {
		if (service == null)
			synchronized (QChatSendService.class) {
				if (service == null)
					service = new QChatSendService(context);
			}
		return service;
	}

	/**
	 * 
	 * @param entitys
	 */
	public void save(ChatSendBean bean) {
		synchronized (CHAT_LIST) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL(
						"insert into "
								+ CHAT_LIST
								+ "(time, session_key, id, name, head, cType, mType, content, tLong) "
								+ "values(?,?,?,?,?,?,?,?,?)",
						new Object[] { bean.getTime(), bean.getSession_key(),
								bean.getId(), bean.getName(), bean.getHead(),
								bean.getcType(), bean.getmType(),
								bean.getContent(), bean.gettLong() });
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	public ArrayList<ChatSendBean> queryById(String session_key, String id,
			int cType) {
		L.d(TAG, "session_key=" + session_key + ",id=" + id + ",cType=" + cType);
		synchronized (CHAT_LIST) {
			Cursor cursor = null;
			ArrayList<ChatSendBean> entitys = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select * from  " + CHAT_LIST
						+ " where session_key = ? and id = ? and cType = ?",
						new String[] { session_key, id + "", cType + "" });
				if (cursor != null && cursor.moveToFirst()) {
					entitys = new ArrayList<ChatSendBean>();
					do {
						ChatSendBean bean = new ChatSendBean();
						bean.setTime(cursor.getLong(1));
						bean.setSession_key(cursor.getString(2));
						bean.setId(cursor.getInt(3));
						bean.setName(cursor.getString(4));
						bean.setHead(cursor.getString(5));
						bean.setcType(cursor.getInt(6));
						bean.setmType(cursor.getString(7));
						bean.setContent(cursor.getString(8));
						bean.settLong(cursor.getLong(9));
						entitys.add(bean);
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

	public ArrayList<ChatSendBean> queryOnlyId(String session_key, int cType) {
		synchronized (CHAT_LIST) {
			Cursor cursor = null;
			ArrayList<ChatSendBean> entitys = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select * from  " + CHAT_LIST
						+ " where session_key = '" + session_key
						+ "' and cType = '" + cType + "' order by time desc",
						null);
				if (cursor != null && cursor.moveToFirst()) {
					entitys = new ArrayList<ChatSendBean>();
					ArrayList<Integer> ids = new ArrayList<Integer>();
					do {
						int id = cursor.getInt(3);
						if (!ids.contains(id)) {
							ChatSendBean bean = new ChatSendBean();
							bean.setTime(cursor.getLong(1));
							bean.setSession_key(cursor.getString(2));
							bean.setId(id);
							bean.setName(cursor.getString(4));
							bean.setHead(cursor.getString(5));
							bean.setcType(cursor.getInt(6));
							bean.setmType(cursor.getString(7));
							bean.setContent(cursor.getString(8));
							bean.settLong(cursor.getLong(9));
							entitys.add(bean);
							ids.add(id);
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
	public void deleteChat(long time) {
		synchronized (CHAT_LIST) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from " + CHAT_LIST + " where time = '"
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
		synchronized (CHAT_LIST) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from  " + CHAT_LIST);
			} finally {
				if (db != null)
					db.close();
			}
		}
	}
}
