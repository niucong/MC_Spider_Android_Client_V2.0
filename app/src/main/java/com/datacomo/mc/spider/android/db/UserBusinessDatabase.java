package com.datacomo.mc.spider.android.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.datacomo.mc.spider.android.bean.UserBean;
import com.datacomo.mc.spider.android.url.L;

public class UserBusinessDatabase extends DataBaseService {
	private static final String TAG = "UserBusinessDatabase";
	private String TABLE_NAME = DataBaseHelper.USER_MESSAGE;

	public UserBusinessDatabase(Context context) {
		super(context);
	}

	/**
	 * 添加用户信息
	 * 
	 * @param user
	 */
	public void insert(UserBean user) {
		L.i(TAG,
				"insert name=" + user.getName() + ",Username="
						+ user.getUsername());
		try {
			wDB = this.openWDB();
			wDB.execSQL(
					"insert into "
							+ TABLE_NAME
							+ "(user_name,account, password, session_key,memberId,mark, "
							+ "accountType,openId,name,sex,headUrlPath,oauth_token,access_Token,"
							+ "access_Token_secret) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new String[] { user.getName(), user.getUsername(),
							user.getPassword(), user.getSession_key(),
							user.getMemberId(), user.getMark(),
							user.getAccountType(), user.getOpenId(),
							user.getName(), user.getSex(),
							user.getHeadUrlPath(), user.getOauth_token(),
							user.getAccess_Token(),
							user.getAccess_Token_secret() });
		} catch (SQLException e) {
			L.i(TAG, "insert SQLException...");
			e.printStackTrace();
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 根据session_key删除用户信息
	 * 
	 * @param username
	 */
	public void delete(String session_key) {
		try {
			wDB = this.openWDB();
			wDB.execSQL("delete from " + TABLE_NAME + " where session_key = '"
					+ session_key + "'");
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 删除用户信息
	 * 
	 * @param username
	 */
	public void deleteAll() {
		L.i(TAG, "deleteAll...");
		try {
			wDB = this.openWDB();
			wDB.execSQL("delete from " + TABLE_NAME);
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 获得上传通信录时间
	 * 
	 * @param name
	 * @return
	 */
	public String getMemberId(String session_key) {
		Cursor cursor = null;
		String memberId = "0";
		try {
			rDB = this.openRDB();

			cursor = rDB.rawQuery("select memberId from " + TABLE_NAME
					+ " where session_key = ?", new String[] { session_key });
			if (cursor.moveToFirst())
				memberId = cursor.getString(0);
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return memberId;
	}

	/**
	 * 修改Id
	 * 
	 * @param memberId
	 */
	public void updateMemberId(String session_key, String memberId) {
		try {
			wDB = this.openWDB();
			wDB.execSQL("update " + TABLE_NAME
					+ " set memberId = ? where session_key = ?", new String[] {
					memberId, session_key });
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 查询所有的用户信息
	 * 
	 * @return
	 */
	public List<UserBean> selectUsers() {
		List<UserBean> users = null;
		Cursor cursor = null;
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select * from " + TABLE_NAME + "", null);
			users = new ArrayList<UserBean>();
			while (cursor.moveToNext()) {
				String username = cursor.getString(cursor
						.getColumnIndex("account"));
				String password = cursor.getString(cursor
						.getColumnIndex("password"));
				String sessionKey = cursor.getString(cursor
						.getColumnIndex("session_key"));
				String mark = cursor.getString(cursor.getColumnIndex("mark"));

				String accountType = cursor.getString(cursor
						.getColumnIndex("accountType"));
				String openId = cursor.getString(cursor
						.getColumnIndex("openId"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String sex = cursor.getString(cursor.getColumnIndex("sex"));
				String headUrlPath = cursor.getString(cursor
						.getColumnIndex("headUrlPath"));
				String oauth_token = cursor.getString(cursor
						.getColumnIndex("oauth_token"));
				String access_Token = cursor.getString(cursor
						.getColumnIndex("access_Token"));
				String access_Token_secret = cursor.getString(cursor
						.getColumnIndex("access_Token_secret"));
				UserBean bean = new UserBean(username, password, sessionKey,
						mark);
				// accountType,openId,name,sex,headUrlPath,access_Token,access_Token_secret
				bean.setAccountType(accountType);
				bean.setOpenId(openId);
				bean.setName(name);
				bean.setSex(sex);
				bean.setHeadUrlPath(headUrlPath);
				bean.setOauth_token(oauth_token);
				bean.setAccess_Token(access_Token);
				bean.setAccess_Token_secret(access_Token_secret);
				users.add(bean);
			}
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return users;
	}

	/**
	 * 根据用户标志查询用户信息
	 * 
	 * @return
	 */
	public List<UserBean> selectUserByMark(String mark) {
		Cursor cursor = null;
		List<UserBean> user = null;
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select * from " + TABLE_NAME
					+ " where mark=?", new String[] { mark });
			user = new ArrayList<UserBean>();
			while (cursor.moveToNext()) {
				String username = cursor.getString(cursor
						.getColumnIndex("account"));
				String password = cursor.getString(cursor
						.getColumnIndex("password"));
				String sessionKey = cursor.getString(cursor
						.getColumnIndex("session_key"));
				String user_mark = cursor.getString(cursor
						.getColumnIndex("mark"));

				String accountType = cursor.getString(cursor
						.getColumnIndex("accountType"));
				String openId = cursor.getString(cursor
						.getColumnIndex("openId"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String sex = cursor.getString(cursor.getColumnIndex("sex"));
				String headUrlPath = cursor.getString(cursor
						.getColumnIndex("headUrlPath"));
				String oauth_token = cursor.getString(cursor
						.getColumnIndex("oauth_token"));
				String access_Token = cursor.getString(cursor
						.getColumnIndex("access_Token"));
				String access_Token_secret = cursor.getString(cursor
						.getColumnIndex("access_Token_secret"));
				UserBean bean = new UserBean(username, password, sessionKey,
						user_mark);
				// accountType,openId,name,sex,headUrlPath,access_Token,access_Token_secret
				bean.setAccountType(accountType);
				bean.setOpenId(openId);
				bean.setName(name);
				bean.setSex(sex);
				bean.setHeadUrlPath(headUrlPath);
				bean.setOauth_token(oauth_token);
				bean.setAccess_Token(access_Token);
				bean.setAccess_Token_secret(access_Token_secret);
				user.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (cursor != null)
					cursor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (rDB != null)
					rDB.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return user;
	}

	/**
	 * 修改用户信息中的标记
	 * 
	 * @param user_name
	 */
	public void updateUserMark(String session_key) {
		Cursor cursor = null;
		try {
			wDB = this.openWDB();
			cursor = wDB.rawQuery("select * from " + TABLE_NAME, null);
			List<String> session_keys = new ArrayList<String>();
			while (cursor.moveToNext()) {
				String sKey = cursor.getString(cursor
						.getColumnIndex("session_key"));
				session_keys.add(sKey);
			}
			for (String sKey : session_keys) {
				if (session_key.equals(sKey)) { // 如果查询出的用户名和传入的用户名一样则把mark修改为true
					wDB.execSQL("update " + TABLE_NAME
							+ " set mark='yes' where session_key='"
							+ session_key + "'");
				} else {
					wDB.execSQL("update " + TABLE_NAME
							+ " set mark='no' where session_key='" + sKey + "'");
				}
			}
		} finally {
			if (cursor != null)
				cursor.close();
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 修改上传通信录时间
	 * 
	 * @param user_name
	 */
	public void updateTime(String session_key, String time) {
		L.i(TAG, "updateTime session_key=" + session_key + ",time=" + time);
		try {
			wDB = this.openWDB();
			wDB.execSQL("update " + TABLE_NAME
					+ " set uploadcontacts_time = ? where session_key = ?",
					new String[] { time, session_key });
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 修改名字
	 * 
	 * @param user_name
	 */
	public void updateName(String session_key, String name) {
		try {
			wDB = this.openWDB();
			wDB.execSQL("update " + TABLE_NAME
					+ " set name = ? where session_key = ?", new String[] {
					name, session_key });
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 查询登录者名字
	 * 
	 * @param user_name
	 */
	public String getName(String session_key) {
		Cursor cursor = null;
		String name = null;
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select name from " + TABLE_NAME
					+ " where session_key = ?", new String[] { session_key });
			cursor.moveToFirst();
			name = cursor.getString(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return name;
	}

	/**
	 * 修改头像
	 * 
	 * 存原图url
	 * 
	 * @param session_key
	 * @param headUrlPath
	 */
	public void updateHeadUrlPath(String session_key, String headUrlPath) {
		try {
			wDB = this.openWDB();
			wDB.execSQL("update " + TABLE_NAME
					+ " set headUrlPath = ? where session_key = ?",
					new String[] { headUrlPath, session_key });
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 查询登录者头像
	 * 
	 * @param user_name
	 */
	public String getHeadUrlPath(String session_key) {
		Cursor cursor = null;
		String headUrlPath = null;
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select headUrlPath from " + TABLE_NAME
					+ " where session_key = ?", new String[] { session_key });
			cursor.moveToFirst();
			headUrlPath = cursor.getString(0);
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return headUrlPath;
	}

	/**
	 * 获得上传通信录时间
	 * 
	 * @param name
	 * @return
	 */
	public String getUploadTime(String session_key) {
		Cursor cursor = null;
		String time = null;
		try {
			rDB = this.openRDB();

			cursor = rDB.rawQuery("select uploadcontacts_time from "
					+ TABLE_NAME + " where session_key = ?",
					new String[] { session_key });
			cursor.moveToFirst();
			time = cursor.getString(0);
			L.d(TAG, "updateTime session_key=" + session_key + ",time=" + time);
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return time;
	}

	/**
	 * 修改用户信息中的密码
	 * 
	 * @param session_key
	 * @param password
	 */
	public void updateUserPassword(String session_key, String password) {
		try {
			wDB = this.openWDB();
			wDB.execSQL("update " + TABLE_NAME
					+ " set password=? where session_key=?", new String[] {
					password, session_key });
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 保存备份、恢复通信录时间
	 * 
	 * @param session_key
	 * @param isBackupOrRenew
	 *            true：备份,false：恢复
	 * @param time
	 */
	public void saveBackupOrRenewContactsTime(String session_key,
			Boolean isBackupOrRenew, long time) {
		try {
			wDB = this.openWDB();
			String sql = "update " + TABLE_NAME + " set %s = " + time
					+ " where session_key = ?";
			if (isBackupOrRenew) {
				sql = String.format(sql, "backupcontacts_time");
			} else {
				sql = String.format(sql, "renewcontacts_time");
			}
			wDB.execSQL(sql, new String[] { session_key });
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 获得备份、恢复通信录时间
	 * 
	 * @param session_key
	 * @param isBackupOrRenew
	 *            true：备份,false：恢复
	 * @return
	 */
	public long getBackupOrRenewContactsTime(String session_key,
			Boolean isBackupOrRenew) {
		Cursor cursor = null;
		long time;
		try {
			rDB = this.openRDB();
			String sql = "select %s from " + TABLE_NAME
					+ " where session_key = ?";
			if (isBackupOrRenew) {
				sql = String.format(sql, "backupcontacts_time");
			} else {
				sql = String.format(sql, "renewcontacts_time");
			}
			cursor = rDB.rawQuery(sql, new String[] { session_key });
			cursor.moveToFirst();
			time = cursor.getLong(0);
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return time;
	}

	/**
	 * 更新超过几周没备份了
	 * 
	 * @param session_key
	 * @param weeks
	 */
	public void saveIntData(String session_key, String key, int weeks) {
		try {
			wDB = this.openWDB();
			String sql = "update " + TABLE_NAME + " set " + key + " = " + weeks
					+ " where session_key = ?";
			wDB.execSQL(sql, new String[] { session_key });
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 获得超过几周没备份了
	 * 
	 * @param session_key
	 * @param weeks
	 */
	public int getIntData(String session_key, String key) {
		Cursor cursor = null;
		int weeks;
		try {
			rDB = this.openRDB();
			String sql = "select " + key + " from " + TABLE_NAME
					+ " where session_key = ?";
			cursor = rDB.rawQuery(sql, new String[] { session_key });
			cursor.moveToFirst();
			weeks = cursor.getInt(0);
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return weeks;
	}

	/**
	 * 更新超过几周没备份了
	 * 
	 * @param session_key
	 * @param weeks
	 */
	public void saveLongData(String session_key, String key, long value) {
		try {
			wDB = this.openWDB();
			String sql = "update " + TABLE_NAME + " set " + key + " = " + value
					+ " where session_key = ?";
			wDB.execSQL(sql, new String[] { session_key });
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 获得超过几周没备份了
	 * 
	 * @param session_key
	 * @param weeks
	 */
	public long getLongData(String session_key, String key) {
		Cursor cursor = null;
		long value;
		try {
			rDB = this.openRDB();
			String sql = "select " + key + " from " + TABLE_NAME
					+ " where session_key = ?";
			cursor = rDB.rawQuery(sql, new String[] { session_key });
			cursor.moveToFirst();
			value = cursor.getLong(0);
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return value;
	}

	/**
	 * 保存朋友列表更新时间
	 * 
	 * @param session_key
	 * @param startUpdateTime
	 */
	public void saveStartUpdateTime(String session_key, long startUpdateTime) {
		try {
			wDB = this.openWDB();
			String sql = "update " + TABLE_NAME + " set startUpdateTime = "
					+ startUpdateTime + " where session_key = ?";
			wDB.execSQL(sql, new String[] { session_key });
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 获得朋友列表更新时间
	 * 
	 * @param session_key
	 * @return
	 */
	public long getStartUpdateTime(String session_key) {
		Cursor cursor = null;
		long startUpdateTime;
		try {
			rDB = this.openRDB();
			String sql = "select startUpdateTime from " + TABLE_NAME
					+ " where session_key = ?";
			cursor = rDB.rawQuery(sql, new String[] { session_key });
			cursor.moveToFirst();
			startUpdateTime = cursor.getLong(0);
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return startUpdateTime;
	}

	/**
	 * 保存朋友列表删除时间
	 * 
	 * @param session_key
	 * @param startDeleteTime
	 */
	public void saveStartDeleteTime(String session_key, long startDeleteTime) {
		try {
			wDB = this.openWDB();
			String sql = "update " + TABLE_NAME + " set startDeleteTime = "
					+ startDeleteTime + " where session_key = ?";
			wDB.execSQL(sql, new String[] { session_key });
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 获得朋友列表删除时间
	 * 
	 * @param session_key
	 * @return
	 */
	public long getStartDeleteTime(String session_key) {
		Cursor cursor = null;
		long startDeleteTime;
		try {
			rDB = this.openRDB();
			String sql = "select startDeleteTime from " + TABLE_NAME
					+ " where session_key = ?";
			cursor = rDB.rawQuery(sql, new String[] { session_key });
			cursor.moveToFirst();
			startDeleteTime = cursor.getLong(0);
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return startDeleteTime;
	}
}
