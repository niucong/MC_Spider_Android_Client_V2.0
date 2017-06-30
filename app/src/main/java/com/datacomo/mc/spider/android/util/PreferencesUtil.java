package com.datacomo.mc.spider.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences 数据存储
 */
public class PreferencesUtil {

	private SharedPreferences preferences;
	private static PreferencesUtil sharedPreferences;

	private PreferencesUtil(Context context) {
		preferences = context.getSharedPreferences("cloudfiles",
				Context.MODE_PRIVATE);
	}

	public static PreferencesUtil getPreferences(Context context) {
		if (sharedPreferences == null) {
			sharedPreferences = new PreferencesUtil(context);
		}
		return sharedPreferences;
	}

	/**
	 * 存储string类型数据
	 * 
	 * @param key
	 * @param value
	 */
	public void putString(String key, String value) {
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 存储int类型数据
	 * 
	 * @param key
	 * @param value
	 */
	public void putInt(String key, int value) {
		Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 存储boolean类型数据
	 * 
	 * @param key
	 * @param value
	 */
	public void putBoolean(String key, boolean value) {
		Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 读取string类型数据
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return preferences.getString(key, null);
	}

	/**
	 * 读取int类型数据
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		return preferences.getInt(key, 0);
	}

	/**
	 * 读取boolean类型数据
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBoolean(String key) {
		return preferences.getBoolean(key, true);
	}

}