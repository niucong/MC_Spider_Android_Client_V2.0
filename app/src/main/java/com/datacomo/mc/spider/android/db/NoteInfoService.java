package com.datacomo.mc.spider.android.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.JsonParseTool;

public class NoteInfoService extends SQLiteOpenHelper {

	private final String TAG = "NoteInfoService";
	public static String NOTE_DETAIL = "note_detail";

	private static NoteInfoService service = null;

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + NOTE_DETAIL
				+ "(_id integer primary key autoincrement,"
				+ " noteId integer, content varchar(200000))");
		db.execSQL("create table " + NoteCreateService.NOTE_CREATE
				+ "(_id integer primary key autoincrement,"
				+ " time long, noteId integer, title varchar(100),"
				+ " content varchar(100000), filePaths varchar(10000),"
				+ " fileTemps varchar(100000), noteBookId integer)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1 && newVersion == 2) {
			db.execSQL("alter table " + NoteCreateService.NOTE_CREATE
					+ " add noteBookId integer");
		}
	}

	private NoteInfoService(Context context) {
		super(context, NoteCreateService.name, null, NoteCreateService.version);
	}

	public static NoteInfoService getService(Context context) {
		if (service == null)
			synchronized (NoteInfoService.class) {
				if (service == null)
					service = new NoteInfoService(context);
			}
		return service;
	}

	public void save(int noteId, String content) {
		synchronized (NOTE_DETAIL) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from  " + NOTE_DETAIL + " where noteId = ?",
						new String[] { noteId + "" });

				Cursor cursor = db.rawQuery("select * from  " + NOTE_DETAIL,
						null);
				L.d(TAG, "save count=" + cursor.getCount());
				if (cursor.getCount() >= 100) {
					db.execSQL("delete from " + NOTE_DETAIL
							+ " where _id in (select _id from " + NOTE_DETAIL
							+ " order by _id limit 1) ");
				}
				db.execSQL("insert into " + NOTE_DETAIL + "(noteId, content) "
						+ "values(?,?)", new Object[] { noteId, content });
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	public MapNoteInfoBean queryMail(int noteId) {
		synchronized (NOTE_DETAIL) {
			Cursor cursor = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select content from  " + NOTE_DETAIL
						+ " where noteId = ?", new String[] { noteId + "" });
				if (cursor != null && cursor.moveToFirst())
					return (MapNoteInfoBean) JsonParseTool.dealComplexResult(
							cursor.getString(0), MapNoteInfoBean.class);
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
	 * 删除
	 */
	public void delete(int noteId) {
		synchronized (NOTE_DETAIL) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from  " + NOTE_DETAIL + " where noteId = ?",
						new String[] { noteId + "" });
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
		synchronized (NOTE_DETAIL) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from  " + NOTE_DETAIL);
			} finally {
				if (db != null)
					db.close();
			}
		}
	}
}
