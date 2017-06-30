package com.datacomo.mc.spider.android.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.bean.SearchRecordBean;

public class SearchRecordService extends DataBaseService {
	private static String TABLE_NAME = "record_search";
	private String session_key;

	public SearchRecordService(Context context) {
		super(context);
		session_key = App.app.share.getSessionKey();
	}

	/**
	 * 添加搜索记录
	 * 
	 * @param user
	 */
	public void insert(String text) {
		try {
			wDB = this.openWDB();
			wDB.execSQL("insert into " + TABLE_NAME
					+ "(record, session_key) values(?,?)", new String[] { text,
					session_key });
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 根据id删除记录
	 */
	public void delete(int id) {
		try {
			wDB = this.openWDB();
			wDB.execSQL("delete from " + TABLE_NAME + "where _id=" + id);
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 根据key查询匹配字符串
	 * 
	 * @return
	 */
	public ArrayList<SearchRecordBean> query(String key) {
		ArrayList<SearchRecordBean> result = new ArrayList<SearchRecordBean>();
		Cursor cursor = null;
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select * from " + TABLE_NAME, null);
			while (cursor.moveToNext()) {
				String text = cursor.getString(cursor.getColumnIndex("record"));
				String sKey = cursor.getString(cursor
						.getColumnIndex("session_key"));
				if (text.startsWith(key) && sKey.equals(session_key)) {
					int id = cursor.getInt(cursor.getColumnIndex("_id"));
					SearchRecordBean bean = new SearchRecordBean();
					bean.setId(id);
					bean.setRecord(text);
					result.add(bean);
				}
			}
		} finally {
			if (rDB != null)
				rDB.close();
			if (cursor != null)
				cursor.close();
		}
		return result;
	}
}
