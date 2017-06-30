package com.datacomo.mc.spider.android.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.datacomo.mc.spider.android.net.been.map.MapResourceVisitBean;
import com.datacomo.mc.spider.android.util.JsonParseTool;

public class QuuBoVisitService extends DetailService {

	private static QuuBoVisitService service = null;

	private QuuBoVisitService(Context context) {
		super(context);
	}

	public static QuuBoVisitService getService(Context context) {
		if (service == null)
			synchronized (QuuBoVisitService.class) {
				if (service == null)
					service = new QuuBoVisitService(context);
			}
		return service;
	}

	public void save(int groupId, int quuboId, String content) {
		synchronized (QUUBO_VISIT) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				Cursor cur = db.rawQuery("select content from  " + QUUBO_VISIT
						+ " where groupId = ? and quuboId = ?", new String[] {
						groupId + "", quuboId + "" });
				if (cur != null && cur.moveToFirst()) {
					db.execSQL(
							"update "
									+ QUUBO_VISIT
									+ " set content = ? where groupId = ? and quuboId = ?",
							new String[] { content, groupId + "", quuboId + "" });
				} else {
					Cursor cursor = db.rawQuery(
							"select * from  " + QUUBO_VISIT, null);
					if (cursor.getCount() >= 100) {
						db.execSQL("delete from " + QUUBO_VISIT
								+ " where _id in (select _id from "
								+ QUUBO_VISIT + " order by _id limit 1) ");
					}
					db.execSQL("insert into " + QUUBO_VISIT
							+ "(groupId, quuboId, content) " + "values(?,?,?)",
							new Object[] { groupId, quuboId, content });
				}
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	public MapResourceVisitBean queryQuubo(int groupId, int quuboId) {
		synchronized (QUUBO_VISIT) {
			Cursor cursor = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select content from  " + QUUBO_VISIT
						+ " where groupId = ? and quuboId = ?", new String[] {
						groupId + "", quuboId + "" });
				if (cursor != null && cursor.moveToFirst())
					return (MapResourceVisitBean) JsonParseTool
							.dealComplexResult(cursor.getString(0),
									MapResourceVisitBean.class);
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
		synchronized (QUUBO_VISIT) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from  " + QUUBO_VISIT);
			} finally {
				if (db != null)
					db.close();
			}
		}
	}
}
