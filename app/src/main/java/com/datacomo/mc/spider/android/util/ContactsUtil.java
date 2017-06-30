package com.datacomo.mc.spider.android.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.util.Log;

import com.datacomo.mc.spider.android.bean.CAddress;
import com.datacomo.mc.spider.android.bean.CEmail;
import com.datacomo.mc.spider.android.bean.CPhone;
import com.datacomo.mc.spider.android.bean.ContactEntity;
import com.datacomo.mc.spider.android.bean.ContactGroupEntity;
import com.datacomo.mc.spider.android.url.L;

public class ContactsUtil {
	private static String TAG = "ContactsUtil";

	/**
	 * 读取手机联系人
	 * 
	 * Phone.TYPE_ASSISTANT : 19 Phone.TYPE_CALLBACK : 8 Phone.TYPE_CAR : 9
	 * Phone.TYPE_COMPANY_MAIN : 10 Phone.TYPE_FAX_HOME : 5 Phone.TYPE_FAX_WORK
	 * : 4 Phone.TYPE_HOME : 1 Phone.TYPE_ISDN : 11 Phone.TYPE_MAIN : 12
	 * Phone.TYPE_MMS : 20 Phone.TYPE_MOBILE : 2 Phone.TYPE_OTHER : 7
	 * Phone.TYPE_OTHER_FAX : 13 Phone.TYPE_PAGER : 6 Phone.TYPE_RADIO : 14
	 * Phone.TYPE_TELEX : 15 Phone.TYPE_TTY_TDD : 16 Phone.TYPE_WORK : 3
	 * Phone.TYPE_WORK_MOBILE : 17 Phone.TYPE_WORK_PAGER : 18 Phone.TYPE_CUSTOM
	 * : 0
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static ArrayList<ContactEntity> getPhonebook(Context context,
			Handler handler) {
		String[] projection = { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.HAS_PHONE_NUMBER,
				ContactsContract.Data.STARRED };
		Cursor cursor = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, projection, null, null,
				ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ");
		int size = cursor.getCount();
		Log.i(TAG, "getPhonebook size=" + size);
		List<ContactEntity> contactList = new ArrayList<ContactEntity>();
		if (cursor.moveToFirst()) {
			int i = 0;
			do {
				// Log.d(TAG, "getPhonebook i=" + i);
				if (handler != null) {
					Message msg = new Message();
					msg.what = 0;
					msg.arg1 = ++i;
					msg.arg2 = size;
					handler.sendMessage(msg);
				}
				String contact_id = cursor.getString(0);
				String contact_hasPhone = cursor.getString(2);
				String contact_name = cursor.getString(1);
				String contact_starred = cursor.getString(3);
				// L.i(TAG, "====>CONTACT: ID=" + contact_id);
				// L.i(TAG, "CONTACT: NAME" + contact_name);
				// L.i(TAG, "CONTACT: STARRED" + contact_starred);
				ContactEntity entity = null;

				if (contact_name == null) {
					continue;
				} else {
					entity = new ContactEntity(contact_name);
					entity.setContactId(contact_id);
					try {
						entity.setStarred(Integer.valueOf(contact_starred));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// // 获取邮箱信息
				// Cursor emailCur = context.getContentResolver().query(
				// ContactsContract.CommonDataKinds.Email.CONTENT_URI,
				// null,
				// ContactsContract.CommonDataKinds.Email.CONTACT_ID
				// + " = ?", new String[] { contact_id }, null);
				// ArrayList<CEmail> emails = new ArrayList<CEmail>();
				// if (null != emailCur && emailCur.moveToFirst()) {
				// do {
				// String typeStr = emailCur
				// .getString(emailCur
				// .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
				// String email = emailCur
				// .getString(emailCur
				// .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
				// Integer type = Integer.valueOf(typeStr);
				// CEmail eml = new CEmail(email, type);
				// if (Email.TYPE_CUSTOM == type) {
				// String label = emailCur
				// .getString(emailCur
				// .getColumnIndex(ContactsContract.CommonDataKinds.Email.LABEL));
				// eml.setLabel(label);
				// }
				// L.i(TAG, "--->CONTACT: EMAILE" + eml.getEmail());
				// L.i(TAG, "CONTACT: EMAILEType" + eml.getType());
				// L.i(TAG, "CONTACT: EMAILELabel" + eml.getLabel());
				// emails.add(eml);
				// } while (emailCur.moveToNext());
				// }
				// emailCur.close();
				// entity.setEmails(emails);

				// 获取电话信息
				if ("1".equals(contact_hasPhone)) {
					ArrayList<CPhone> phoneList = getContactPhonesVarContactId(
							context, contact_id);
					entity.setNumbers(phoneList);
				}

				// // 获取组织信息 (暂时只取第一条)
				// Cursor orgCur = context
				// .getContentResolver()
				// .query(ContactsContract.Data.CONTENT_URI,
				// null,
				// ContactsContract.CommonDataKinds.Phone.CONTACT_ID
				// + " = ?"
				// + " AND "
				// + ContactsContract.Data.MIMETYPE
				// + " = ?",
				// new String[] {
				// contact_id,
				// ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
				// },
				// null);
				// if (orgCur.moveToFirst()) {
				// String orgName = orgCur
				// .getString(orgCur
				// .getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
				// String title = orgCur
				// .getString(orgCur
				// .getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
				// String type = orgCur
				// .getString(orgCur
				// .getColumnIndex(ContactsContract.CommonDataKinds.Organization.TYPE));
				// L.i(TAG, "CONTACT: ORGName" + orgName);
				// L.i(TAG, "CONTACT: ORGtitle" + title);
				// // L.i(TAG, "CONTACT: ORGtype" + type);
				// entity.setOrgName(orgName);
				// entity.setOrgTitle(title);
				// }
				// orgCur.close();
				//
				// // 获取备注信息
				//
				// String where = ContactsContract.Data.CONTACT_ID + " = ? AND "
				// + ContactsContract.Data.MIMETYPE + " = ?";
				// String[] whereParameters = new String[] { contact_id,
				// ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE };
				// Cursor noteCur = context.getContentResolver().query(
				// ContactsContract.Data.CONTENT_URI, null, where,
				// whereParameters, null);
				// if (noteCur.moveToFirst()) {
				// String note = noteCur
				// .getString(noteCur
				// .getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
				// entity.setNote(note);
				// }
				// noteCur.close();
				//
				// // 获取地址信息
				// ArrayList<CAddress> addrList = new ArrayList<CAddress>();

				// String whereAddr = ContactsContract.Data.CONTACT_ID
				// + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
				// String[] whereParametersAddr = new String[] {
				// contact_id,
				// ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE
				// };
				//
				// Cursor addrCur = context.getContentResolver().query(
				// ContactsContract.Data.CONTENT_URI, null, whereAddr,
				// whereParametersAddr, null);
				// while (addrCur.moveToNext()) {
				// String poBox = addrCur
				// .getString(addrCur
				// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
				// String street = addrCur
				// .getString(addrCur
				// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
				// String city = addrCur
				// .getString(addrCur
				// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
				// String state = addrCur
				// .getString(addrCur
				// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
				// String postalCode = addrCur
				// .getString(addrCur
				// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
				// String country = addrCur
				// .getString(addrCur
				// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
				// String type = addrCur
				// .getString(addrCur
				// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
				// CAddress a = new CAddress(poBox, street, city, state,
				// postalCode, country, Integer.valueOf(type));
				// if ("0".equals(type)) {
				// String label = addrCur
				// .getString(addrCur
				// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.LABEL));
				// a.setLabel(label);
				// }
				// addrList.add(a);
				// }
				// entity.setAddrs(addrList);
				// addrCur.close();

				contactList.add(entity);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return (ArrayList<ContactEntity>) contactList;
	}

	private static ArrayList<CPhone> getContactPhonesVarContactId(
			Context context, String contactId) {
		Cursor PhoneCur = context.getContentResolver().query(
				Phone.CONTENT_URI, null,
				Phone.CONTACT_ID + " = ?",
				new String[] { contactId }, null);
		ArrayList<CPhone> phoneList = new ArrayList<CPhone>();
		if (PhoneCur.getCount() > 0 && PhoneCur.moveToFirst()) {
			do {
				String number = PhoneCur
						.getString(PhoneCur
								.getColumnIndex(Phone.NUMBER));
				number = formartPhone(number);

				String typeStr = PhoneCur
						.getString(PhoneCur
								.getColumnIndex(Phone.TYPE));
				if (null != number && !"".equals(number.trim())) {
					try {
						Integer phoneType = Integer.valueOf(typeStr);
						CPhone phone = new CPhone(number, phoneType);
						if (phoneType == Phone.TYPE_CUSTOM) {
							String label = PhoneCur
									.getString(PhoneCur
											.getColumnIndex(Phone.LABEL));
							phone.setLabel(label);
						}
						phoneList.add(phone);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} while (PhoneCur.moveToNext());
			PhoneCur.close();
		}
		return phoneList;
	}

	// 获取联系人总的电话数量
	public static int getAllPhonesCount(ArrayList<ContactEntity> contacts) {
		int count = 0;
		if (null == contacts) {
			return count;
		}
		for (ContactEntity entity : contacts) {
			count += entity.getNumbers().size();
		}
		return count;
	}

	/**
	 * 添加联系人
	 * 
	 * @throws Exception
	 */
	private static long insertContact(Context c, ContactEntity entity) {
		ContentResolver cr = c.getContentResolver();
		ContentValues values = new ContentValues();
		// 插入一个空contact并获取对应的rawContactUri
		Uri rawContactUri = cr.insert(
				ContactsContract.RawContacts.CONTENT_URI,
				values);
		if (rawContactUri != null) {
			long rawContactId = ContentUris.parseId(rawContactUri);
			// 插入姓名
			values.clear();
			values.put(
					ContactsContract.Contacts.Data.RAW_CONTACT_ID,
					rawContactId);
			values.put(
					ContactsContract.Contacts.Data.MIMETYPE,
					StructuredName.CONTENT_ITEM_TYPE);
			values.put(StructuredName.GIVEN_NAME, entity.getName());
			cr.insert(ContactsContract.Data.CONTENT_URI,
					values);

			values.clear();
			values.put(ContactsContract.Data.STARRED, entity.getStarred());
			cr.update(ContactsContract.Contacts.CONTENT_URI, values,
					ContactsContract.Contacts._ID + "=" + rawContactId, null);

			// 插入电话
			ArrayList<CPhone> numList = entity.getNumbers();
			if (null != numList) {
				for (int i = 0; i < numList.size(); i++) {
					CPhone phone = numList.get(i);
					Integer type = phone.getType();
					values.clear();
					values.put(
							ContactsContract.Contacts.Data.RAW_CONTACT_ID,
							rawContactId);
					values.put(
							ContactsContract.Contacts.Data.MIMETYPE,
							Phone.CONTENT_ITEM_TYPE);
					values.put(Phone.NUMBER, phone.getPhone());
					// L.i(TAG, "insertContact Phone.NUMBER=" +
					// phone.getPhone());
					values.put(Phone.TYPE, type);
					if (Phone.TYPE_CUSTOM == type && null != phone.getLabel()) {
						values.put(Phone.LABEL, phone.getLabel());
					}
					cr.insert(
							ContactsContract.Data.CONTENT_URI,
							values);
				}
			}

			// 插入邮箱
			ArrayList<CEmail> emails = entity.getEmails();
			if (null != emails) {
				for (int i = 0; i < emails.size(); i++) {
					CEmail email = emails.get(i);
					Integer type = email.getType();
					values.clear();
					values.put(
							ContactsContract.Contacts.Data.RAW_CONTACT_ID,
							rawContactId);
					values.put(
							ContactsContract.Contacts.Data.MIMETYPE,
							Email.CONTENT_ITEM_TYPE);
					values.put(Email.DATA, email.getEmail());
					values.put(Email.TYPE, type);
					if (Email.TYPE_CUSTOM == type && null != email.getLabel()) {
						values.put(Phone.LABEL, email.getLabel());
					}
					cr.insert(
							ContactsContract.Data.CONTENT_URI,
							values);
				}
			}

			// 插入地址
			values.clear();
			ArrayList<CAddress> addrs = entity.getAddrs();
			if (null != entity.getAddrs()) {
				for (int i = 0; i < addrs.size(); i++) {
					CAddress addr = addrs.get(i);
					values.put(ContactsContract.Data.RAW_CONTACT_ID,
							rawContactId);
					values.put(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
					values.put(
							ContactsContract.CommonDataKinds.StructuredPostal.STREET,
							addr.getStreet());
					values.put(
							ContactsContract.CommonDataKinds.StructuredPostal.CITY,
							addr.getCity());
					values.put(
							ContactsContract.CommonDataKinds.StructuredPostal.REGION,
							addr.getState());
					values.put(
							ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
							addr.getPostalCode());
					values.put(
							ContactsContract.CommonDataKinds.StructuredPostal.POBOX,
							addr.getPoBox());
					values.put(
							ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
							addr.getType());
					if (ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM == addr
							.getType()) {
						values.put(Phone.LABEL, addr.getLabel());
					}
					cr.insert(
							ContactsContract.Data.CONTENT_URI,
							values);
				}
				values.clear();
				if (!"".equals(entity.getOrgName())
						|| !"".equals(entity.getOrgTitle())) {
					values.put(ContactsContract.Data.RAW_CONTACT_ID,
							rawContactId);
					values.put(
							ContactsContract.Data.MIMETYPE,
							Organization.CONTENT_ITEM_TYPE);
					values.put(
							Organization.COMPANY,
							entity.getOrgName());
					values.put(
							Organization.TITLE,
							entity.getOrgTitle());
					cr.insert(
							ContactsContract.Data.CONTENT_URI,
							values);
				}

				values.clear();
				if (!"".equals(entity.getNote())) {
					values.put(ContactsContract.Data.RAW_CONTACT_ID,
							rawContactId);
					values.put(
							ContactsContract.Data.MIMETYPE,
							Note.CONTENT_ITEM_TYPE);
					values.put(Note.NOTE,
							entity.getNote());
					cr.insert(
							ContactsContract.Data.CONTENT_URI,
							values);

				}
			}
			return ContentUris.parseId(rawContactUri);
		}
		return 0;
	}

	private static String getRawId(Context c, String contact_id) {
		String[] projection = { ContactsContract.RawContacts._ID };
		Cursor cursor = c.getContentResolver().query(
				ContactsContract.RawContacts.CONTENT_URI, projection,
				ContactsContract.RawContacts.CONTACT_ID + "=?",
				new String[] { contact_id }, null);
		if (cursor.moveToFirst()) {
			String rawId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.RawContacts._ID));
			return rawId;
		}
		return "-1";
	}

	private static String getContactIdByPhone(Context c, String phone) {
		try {
			ContentResolver resolver = c.getContentResolver();
			Uri uri = Uri
					.parse("content://com.android.contacts/data/phones/filter/"
							+ phone);
			Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
					null, null, null);
			if (null != cursor && cursor.moveToNext()) {
				return cursor.getString(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "-1";
	}

	/**
	 * 给联系人添加电话
	 * 
	 * @throws Exception
	 */
	private static void insertAPhone(Context c, String id, CPhone number,
			String name) {
		String phoneNum = number.getPhone();
		// L.i(TAG, "PHONE-INSERT id=" + id + " NAME=" + name + " NUMBER"
		// + phoneNum);
		ContentResolver cr = c.getContentResolver();
		ContentValues values = new ContentValues();
		if (null == id || "".equals(id)) {
			return;
		}
		Uri rawContactUri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));

		if (rawContactUri != null) {
			values.clear();
			values.put(
					ContactsContract.Contacts.Data.RAW_CONTACT_ID,
					getRawId(c, id));
			values.put(
					ContactsContract.Contacts.Data.MIMETYPE,
					Phone.CONTENT_ITEM_TYPE);
			values.put(Phone.NUMBER, phoneNum);
			values.put(Phone.TYPE, number.getType());
			if (Phone.TYPE_CUSTOM == number.getType()) {
				values.put(Phone.LABEL, number.getLabel());
			}
			cr.insert(ContactsContract.Data.CONTENT_URI,
					values);
		}
	}

	/**
	 * 根据号码修改头像
	 * 
	 * @param c
	 * @param phoneNum
	 * @param head
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public static void changeMemberHead(Context c, String name,
			String phoneNum, String head) throws MalformedURLException,
			IOException, Exception {
		L.i(TAG, "changeMemberHead phoneNum=" + phoneNum + ",head=" + head);
		try {
			long id = Long
					.valueOf(getRawId(c, getContactIdByPhone(c, phoneNum)));
			if (head != null && !"".equals(head)) {
				ContactsUtil.changeHead(c, id,
						getBytes(new URL(head).openStream()), name, phoneNum);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取sim卡（主卡）联系人_id集合
	 * 
	 * @param context
	 * @return
	 */
	private static ContactEntity isNumberInSimCard(Context context,
			String number) {
		if (HardwareStateUtil.isSimEnable(context)) {
			Cursor cursor = context.getContentResolver().query(
					Uri.parse("content://icc/adn"), null, null, null, null);
			if (null != cursor && cursor.moveToFirst()) {
				do {
					String num = cursor.getString(cursor
							.getColumnIndex("number"));
					if (num != null && number.equals(num)) {
						String name = cursor.getString(cursor
								.getColumnIndex("name"));
						return new ContactEntity(name, number);
					}
				} while (cursor.moveToNext());
			}
		}
		return null;
	}

	/**
	 * 修改指定联系人头像
	 * 
	 * @param cr
	 * @param contactId
	 * @param avatar
	 */
	private static void changeHead(Context c, long contactId, byte[] avatar,
			String name, String phoneNum) {
		ContentValues values = getContentValues(contactId, avatar);
		ContentResolver cr = c.getContentResolver();
		int photoRow = getPhotoRow(cr, contactId);
		L.d(TAG, "changeHead photoRow=" + photoRow + ",phoneNum=" + phoneNum
				+ ",contactId=" + contactId);
		ContactEntity entity = isNumberInSimCard(c, phoneNum);
		if (null != entity) { // 此联系人存在sim卡（主卡）中
			int row = deleteContactVarId(c, contactId);
			if (row != 0) {
				long newContactId = insertContact(c, entity);
				ContentValues newValues = getContentValues(newContactId, avatar);
				cr.insert(ContactsContract.Data.CONTENT_URI, newValues);
			}
		} else {// 此联系人存在手机中
			if (photoRow >= 0) {
				cr.update(ContactsContract.Data.CONTENT_URI, values,
						ContactsContract.Data._ID + " = " + photoRow, null);
			} else {
				Uri url = cr.insert(ContactsContract.Data.CONTENT_URI, values);
				L.d(TAG,
						"changeHead url=" + url + " "
								+ getPhotoRow(cr, contactId));
			}
		}
	}

	/**
	 * 封装插入的联系人头像
	 * 
	 * @param contactId
	 * @param avatar
	 * @return
	 */
	private static ContentValues getContentValues(long contactId, byte[] avatar) {
		ContentValues values = new ContentValues();
		values.put(ContactsContract.Data.RAW_CONTACT_ID, contactId);
		values.put(ContactsContract.Data.IS_SUPER_PRIMARY, 1);
		values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, avatar);
		values.put(ContactsContract.Data.MIMETYPE,
				ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
		return values;
	}

	/**
	 * 联系人头像ID
	 * 
	 * @param cr
	 * @param contactId
	 * @return
	 */
	private static int getPhotoRow(ContentResolver cr, long contactId) {
		int photoRow = -1;
		String where = ContactsContract.Data.RAW_CONTACT_ID + " = " + contactId
				+ " AND " + ContactsContract.Data.MIMETYPE + "=='"
				+ ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
				+ "'";
		Cursor cursor = cr.query(ContactsContract.Data.CONTENT_URI, null,
				where, null, null);
		int idIdx = cursor.getColumnIndexOrThrow(ContactsContract.Data._ID);
		if (cursor.moveToFirst()) {
			photoRow = cursor.getInt(idIdx);
		}
		cursor.close();
		return photoRow;
	}

	/**
	 * 给联系人添加邮箱
	 * 
	 * @throws Exception
	 */
	private static void insertAEmail(Context c, String id, CEmail email,
			String name) {
		// L.i(TAG,
		// "EMAIL-INSERT id=" + id + " NAME=" + name + " EMAIL"
		// + email.getEmail());
		ContentResolver cr = c.getContentResolver();
		ContentValues values = new ContentValues();
		if (null == id || "".equals(id)) {
			return;
		}
		Uri rawContactUri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
		if (rawContactUri != null) {
			values.clear();
			values.put(
					ContactsContract.Contacts.Data.RAW_CONTACT_ID,
					getRawId(c, id));
			values.put(
					ContactsContract.Contacts.Data.MIMETYPE,
					Email.CONTENT_ITEM_TYPE);
			values.put(Email.DATA, email.getEmail());
			values.put(Email.TYPE, email.getType());
			if (Email.TYPE_CUSTOM == email.getType()) {
				values.put(Phone.LABEL, email.getLabel());
			}
			cr.insert(ContactsContract.Data.CONTENT_URI,
					values);
		}
	}

	/**
	 * 给联系人添加地址
	 * 
	 * @throws Exception
	 */
	private static void insertAAddress(Context c, String id, CAddress address,
			String name) {
		// L.i(TAG, "ADDRESS-INSERT id=" + id + " NAME=" + name + " ADRDDRESS"
		// + address.toString());
		ContentResolver cr = c.getContentResolver();
		ContentValues values = new ContentValues();
		if (null == id || "".equals(id)) {
			return;
		}
		Uri rawContactUri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
		if (rawContactUri != null) {
			values.clear();
			values.put(ContactsContract.Data.RAW_CONTACT_ID, getRawId(c, id));
			values.put(
					ContactsContract.Data.MIMETYPE,
					ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
			values.put(
					ContactsContract.CommonDataKinds.StructuredPostal.STREET,
					address.getStreet());
			values.put(ContactsContract.CommonDataKinds.StructuredPostal.CITY,
					address.getCity());
			values.put(
					ContactsContract.CommonDataKinds.StructuredPostal.REGION,
					address.getState());
			values.put(
					ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
					address.getPostalCode());
			values.put(ContactsContract.CommonDataKinds.StructuredPostal.POBOX,
					address.getPoBox());
			values.put(ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
					address.getType());
			if (ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM == address
					.getType()) {
				values.put(Phone.LABEL, address.getLabel());
			}
			cr.insert(ContactsContract.Data.CONTENT_URI,
					values);
		}
	}

	/**
	 * 给联系人添加社会信息
	 * 
	 * @throws Exception
	 */
	private static void insertAOrg(Context c, String id, String orgTitle,
			String orgName, String name) {
		// L.i(TAG, "ORG-INSERT id=" + id + " NAME=" + name + " TITLE： "
		// + orgTitle + " NAME： " + orgName);
		ContentResolver cr = c.getContentResolver();
		ContentValues values = new ContentValues();
		if (null == id || "".equals(id)) {
			return;
		}
		Uri rawContactUri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
		if (rawContactUri != null) {
			values.clear();
			values.put(
					ContactsContract.Contacts.Data.RAW_CONTACT_ID,
					getRawId(c, id));
			values.put(
					ContactsContract.Contacts.Data.MIMETYPE,
					Organization.CONTENT_ITEM_TYPE);
			values.put(Organization.COMPANY, orgName);
			values.put(Organization.TITLE, orgTitle);
			values.put(Organization.TYPE, Organization.TYPE_WORK);
			cr.insert(ContactsContract.Data.CONTENT_URI,
					values);
		}
	}

	/**
	 * 给联系人添加note
	 * 
	 * @throws Exception
	 */
	private static void insertANote(Context c, String id, String note,
			String name) {
		// L.i(TAG, "NOTE-INSERT id=" + id + " NAME=" + name + " NOTE： " +
		// note);
		ContentResolver cr = c.getContentResolver();
		ContentValues values = new ContentValues();
		if (null == id || "".equals(id)) {
			return;
		}
		Uri rawContactUri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
		if (rawContactUri != null) {
			values.clear();
			values.put(
					ContactsContract.Contacts.Data.RAW_CONTACT_ID,
					getRawId(c, id));
			values.put(
					ContactsContract.Contacts.Data.MIMETYPE,
					Note.CONTENT_ITEM_TYPE);
			values.put(Note.NOTE, note);
			cr.insert(ContactsContract.Data.CONTENT_URI,
					values);
		}
	}

	/**
	 * 删除本地通讯录
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unused")
	private static void deleteContacts(Context context) {
		String[] projection = { ContactsContract.Contacts._ID, };
		Cursor cursor = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, projection, null, null,
				null);
		if (null != cursor && cursor.getCount() > 0 && cursor.moveToFirst()) {
			do {
				String contact_id = cursor.getString(0);
				deleteContactVarId(context, Long.parseLong(contact_id));
			} while (cursor.moveToNext());
		}
	}

	/**
	 * 根据contactIid删除联系人
	 * 
	 * @param
	 * @return
	 */
	private static int deleteContactVarId(Context context, long contactId) {
		Uri uri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, contactId);
		ContentResolver cr = context.getContentResolver();
		return cr.delete(uri, null, null);
	}

	/**
	 * 根据contactIid删除sim联系人
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unused")
	private static int deleteContactInSimVarNumber(Context context,
			String number) {
		Uri uri = Uri.parse("content://icc/adn");
		Cursor cursor = context.getContentResolver().query(uri, null, null,
				null, null);
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String phoneNumber = cursor.getString(cursor
					.getColumnIndex("number"));
			String where = "number=?";
			return context.getContentResolver().delete(uri, where,
					new String[] { phoneNumber });
		}
		return -1;
	}

	/**
	 * 同步联系人
	 * 
	 * @param contactInfos
	 * @return int[0]: 新增联系人个数； int[1]: 更新数据条数 。
	 */
	public static int[] cogradientContact(Context context,
			ArrayList<ContactEntity> infos, Handler handler) {
		int updateCount = 0;
		int addCount = 0;
		if (null != infos) {
			// L.i(TAG, "cogradientContact 读取本地通讯录。。。");
			ArrayList<ContactEntity> entitis = ContactsUtil.getPhonebook(
					context, null);
			int x = 0, y = infos.size();
			 int s = 0;
			labelNextContact: for (ContactEntity info : infos) {
				 L.d(TAG, "cogradientContact s=" + ++s);
				if (handler != null) {
					++x;
					Message msg = new Message();
					msg.what = 0;
					msg.arg1 = x;
					msg.arg2 = y;
					handler.sendMessage(msg);
				}
				String name = info.getName();
				for (ContactEntity entity : entitis) {
					if (name.equals(entity.getName())) {
						String id = entity.getContactId();
						boolean updateFlag = false;
						labelNextPhone: for (int i = 0; i < info.getNumbers()
								.size(); i++) {
							String number = info.getNumbers().get(i).getPhone();
							for (CPhone phone : entity.getNumbers()) {
								if (number
										.equals(formartPhone(phone.getPhone()))) {
									continue labelNextPhone;
								}
							}
							insertAPhone(context, id, info.getNumbers().get(i),
									name);
							updateFlag = true;
						}

						ArrayList<CEmail> emails = info.getEmails();
						labelNextEmail: for (int i = 0; i < emails.size(); i++) {
							String email = emails.get(i).getEmail();
							for (CEmail cEmail : entity.getEmails()) {
								if (cEmail.getEmail().equals(email)) {
									continue labelNextEmail;
								}
							}
							insertAEmail(context, id, emails.get(i), name);
							updateFlag = true;
						}

						// ArrayList<CAddress> addrs = info.getAddrs();
						// for (int i = 0; i < addrs.size(); i++) {
						// if (0 >= entity.getAddrs().size()) {
						// insertAAddress(context, id, addrs.get(i), name);
						// updateFlag = true;
						// }
						// }

						if ("".equals(entity.getNote())) {
							String note = info.getNote();
							if (!"".equals(note)) {
								insertANote(context, id, note, name);
								updateFlag = true;
							}
						}

						if ("".equals(entity.getOrgName())
								&& "".equals(entity.getOrgTitle())) {
							String orgName = info.getOrgName();
							String orgTitle = info.getOrgTitle();
							if (!"".equals(orgName) || !"".equals(orgTitle)) {
								insertAOrg(context, id, orgTitle, orgName, name);
								updateFlag = true;
							}
						}

						if (updateFlag) {
							updateCount++;
						}
						continue labelNextContact;
					}
				}
				insertContact(context, info);
				addCount++;
			}

		}
		return new int[] { addCount, updateCount };
	}

	private static String formartPhone(String phone) {
		return phone.replaceAll("-", "");
	}

	/**
	 * backup groups
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unused")
	private static List<ContactGroupEntity> getGroups(Context context) {
		String[] RAW_PROJECTION = new String[] { ContactsContract.Groups._ID,
				ContactsContract.Groups.TITLE,
				ContactsContract.Groups.ACCOUNT_NAME,
				ContactsContract.Groups.ACCOUNT_TYPE,
				ContactsContract.Groups.DELETED };
		String RAW_CONTACTS_WHERE = ContactsContract.Groups.DELETED + " = ? ";
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(ContactsContract.Groups.CONTENT_URI,
				RAW_PROJECTION, RAW_CONTACTS_WHERE, new String[] { "" + 0 },
				null);
		List<ContactGroupEntity> entities_ContactGroup = new ArrayList<ContactGroupEntity>();
		ContactGroupEntity entity_ContactGroup = null;
		while (cursor.moveToNext()) {
			String groupId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Groups._ID));
			String groupName = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Groups.TITLE));
			entity_ContactGroup = new ContactGroupEntity(groupId, groupName);
			entity_ContactGroup.setmAccount_Name(cursor.getString(cursor
					.getColumnIndex(ContactsContract.Groups.ACCOUNT_NAME)));
			entity_ContactGroup.setmAccount_Type(cursor.getString(cursor
					.getColumnIndex(ContactsContract.Groups.ACCOUNT_TYPE)));
			entity_ContactGroup.setDelete(cursor.getString(cursor
					.getColumnIndex(ContactsContract.Groups.DELETED)));
			entities_ContactGroup.add(entity_ContactGroup);
		}
		cursor.close();
		return entities_ContactGroup;
	}

	/**
	 * get groupId which the contact's phone is specified number
	 * 
	 * @param context
	 * @param phone
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String getGroupIdByPhone(Context context, String phone) {
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri
				.parse("content://com.android.contacts/data/phones/filter/"
						+ phone);
		Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
				null, null, null);
		String contact_id = "-1";
		if (null != cursor && cursor.moveToNext()) {
			contact_id = cursor.getString(0);
		}
		Log.i(TAG, "contact_id" + contact_id);
		String[] projection = { ContactsContract.Data.DATA1 };
		String WHERE = ContactsContract.Data.RAW_CONTACT_ID
				+ " = ? "
				+ " and "
				+ ContactsContract.Data.MIMETYPE
				+ "="
				+ "'"
				+ ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE
				+ "'";
		cursor = resolver.query(ContactsContract.Data.CONTENT_URI, projection,
				WHERE, new String[] { contact_id }, null);
		int size = cursor.getCount();
		String group = "-1";
		if (cursor.moveToFirst()) {
			group = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Data.DATA1));// 获取摸个联系人对应的分组ID，如果没有测返回-1并保存
		}
		cursor.close();
		return String.valueOf(group);
	}

	/**
	 * recovery Contact's groupId
	 * 
	 * @param context
	 * @param phone
	 * @param groupId
	 * @return
	 */
	@SuppressWarnings("unused")
	private static int addGroupIdInContact(Context context, String phone,
			String groupId) {
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri
				.parse("content://com.android.contacts/data/phones/filter/"
						+ phone);
		Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
				null, null, null);
		String contact_id = "-1";
		if (null != cursor && cursor.moveToNext()) {
			contact_id = cursor.getString(0);
		}
		cursor.close();
		if (!"-1".equals(groupId)) { // -1未分组的不添加组Id
			String WHERE = ContactsContract.Data.RAW_CONTACT_ID
					+ " = ? "
					+ " and "
					+ ContactsContract.Data.MIMETYPE
					+ "="
					+ "'"
					+ ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE
					+ "'";
			ContentValues values = new ContentValues();
			values.put(ContactsContract.Data.DATA1, groupId);
			return resolver.update(ContactsContract.Data.CONTENT_URI, values,
					WHERE, new String[] { contact_id });
		}
		return -2;

	}

	/**
	 * recovery groups
	 * 
	 * @param context
	 * @param entities
	 */
	@SuppressWarnings("unused")
	private static void addGroups(Context context,
			List<ContactGroupEntity> entities) {
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = null;
		Cursor cursor = null;
		for (ContactGroupEntity entity : entities) {
			values = new ContentValues();
			values.put(ContactsContract.Groups.TITLE, entity.getGroupName());
			values.put(ContactsContract.Groups.ACCOUNT_NAME,
					entity.getmAccount_Name());
			values.put(ContactsContract.Groups.ACCOUNT_TYPE,
					entity.getmAccount_Type());
			values.put(ContactsContract.Groups.DELETED, entity.isDelete());
			String RAW_CONTACTS_WHERE = ContactsContract.Groups._ID + " = ? ";
			cursor = resolver.query(ContactsContract.Groups.CONTENT_URI, null,
					RAW_CONTACTS_WHERE, new String[] { entity.getGroupId() },
					null);
			boolean has = cursor.moveToNext();
			L.d(TAG, "has" + has);
			cursor.close();
			if (has)
				resolver.update(ContactsContract.Groups.CONTENT_URI, values,
						RAW_CONTACTS_WHERE,
						new String[] { entity.getGroupId() });
			else
				resolver.insert(ContactsContract.Groups.CONTENT_URI, values);
		}
	}

	public static String getName(Context context, String phone) {
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri
				.parse("content://com.android.contacts/data/phones/filter/"
						+ phone);
		Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
				null, null, null);
		String contact_id = "-1";
		if (null != cursor && cursor.moveToNext()) {
			contact_id = cursor.getString(0);
		}
		String[] projection = { ContactsContract.Contacts.DISPLAY_NAME };
		String WHERE = ContactsContract.Contacts._ID + " = ? ";
		cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
				projection, WHERE, new String[] { contact_id }, null);
		String name = "";
		if (cursor.moveToNext()) {
			name = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		}
		return name;
	}

	/**
	 * 读取联系人id及版本
	 * 
	 * @param c
	 * @return
	 */
	public static HashMap<String, String> getRawIdVersion(Context c) {
		HashMap<String, String> map = new HashMap<String, String>();
		String[] projection = { ContactsContract.RawContacts._ID,
				ContactsContract.RawContacts.VERSION };
		Cursor cursor = c.getContentResolver().query(
				ContactsContract.RawContacts.CONTENT_URI, projection, null,
				null, null);
		while (cursor.moveToNext()) {
			map.put(cursor.getString(cursor
					.getColumnIndex(ContactsContract.RawContacts._ID)),
					cursor.getString(cursor
							.getColumnIndex(ContactsContract.RawContacts.VERSION)));
		}
		return map;
	}

	/**
	 * 判断联系人是否变化
	 * 
	 * @param oldMap
	 * @param newMap
	 * @return
	 */
	public static boolean isContactsChange(HashMap<String, String> oldMap,
			HashMap<String, String> newMap) {
		if (oldMap.size() != newMap.size()) {
			// 有新增或删除联系人
			return true;
		} else {
			for (Iterator<?> iterator = newMap.keySet().iterator(); iterator
					.hasNext();) {
				String key = (String) iterator.next();
				if (!oldMap.containsKey(key)) {
					// 有新增联系人
					return true;
				} else if (!newMap.get(key).equals(oldMap.get(key))) {
					// 有编辑联系人
					return true;
				}
			}

			for (Iterator<?> iterator = oldMap.keySet().iterator(); iterator
					.hasNext();) {
				String key = (String) iterator.next();
				if (!newMap.containsKey(key)) {
					// 有删除联系人
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * 将一个inputstream里面的数组全部读取到 一个数组中，这个能将数组数据全部读出来。
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static byte[] getBytes(InputStream is) throws Exception {
		byte[] data = null;
		Collection chunks = new ArrayList();
		byte[] buffer = new byte[1024 * 1000];
		int read = -1;
		int size = 0;

		while ((read = is.read(buffer)) != -1) {
			if (read > 0) {
				byte[] chunk = new byte[read];
				System.arraycopy(buffer, 0, chunk, 0, read);
				chunks.add(chunk);
				size += chunk.length;
			}
		}

		if (size > 0) {
			ByteArrayOutputStream bos = null;
			try {
				bos = new ByteArrayOutputStream(size);
				for (Iterator itr = chunks.iterator(); itr.hasNext();) {
					byte[] chunk = (byte[]) itr.next();
					bos.write(chunk);
				}
				data = bos.toByteArray();
			} finally {
				if (bos != null) {
					bos.close();
				}
			}
		}
		return data;
	}

}
