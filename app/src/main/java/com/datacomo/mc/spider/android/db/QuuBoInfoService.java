package com.datacomo.mc.spider.android.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.datacomo.mc.spider.android.net.been.QuuboInfoBean;
import com.datacomo.mc.spider.android.util.JsonParseTool;

public class QuuBoInfoService extends DetailService {

	private static QuuBoInfoService service = null;

	private QuuBoInfoService(Context context) {
		super(context);
	}

	public static QuuBoInfoService getService(Context context) {
		if (service == null)
			synchronized (QuuBoInfoService.class) {
				if (service == null)
					service = new QuuBoInfoService(context);
			}
		return service;
	}

	public void save(int groupId, int quuboId, String content) {
		synchronized (QUUBO_DETAIL) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				Cursor cursor = db.rawQuery("select * from  " + QUUBO_DETAIL,
						null);
				if (cursor.getCount() >= 100) {
					db.execSQL("delete from " + QUUBO_DETAIL
							+ " where _id in (select _id from " + QUUBO_DETAIL
							+ " order by _id limit 1) ");
				}
				db.execSQL("insert into " + QUUBO_DETAIL
						+ "(groupId, quuboId, content) " + "values(?,?,?)",
						new Object[] { groupId, quuboId, content });
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	public QuuboInfoBean queryQuubo(int groupId, int quuboId) {
		synchronized (QUUBO_DETAIL) {
			Cursor cursor = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select content from  " + QUUBO_DETAIL
						+ " where groupId = ? and quuboId = ?", new String[] {
						groupId + "", quuboId + "" });
				if (cursor != null && cursor.moveToFirst())
					return (QuuboInfoBean) JsonParseTool.dealComplexResult(
							cursor.getString(0), QuuboInfoBean.class);
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
		synchronized (QUUBO_DETAIL) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from  " + QUUBO_DETAIL);
			} finally {
				if (db != null)
					db.close();
			}
		}
	}
}
