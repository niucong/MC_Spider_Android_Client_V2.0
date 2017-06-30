package com.datacomo.mc.spider.android.util;

import com.datacomo.mc.spider.android.url.L;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;

public class ThumbnailImgUtil {
	private static final String TAG = "ThumbnailImgUtil";

	/**
	 * if specify id equel this id mean the specify id is Empty
	 */
	private static final String EMPTYID = "-1";
	/**
	 * the connect mark
	 */
	public static final String MARK = "_";
	/**
	 * the default suffix id;
	 */
	public static final String SUFFIX = MARK + EMPTYID;

	/**
	 * split specify connectUrl
	 * 
	 * @param connectUrl
	 * @return 0 url 1 id;
	 */
	public static String[] split(Context context, String connectUrl) {
		String data = connectUrl.substring(0, connectUrl.lastIndexOf(MARK));
		String id = connectUrl.substring(connectUrl.lastIndexOf(MARK) + 1);
		if (isIdEmpty(id))
			id = selectId(context, data);
		return new String[] { data, id };
	}

	public static String[] split(String connectUrl) {
		String data = connectUrl.substring(0, connectUrl.lastIndexOf(MARK));
		String id = connectUrl.substring(connectUrl.lastIndexOf(MARK) + 1);
		return new String[] { data, id };
	}

	public static String getData(String connectUrl) {
		return connectUrl.substring(0, connectUrl.lastIndexOf(MARK));
	}

	public static String getId(String connectUrl) {
		return connectUrl.substring(connectUrl.lastIndexOf(MARK) + 1);
	}

	public static boolean isIdEmpty(String id) {
		return ThumbnailImgUtil.EMPTYID.equals(id);
	}

	public static boolean isUrlEmpty(String connectUrl) {
		String id = connectUrl.substring(connectUrl.lastIndexOf(MARK) + 1);
		return ThumbnailImgUtil.EMPTYID.equals(id);
	}

	private static String selectId(Context context, String path) {
		Cursor cursor = null;
		String id = null;
		String whereClause = Media.DATA + " = '" + path + "'";
		L.d(TAG, "selectId whereClause:" + whereClause);
		try {
			cursor = context.getContentResolver().query(
					Media.EXTERNAL_CONTENT_URI,
					new String[] { Media._ID }, whereClause,
					null, null);
			L.d(TAG,
					"selectId " + Media.DATA + " " + Media._ID + " "
							+ cursor.getCount());
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				id = cursor.getString(0);
				L.d(TAG, "selectId id:" + id);
			} else {
				id = EMPTYID;
			}
		} catch (Exception e) {
			e.printStackTrace();
			id = EMPTYID;
		} finally {
			if (null != cursor) {
				cursor.close();
				cursor = null;
			}
		}
		return id;
	}
}
