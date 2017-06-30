package com.datacomo.mc.spider.android.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.datacomo.mc.spider.android.net.been.map.MapResourceGreatBean;
import com.datacomo.mc.spider.android.util.JsonParseTool;

public class QuuBoPraiseService extends DetailService {

	private static QuuBoPraiseService service = null;

	private QuuBoPraiseService(Context context) {
		super(context);
	}

	public static QuuBoPraiseService getService(Context context) {
		if (service == null)
			synchronized (QuuBoPraiseService.class) {
				if (service == null)
					service = new QuuBoPraiseService(context);
			}
		return service;
	}

	public void save(int groupId, int quuboId, String content) {
		synchronized (QUUBO_PRAISE) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				Cursor cur = db.rawQuery("select content from  " + QUUBO_PRAISE
						+ " where groupId = ? and quuboId = ?", new String[] {
						groupId + "", quuboId + "" });
				if (cur != null && cur.moveToFirst()) {
					db.execSQL(
							"update "
									+ QUUBO_PRAISE
									+ " set content = ? where groupId = ? and quuboId = ?",
							new String[] { content, groupId + "", quuboId + "" });
				} else {
					Cursor cursor = db.rawQuery("select * from  "
							+ QUUBO_PRAISE, null);
					if (cursor.getCount() >= 100) {
						db.execSQL("delete from " + QUUBO_PRAISE
								+ " where _id in (select _id from "
								+ QUUBO_PRAISE + " order by _id limit 1) ");
					}
					db.execSQL("insert into " + QUUBO_PRAISE
							+ "(groupId, quuboId, content) " + "values(?,?,?)",
							new Object[] { groupId, quuboId, content });
				}
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	public MapResourceGreatBean queryQuubo(int groupId, int quuboId) {
		synchronized (QUUBO_PRAISE) {
			Cursor cursor = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select content from  " + QUUBO_PRAISE
						+ " where groupId = ? and quuboId = ?", new String[] {
						groupId + "", quuboId + "" });
				if (cursor != null && cursor.moveToFirst())
					return (MapResourceGreatBean) JsonParseTool
							.dealComplexResult(cursor.getString(0),
									MapResourceGreatBean.class);
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
		synchronized (QUUBO_PRAISE) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from  " + QUUBO_PRAISE);
			} finally {
				if (db != null)
					db.close();
			}
		}
	}
}
