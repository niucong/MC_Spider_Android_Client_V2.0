package com.datacomo.mc.spider.android.db;

import android.content.Context;
import android.database.Cursor;

public class MessageService extends DataBaseService {

	private static String TABLE_NAME = DataBaseHelper.MESSAGE;

	public MessageService(Context context) {
		super(context);
	}

	/**
	 * 保存
	 * 
	 * @param session_key
	 * @param type
	 * @param message
	 */
	public void save(String session_key, String type, String message) {
		try {
			wDB = this.openWDB();
			if (message != null && !message.equals("")) {
				wDB.execSQL("insert into " + TABLE_NAME
						+ "(session_key,type,message) " + "values(?,?,?)",
						new Object[] { session_key, type, message });
			}
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 删除
	 */
	public void delete(String session_key, String type) {
		try {
			wDB = this.openWDB();
			wDB.execSQL("delete from  " + TABLE_NAME + " where session_key = '"
					+ session_key + "' and type = '" + type + "'");
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
	public String queryTrends(String session_key, String type) {
		Cursor cursor = null;
		String message = null;
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select message from  " + TABLE_NAME
					+ " where session_key = '" + session_key + "' and type = '"
					+ type + "'", null);
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
}
