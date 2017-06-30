package com.datacomo.mc.spider.android.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FileUrlService extends DetailService {

	private static FileUrlService service = null;

	private FileUrlService(Context context) {
		super(context);
	}

	public static FileUrlService getService(Context context) {
		if (service == null)
			synchronized (FileUrlService.class) {
				if (service == null)
					service = new FileUrlService(context);
			}
		return service;
	}

	public void save(int groupId, int fileId, String content) {
		synchronized (FILE_URL) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				Cursor cursor = db.rawQuery("select * from  " + FILE_URL, null);
				if (cursor.getCount() >= 500) {
					db.execSQL("delete from " + FILE_URL
							+ " where _id in (select _id from " + FILE_URL
							+ " order by _id limit 1) ");
				}
				db.execSQL("insert into " + FILE_URL
						+ "(groupId, fileId, content) " + "values(?,?,?)",
						new Object[] { groupId, fileId, content });
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	public String queryFileUrl(int groupId, int fileId) {
		synchronized (FILE_URL) {
			Cursor cursor = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select content from  " + FILE_URL
						+ " where groupId = ? and fileId = ?", new String[] {
						groupId + "", fileId + "" });
				if (cursor != null && cursor.moveToFirst())
					return cursor.getString(0);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
			}
			return null;
		}
	}

	/**
	 * 删除所有
	 */
	public void deleteAll() {
		synchronized (FILE_URL) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from  " + FILE_URL);
			} finally {
				if (db != null)
					db.close();
			}
		}
	}
}
