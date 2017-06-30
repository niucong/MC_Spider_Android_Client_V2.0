/**
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * Copyright 2006-2011 DataComo Communications Technology INC.
 * 
 * This source file is a part of MC_Spider_Android_Client_V1.0 project. 
 * date: Jun 30, 2011
 *
 */
package com.datacomo.mc.spider.android.util;

import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 用户臨時保存session_key配置类
 * 
 * @author Sharesin
 * @date Jun 30, 2011 4:21:13 AM
 * @update developer Sharesin
 * @update date Jun 30, 2011 4:21:13 AM
 * @version v1.0.0
 */
public class AppSharedPreferences {

	private Context context;

	public AppSharedPreferences(Context applicationContext) {
		this.context = applicationContext;
	}

	/**
	 * 使用SharedPreferences保存软件设置参数
	 * 
	 * @param name
	 *            需要保存的参数名称
	 * @param age
	 *            需要保存的参数名称
	 */
	@SuppressWarnings("static-access")
	public void saveSessionKey(String session_key) {
		SharedPreferences preferences = this.context.getSharedPreferences(
				"SESSIONKEY", context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.putString("session_key", session_key);
		// 执行commit方法后，edit中设置的内容才真正写入文件中
		edit.commit();
	}

	/**
	 * 从SharedPreferences中获取数据用于在界面回显
	 * 
	 * @return 保存的数据
	 */
	@SuppressWarnings("static-access")
	public String getSessionKey() {
		SharedPreferences preferences = this.context.getSharedPreferences(
				"SESSIONKEY", context.MODE_PRIVATE);
		String session_key = preferences.getString("session_key", "");
		return session_key;
	}

	/**
	 * 保存数据 fileName 文件名 key 键 value 值
	 */
	@SuppressWarnings("static-access")
	public void saveStringMessage(String fileName, String key, String value) {
		SharedPreferences preferences = this.context.getSharedPreferences(
				fileName, context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.putString(key, value);
		// 执行commit方法后，edit中设置的内容才真正写入文件中
		edit.commit();
	}

	/**
	 * 获取数据 fileName 文件名 key 键
	 */
	public String getStringMessage(String fileName, String key, String value) {
		SharedPreferences preferences = this.context.getSharedPreferences(
				fileName, Context.MODE_PRIVATE);
		String setup = preferences.getString(key, value);
		return setup;
	}

	@SuppressWarnings("static-access")
	public void saveIntMessage(String fileName, String key, int value) {
		SharedPreferences preferences = this.context.getSharedPreferences(
				fileName, context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.putInt(key, value);
		// 执行commit方法后，edit中设置的内容才真正写入文件中
		edit.commit();
	}

	public int getIntMessage(String fileName, String key, int defaltValve) {
		SharedPreferences preferences = this.context.getSharedPreferences(
				fileName, Context.MODE_PRIVATE);
		int setup = preferences.getInt(key, defaltValve);
		return setup;
	}

	public int getAllMessage(String fileName, int defaltValve) {
		SharedPreferences preferences = this.context.getSharedPreferences(
				fileName, Context.MODE_PRIVATE);
		@SuppressWarnings("unchecked")
		Map<String, Integer> map = (Map<String, Integer>) preferences.getAll();
		int setup = 0;
		if (map != null && map.size() > 0) {
			for (Iterator<?> iterator = map.keySet().iterator(); iterator
					.hasNext();) {
				String key = (String) iterator.next();
				setup += map.get(key);
			}
		}
		return setup;
	}

	/**
	 * 保存数据 fileName 文件名 key 键 value 值
	 */
	@SuppressWarnings("static-access")
	public void saveBooleanMessage(String fileName, String key, boolean value) {
		SharedPreferences preferences = this.context.getSharedPreferences(
				fileName, context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	/**
	 * 获取数据 fileName 文件名 key 键
	 */
	public boolean getBooleanMessage(String fileName, String key,
			boolean defaltValve) {
		SharedPreferences preferences = this.context.getSharedPreferences(
				fileName, Context.MODE_PRIVATE);
		boolean setup = preferences.getBoolean(key, defaltValve);
		return setup;
	}

	@SuppressWarnings("static-access")
	public void saveLongMessage(String fileName, String key, long value) {
		SharedPreferences preferences = this.context.getSharedPreferences(
				fileName, context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.putLong(key, value);
		edit.commit();
	}

	public long getLongMessage(String fileName, String key, long defaltValve) {
		SharedPreferences preferences = this.context.getSharedPreferences(
				fileName, Context.MODE_PRIVATE);
		long setup = preferences.getLong(key, defaltValve);
		return setup;
	}

	/**
	 * 删除数据 fileName 文件名 key 键 value 值
	 */
	@SuppressWarnings("static-access")
	public void deleteMessage(String fileName, String key) {
		SharedPreferences preferences = this.context.getSharedPreferences(
				fileName, context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.remove(key);
		// 执行commit方法后，edit中设置的内容才真正写入文件中
		edit.commit();
	}
}
