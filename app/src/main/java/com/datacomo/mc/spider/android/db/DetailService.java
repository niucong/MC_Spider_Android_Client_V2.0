package com.datacomo.mc.spider.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class DetailService extends SQLiteOpenHelper {

	private static int version = 3;

	// 数据库名字
	private static final String name = "detail.db";

	protected static String QUUBO_DETAIL = "quubo_detail";
	protected static String MAIL_DETAIL = "mail_detail";
	protected static String FILE_URL = "file_url";

	protected static String QUUBO_VISIT = "quubo_visit";
	protected static String QUUBO_PRAISE = "quubo_praise";
	protected static String QUUBO_COMMENT = "quubo_comment";

	public DetailService(Context context) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + QUUBO_DETAIL
				+ "(_id integer primary key autoincrement,"
				+ " groupId integer, quuboId integer, content varchar(100000))");
		db.execSQL("create table " + MAIL_DETAIL
				+ "(_id integer primary key autoincrement,"
				+ " mailId integer, recordId integer, content varchar(100000))");
		oneToTwo(db);
		twoToThree(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 2 && newVersion == 3) {
			twoToThree(db);
		} else if (oldVersion == 1 && newVersion == 3) {
			oneToTwo(db);
			twoToThree(db);
		}
	}

	private void twoToThree(SQLiteDatabase db) {
		db.execSQL("create table " + QUUBO_VISIT
				+ "(_id integer primary key autoincrement,"
				+ " groupId integer, quuboId integer, content varchar(100000))");
		db.execSQL("create table " + QUUBO_PRAISE
				+ "(_id integer primary key autoincrement,"
				+ " groupId integer, quuboId integer, content varchar(100000))");
		db.execSQL("create table " + QUUBO_COMMENT
				+ "(_id integer primary key autoincrement,"
				+ " groupId integer, quuboId integer, content varchar(100000))");
	}

	private void oneToTwo(SQLiteDatabase db) {
		db.execSQL("create table " + FILE_URL
				+ "(_id integer primary key autoincrement,"
				+ " groupId integer, fileId integer, content varchar(200))");
	}

}
