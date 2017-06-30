package com.datacomo.mc.spider.android.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

public class RegisterInfoDatabase extends DataBaseService {
	private String TABLE_NAME = DataBaseHelper.REGISTER_INFO;

	public RegisterInfoDatabase(Context context) {
		super(context);
	}

	/**
	 * 添加用户信息
	 * 
	 * @param user
	 */
	public void insert(String email, String password, String phone) {
		try {
			wDB = this.openWDB();
			wDB.execSQL("insert into " + TABLE_NAME
					+ "(email, password, phone) values(?,?,?)", new String[] {
					email, password, phone });
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 删除用户信息
	 */
	public void delete() {
		try {
			wDB = this.openWDB();
			wDB.execSQL("delete from " + TABLE_NAME);
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 根据用户名查询用户信息
	 * 
	 * @return
	 */
	public ArrayList<String> queryInfo() {
		ArrayList<String> info = null;
		Cursor cursor = null;
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select * from " + TABLE_NAME, null);
			while (cursor.moveToNext()) {
				info = new ArrayList<String>();
				info.add(cursor.getString(cursor.getColumnIndex("email")));
				info.add(cursor.getString(cursor.getColumnIndex("password")));
				info.add(cursor.getString(cursor.getColumnIndex("phone")));
			}
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return info;
	}
}
