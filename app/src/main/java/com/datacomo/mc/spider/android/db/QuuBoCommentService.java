package com.datacomo.mc.spider.android.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.datacomo.mc.spider.android.net.been.map.MapResourceCommentBean;
import com.datacomo.mc.spider.android.util.JsonParseTool;

public class QuuBoCommentService extends DetailService {

	private static QuuBoCommentService service = null;

	private QuuBoCommentService(Context context) {
		super(context);
	}

	public static QuuBoCommentService getService(Context context) {
		if (service == null)
			synchronized (QuuBoCommentService.class) {
				if (service == null)
					service = new QuuBoCommentService(context);
			}
		return service;
	}

	public void save(int groupId, int quuboId, String content) {
		synchronized (QUUBO_COMMENT) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				Cursor cur = db.rawQuery("select content from  "
						+ QUUBO_COMMENT + " where groupId = ? and quuboId = ?",
						new String[] { groupId + "", quuboId + "" });
				if (cur != null && cur.moveToFirst()) {
					db.execSQL(
							"update "
									+ QUUBO_COMMENT
									+ " set content = ? where groupId = ? and quuboId = ?",
							new String[] { content, groupId + "", quuboId + "" });
				} else {
					Cursor cursor = db.rawQuery("select * from  "
							+ QUUBO_COMMENT, null);
					if (cursor.getCount() >= 100) {
						db.execSQL("delete from " + QUUBO_COMMENT
								+ " where _id in (select _id from "
								+ QUUBO_COMMENT + " order by _id limit 1) ");
					}
					db.execSQL("insert into " + QUUBO_COMMENT
							+ "(groupId, quuboId, content) " + "values(?,?,?)",
							new Object[] { groupId, quuboId, content });
				}
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	public MapResourceCommentBean queryQuubo(int groupId, int quuboId) {
		synchronized (QUUBO_COMMENT) {
			Cursor cursor = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select content from  " + QUUBO_COMMENT
						+ " where groupId = ? and quuboId = ?", new String[] {
						groupId + "", quuboId + "" });
				if (cursor != null && cursor.moveToFirst())
					return (MapResourceCommentBean) JsonParseTool
							.dealComplexResult(cursor.getString(0),
									MapResourceCommentBean.class);
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
		synchronized (QUUBO_COMMENT) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from  " + QUUBO_COMMENT);
			} finally {
				if (db != null)
					db.close();
			}
		}
	}
}
