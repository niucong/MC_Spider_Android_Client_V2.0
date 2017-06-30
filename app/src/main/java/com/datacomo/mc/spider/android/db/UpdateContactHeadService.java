package com.datacomo.mc.spider.android.db;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;

public class UpdateContactHeadService extends DataBaseService {

	private String TABLE_NAME = DataBaseHelper.UPDATE_CONTACT_HEAD;

	public UpdateContactHeadService(Context context) {
		super(context);
	}

	/**
	 * 
	 * @param number
	 * @param memberHead
	 */
	public void save(String number, String memberHead) {
		try {
			wDB = this.openWDB();
			wDB.execSQL("insert into " + TABLE_NAME + "(number,memberHead) "
					+ "values(?,?)", new Object[] { number, memberHead });
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 删除联系人
	 */
	public void delete() {
		try {
			wDB = this.openWDB();
			wDB.execSQL("delete from  " + TABLE_NAME);
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 查询头像列表
	 * 
	 * @return
	 */
	public HashMap<String, String> getContactHeads() {
		Cursor cursor = null;
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select * from " + TABLE_NAME, null);
			if (cursor != null && cursor.moveToFirst()) {
				do {
					map.put(cursor.getString(1), cursor.getString(2));
				} while (cursor.moveToNext());
			}
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return map;
	}

	/**
	 * 
	 * @param number
	 * @param memberHead
	 */
	public void updateHead(String number, String memberHead) {
		try {
			wDB = this.openWDB();
			wDB.execSQL("update " + TABLE_NAME
					+ " set memberHead = ? where number = ?", new String[] {
					memberHead, number });
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}
}
