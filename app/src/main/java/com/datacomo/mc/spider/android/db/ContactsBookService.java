package com.datacomo.mc.spider.android.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;

import com.datacomo.mc.spider.android.bean.ContactInfo;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.PinYin4JCn;

public class ContactsBookService extends DataBaseService {
	protected static final String TAG = "ContactsBookService";

	private String TABLE_NAME = DataBaseHelper.CONTACTS_BOOK;

	public ContactsBookService(Context context) {
		super(context);
	}

	/**
	 * 保存联系人
	 * 
	 * @param contactInfos
	 */
	public void save(ArrayList<ContactInfo> contactInfos) {
		try {
			wDB = this.openWDB();
			if (contactInfos != null && contactInfos.size() > 0) {
				for (int i = 0; i < contactInfos.size(); i++) {
					ContactInfo contactInfo = contactInfos.get(i);
					if (contactInfo != null) {
						wDB.execSQL(
								"insert into "
										+ TABLE_NAME
										+ "(name,number,RegisterStatus,FriendStatus,contactMemberId,memberHead) "
										+ "values(?,?,?,?,?,?)",
								new Object[] { contactInfo.getName(),
										contactInfo.getNumber(),
										contactInfo.getRegisterStatus(),
										contactInfo.getFriendStatus(),
										contactInfo.getContactMemberId(),
										contactInfo.getMemberHead() });
					}
				}
			}
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
	 * 查询列表
	 * 
	 * @param registerStatus
	 * @return
	 */
	public ArrayList<ContactInfo> getContacts(String registerStatus) {
		Cursor cursor = null;
		ArrayList<ContactInfo> contactInfos = null;
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select * from  " + TABLE_NAME
					+ " where RegisterStatus = ?",
					new String[] { registerStatus });
			if (cursor != null && cursor.moveToFirst()) {
				contactInfos = new ArrayList<ContactInfo>();
				do {
					ContactInfo contactInfo = new ContactInfo(
							cursor.getString(1), cursor.getString(2));
					contactInfo.setRegisterStatus(cursor.getString(3));
					contactInfo.setFriendStatus(cursor.getString(4));
					contactInfo.setContactMemberId(cursor.getInt(5));
					contactInfo.setMemberHead(cursor.getString(6));
					contactInfos.add(contactInfo);
				} while (cursor.moveToNext());
			}
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return contactInfos;
	}

	/**
	 * 查询列表
	 * 
	 * @param registerStatus
	 * @return
	 */
	public ArrayList<ContactInfo> getContactLists(int memberId) {
		Cursor cursor = null;
		ArrayList<ContactInfo> contactInfos = null;
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select * from  " + TABLE_NAME
					+ " where FriendStatus = 3", new String[] {});
			if (cursor != null && cursor.moveToFirst()) {
				contactInfos = new ArrayList<ContactInfo>();
				do {
					int id = cursor.getInt(5);
					if (id != memberId) {
						ContactInfo contactInfo = new ContactInfo(
								cursor.getString(1), cursor.getString(2));
						contactInfo.setRegisterStatus(cursor.getString(3));
						contactInfo.setFriendStatus(cursor.getString(4));
						contactInfo.setContactMemberId(cursor.getInt(5));
						contactInfo.setMemberHead(cursor.getString(6));
						contactInfos.add(contactInfo);
					}
				} while (cursor.moveToNext());
			}
			try {
				if (contactInfos != null && contactInfos.size() > 1)
					Collections.sort(contactInfos,
							new Comparator<ContactInfo>() {
								@Override
								public int compare(ContactInfo bean1,
										ContactInfo bean2) {
									return PinYin4JCn
											.convertPy("leaguer",
													bean1.getName())
											.toLowerCase()
											.compareTo(
													PinYin4JCn.convertPy(
															"leaguer",
															bean2.getName())
															.toLowerCase());
								}
							});
			} catch (Exception e) {
				e.printStackTrace();
			}

			ArrayList<ContactInfo> cs = new ArrayList<ContactInfo>();
			ArrayList<Integer> is = new ArrayList<Integer>();
			for (int i = 0; i < contactInfos.size(); i++) {
				ContactInfo contactInfo = contactInfos.get(i);
				String py = PinYin4JCn.convertPy("leaguer",
						contactInfo.getName()).toLowerCase();
				L.d(TAG, "py=" + py);
				if (py != null && py.length() > 0) {
					char c = py.trim().substring(0, 1).charAt(0);
					Pattern pattern = Pattern.compile("[a-zA-Z]{1}+$");
					if (pattern.matcher(c + "").matches()) {
						L.i(TAG, "字母");
					} else {
						cs.add(contactInfo);
						is.add(i);
						L.i(TAG, "数字");
					}
				} else {
					cs.add(contactInfo);
					is.add(i);
					L.i(TAG, "数字");
				}
			}
			for (int i = is.size(); i > 0; i--) {
				contactInfos.remove(i-1);
			}
			contactInfos.addAll(cs);
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return contactInfos;
	}

	/**
	 * 查询列表
	 * 
	 * @param registerStatus
	 * @return
	 */
	public ArrayList<ContactInfo> getContactLists(String registerStatus,
			int memberId) {
		Cursor cursor = null;
		ArrayList<ContactInfo> contactInfos = null;
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select * from  " + TABLE_NAME
					+ " where RegisterStatus = ? and FriendStatus = 3",
					new String[] { registerStatus });
			if (cursor != null && cursor.moveToFirst()) {
				contactInfos = new ArrayList<ContactInfo>();
				do {
					int id = cursor.getInt(5);
					if (id != memberId) {
						ContactInfo contactInfo = new ContactInfo(
								cursor.getString(1), cursor.getString(2));
						contactInfo.setRegisterStatus(cursor.getString(3));
						contactInfo.setFriendStatus(cursor.getString(4));
						contactInfo.setContactMemberId(cursor.getInt(5));
						contactInfo.setMemberHead(cursor.getString(6));
						contactInfos.add(contactInfo);
					}
				} while (cursor.moveToNext());
			}
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return contactInfos;
	}

	/**
	 * 查询头像列表
	 * 
	 * @return
	 */
	public HashMap<String, String[]> getContactHeads() {
		Cursor cursor = null;
		HashMap<String, String[]> map = new HashMap<String, String[]>();
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select * from " + TABLE_NAME, null);
			if (cursor != null && cursor.moveToFirst()) {
				do {
					String head = cursor.getString(cursor
							.getColumnIndex("memberHead"));
					if (head != null && !"".equals(head)) {
						map.put(cursor.getString(cursor
								.getColumnIndex("number")),
								new String[] {
										head,
										cursor.getString(cursor
												.getColumnIndex("name")) });
					}
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
	 * 更新注册状态
	 * 
	 * @param nums
	 */
	public void updateRegisterStatus(String[] nums) {
		try {
			wDB = this.openWDB();
			for (String num : nums) {
				wDB.execSQL("update " + TABLE_NAME
						+ " set RegisterStatus = 1 where number = ?",
						new String[] { num });
				wDB.execSQL("update " + TABLE_NAME
						+ " set FriendStatus = 1 where number = ?",
						new String[] { num });
			}
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 更新朋友状态
	 * 
	 * @param friendStatus
	 *            朋友状态：1：是朋友;2：申请加为朋友;3：非朋友
	 * @param number
	 */
	public void updateFriendStatus(String friendStatus, String number) {
		try {
			wDB = this.openWDB();
			wDB.execSQL("update " + TABLE_NAME
					+ " set FriendStatus = ? where number = ?", new String[] {
					friendStatus, number });
		} finally {
			if (wDB != null)
				wDB.close();
		}
	}

	/**
	 * 按号码查名字
	 * 
	 * @param phone
	 * @return
	 */
	public String getContactName(String phone) {
		Cursor cursor = null;
		String name = null;
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select * from  " + TABLE_NAME
					+ " where number = ?", new String[] { phone });
			if (cursor != null && cursor.moveToFirst()) {
				do {
					name = cursor.getString(cursor.getColumnIndex("name"));
				} while (cursor.moveToNext());
			}
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return name;
	}

	/**
	 * 获取注册和未注册数量
	 * 
	 * @param registerStatus
	 * @return
	 */
	public int getCount(String registerStatus) {
		Cursor cursor = null;
		int count = 0;
		try {
			rDB = this.openRDB();
			cursor = rDB.rawQuery("select count(0) from " + TABLE_NAME
					+ " where RegisterStatus = ?",
					new String[] { registerStatus });
			cursor.moveToFirst();
			count = Integer.parseInt(cursor.getString(0));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (rDB != null)
				rDB.close();
		}
		return count;
	}
}
