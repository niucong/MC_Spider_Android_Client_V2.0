package com.datacomo.mc.spider.android.db;

import android.content.Context;
import android.database.Cursor;

public class GroupTrendListService extends DataBaseService {

	private static String TABLE_NAME = DataBaseHelper.GROUP_TREND;

	public GroupTrendListService(Context context) {
		super(context);
	}

	/**
	 * 
	 * @param session_key
	 * @param type
	 *            0：圈子动态、1：朋友动态、2：随便看看、3：收藏的圈博、4：收藏的文件、5：收藏的照片
	 * @param trends
	 */
	public void save(String session_key, String type, String trends) {
		try {
			wDB = this.openWDB();
			if (trends != null && !trends.equals("")) {
				wDB.execSQL("insert into " + TABLE_NAME
						+ "(session_key,type,group_trend) " + "values(?,?,?)",
						new Object[] { session_key, type, trends });
			}
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 删除
	 */
	public void delete(String session_key, String type) {
		try {
			wDB = this.openWDB();
			wDB.execSQL("delete from  " + TABLE_NAME + " where session_key = '"
					+ session_key + "' and type = '" + type + "'");
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 查询
	 * 
	 * @return
	 */
	public String queryTrends(String session_key, String type) {
		Cursor cursor = null;
		String trends = null;
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select group_trend from  " + TABLE_NAME
					+ " where session_key = '" + session_key + "' and type = '"
					+ type + "'", null);
			if (cursor != null && cursor.moveToFirst()) {
				do {
					trends = cursor.getString(0);
				} while (cursor.moveToNext());
			}
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return trends;
	}
}
