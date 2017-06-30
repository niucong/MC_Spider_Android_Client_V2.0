package com.datacomo.mc.spider.android.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.datacomo.mc.spider.android.net.been.MailBean;
import com.datacomo.mc.spider.android.util.JsonParseTool;

public class MailInfoService extends DetailService {

	private static MailInfoService service = null;

	private MailInfoService(Context context) {
		super(context);
	}

	public static MailInfoService getService(Context context) {
		if (service == null)
			synchronized (MailInfoService.class) {
				if (service == null)
					service = new MailInfoService(context);
			}
		return service;
	}

	public void save(int mailId, int recordId, String content) {
		synchronized (MAIL_DETAIL) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				Cursor cursor = db.rawQuery("select * from  " + MAIL_DETAIL,
						null);
				if (cursor.getCount() >= 100) {
					db.execSQL("delete from " + MAIL_DETAIL
							+ " where _id in (select _id from " + MAIL_DETAIL
							+ " order by _id limit 1) ");
				}
				db.execSQL("insert into " + MAIL_DETAIL
						+ "(mailId, recordId, content) " + "values(?,?,?)",
						new Object[] { mailId, recordId, content });
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	public MailBean queryMail(int mailId, int recordId) {
		synchronized (MAIL_DETAIL) {
			Cursor cursor = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select content from  " + MAIL_DETAIL
						+ " where mailId = ? and recordId = ?", new String[] {
						mailId + "", recordId + "" });
				if (cursor != null && cursor.moveToFirst())
					return (MailBean) JsonParseTool.dealComplexResult(
							cursor.getString(0), MailBean.class);
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
		synchronized (MAIL_DETAIL) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from  " + MAIL_DETAIL);
			} finally {
				if (db != null)
					db.close();
			}
		}
	}
}
