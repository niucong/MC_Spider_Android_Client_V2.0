package com.datacomo.mc.spider.android.db;

import android.content.Context;
import android.database.Cursor;

public class MessageChatService extends DataBaseService {

	private static String TABLE_NAME = DataBaseHelper.MESSAGE_CHAT;

	public MessageChatService(Context context) {
		super(context);
	}

	/**
	 * 保存
	 * 
	 * @param session_key
	 * @param friendId
	 * @param contactLeaguerId
	 * @param message
	 */
	public void save(String session_key, String friendId,
			String contactLeaguerId, String message) {
		try {
			wDB = this.openWDB();
			if (message != null && !message.equals("")) {
				wDB.execSQL("insert into " + TABLE_NAME
						+ "(session_key,friendId,contactLeaguerId,message) "
						+ "values(?,?,?,?)", new Object[] { session_key,
						friendId, contactLeaguerId, message });
			}
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 删除
	 */
	public void delete(String session_key, String contactLeaguerId) {
		try {
			wDB = this.openWDB();
			wDB.execSQL("delete from  " + TABLE_NAME + " where session_key = '"
					+ session_key + "' and contactLeaguerId = '"
					+ contactLeaguerId + "'");
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 查询
	 * 
	 * @return
	 */
	public String queryTrends(String session_key, String contactLeaguerId) {
		Cursor cursor = null;
		String message = null;
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select message from  " + TABLE_NAME
					+ " where session_key = '" + session_key
					+ "' and contactLeaguerId = '" + contactLeaguerId + "'",
					null);
			if (cursor != null && cursor.moveToFirst()) {
				do {
					message = cursor.getString(0);
				} while (cursor.moveToNext());
			}
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return message;
	}

	/**
	 * 查询contactLeaguerId
	 * 
	 * @return
	 */
	public String queryContactLeaguerId(String session_key, String friendId) {
		Cursor cursor = null;
		String contactLeaguerId = null;
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select contactLeaguerId from  " + TABLE_NAME
					+ " where session_key = '" + session_key
					+ "' and friendId = '" + friendId + "'", null);
			if (cursor != null && cursor.moveToFirst()) {
				do {
					contactLeaguerId = cursor.getString(0);
				} while (cursor.moveToNext());
			}
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return contactLeaguerId;
	}
}
