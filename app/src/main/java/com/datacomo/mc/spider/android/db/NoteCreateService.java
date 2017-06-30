package com.datacomo.mc.spider.android.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.datacomo.mc.spider.android.bean.NoteDraftBean;
import com.datacomo.mc.spider.android.url.L;

public class NoteCreateService extends SQLiteOpenHelper {

	public static int version = 2;

	// 数据库名字
	public static final String name = "note.db";

	public static String NOTE_CREATE = "note_create";

	private static NoteCreateService service = null;

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + NOTE_CREATE
				+ "(_id integer primary key autoincrement,"
				+ " time long, noteId integer, title varchar(100),"
				+ " content varchar(100000), filePaths varchar(10000),"
				+ " fileTemps varchar(100000), noteBookId integer)");

		db.execSQL("create table " + NoteInfoService.NOTE_DETAIL
				+ "(_id integer primary key autoincrement,"
				+ " noteId integer, content varchar(200000))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1 && newVersion == 2) {
			db.execSQL("alter table " + NOTE_CREATE + " add noteBookId integer");
		}
	}

	private NoteCreateService(Context context) {
		super(context, name, null, version);
	}

	public static NoteCreateService getService(Context context) {
		if (service == null)
			synchronized (NoteCreateService.class) {
				if (service == null)
					service = new NoteCreateService(context);
			}
		return service;
	}

	public void save(NoteDraftBean bean) {
		synchronized (NOTE_CREATE) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL(
						"insert into "
								+ NOTE_CREATE
								+ "(time, noteId, title, content, filePaths, fileTemps, noteBookId) "
								+ "values(?,?,?,?,?,?,?)",
						new Object[] { bean.getTime(), bean.getNoteId(),
								bean.getTitle(), bean.getContent(),
								bean.getFilePaths(), bean.getFileTemps(),
								bean.getNoteBookId() });
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	/**
	 * 修改笔记
	 * 
	 * @param time
	 * @param content
	 * @param updateTime
	 */
	public void updateNote(long time, NoteDraftBean bean) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.execSQL("delete from  " + NOTE_CREATE + " where time = '" + time
					+ "'");
			db.execSQL(
					"insert into "
							+ NOTE_CREATE
							+ "(time, noteId, title, content, filePaths, fileTemps,noteBookId) "
							+ "values(?,?,?,?,?,?,?)",
					new Object[] { bean.getTime(), bean.getNoteId(),
							bean.getTitle(), bean.getContent(),
							bean.getFilePaths(), bean.getFileTemps(),
							bean.getNoteBookId() });
		} finally {
			if (db != null)
				db.close();
		}
	}

	public ArrayList<NoteDraftBean> queryNotes() {
		synchronized (NOTE_CREATE) {
			Cursor cursor = null;
			SQLiteDatabase db = this.getWritableDatabase();
			ArrayList<NoteDraftBean> beans = null;
			try {
				cursor = db.rawQuery("select * from  " + NOTE_CREATE
						+ " order by time desc", null);
				if (cursor != null && cursor.moveToFirst()) {
					beans = new ArrayList<NoteDraftBean>();
					do {
						NoteDraftBean bean = new NoteDraftBean();
						bean.setTime(cursor.getLong(1));
						bean.setNoteId(cursor.getInt(2));
						bean.setTitle(cursor.getString(3));
						bean.setContent(cursor.getString(4));
						bean.setFilePaths(cursor.getString(5));
						bean.setFileTemps(cursor.getString(6));
						bean.setNoteBookId(cursor.getInt(7));
						beans.add(bean);
					} while (cursor.moveToNext());
					return beans;
				}
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

	public ArrayList<NoteDraftBean> queryNotesByBook(int noteBookId) {
		L.d("NoteCreateService", "queryNotesByBook noteBookId=" + noteBookId);
		synchronized (NOTE_CREATE) {
			Cursor cursor = null;
			SQLiteDatabase db = this.getWritableDatabase();
			ArrayList<NoteDraftBean> beans = null;
			try {
				cursor = db.rawQuery("select * from " + NOTE_CREATE
						+ " where noteBookId = ? order by time desc",
						new String[] { noteBookId + "" });
				if (cursor != null && cursor.moveToFirst()) {
					beans = new ArrayList<NoteDraftBean>();
					do {
						NoteDraftBean bean = new NoteDraftBean();
						bean.setTime(cursor.getLong(1));
						bean.setNoteId(cursor.getInt(2));
						bean.setTitle(cursor.getString(3));
						bean.setContent(cursor.getString(4));
						bean.setFilePaths(cursor.getString(5));
						bean.setFileTemps(cursor.getString(6));
						bean.setNoteBookId(cursor.getInt(7));
						beans.add(bean);
					} while (cursor.moveToNext());
					return beans;
				}
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

	public NoteDraftBean queryNote(int noteId) {
		synchronized (NOTE_CREATE) {
			Cursor cursor = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				cursor = db.rawQuery("select * from  " + NOTE_CREATE
						+ " where noteId = ?", new String[] { noteId + "" });
				if (cursor != null && cursor.moveToFirst()) {
					NoteDraftBean bean = new NoteDraftBean();
					bean.setTime(cursor.getLong(1));
					bean.setNoteId(noteId);
					bean.setTitle(cursor.getString(3));
					bean.setContent(cursor.getString(4));
					bean.setFilePaths(cursor.getString(5));
					bean.setFileTemps(cursor.getString(6));
					bean.setNoteBookId(cursor.getInt(7));
					return bean;
				}
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
	public void delete(long time, int noteId) {
		synchronized (NOTE_CREATE) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				if (noteId != 0) {
					db.execSQL("delete from  " + NOTE_CREATE
							+ " where noteId = '" + noteId + "'");
				} else {
					db.execSQL("delete from  " + NOTE_CREATE
							+ " where time = '" + time + "'");
				}
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
		synchronized (NOTE_CREATE) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from  " + NOTE_CREATE);
			} finally {
				if (db != null)
					db.close();
			}
		}
	}
}
