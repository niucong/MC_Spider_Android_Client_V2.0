package com.datacomo.mc.spider.android.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.datacomo.mc.spider.android.net.been.MemberHeadBean;
import com.datacomo.mc.spider.android.net.been.MessageBean;
import com.datacomo.mc.spider.android.net.been.MessageResourceInfo;
import com.datacomo.mc.spider.android.url.L;

public class ChatMessageBeanService extends SQLiteOpenHelper {

	private final String TAG = "ChatMessageBeanService";
	public static String CHAT_MSG = "member_chat";

	private static ChatMessageBeanService service = null;

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "
				+ QChatSendService.CHAT_LIST
				+ "(_id integer primary key autoincrement,"
				+ " time Long, session_key varchar(30), id integer, name varchar(20), head varchar(100), cType integer, mType varchar(20), content varchar(10000), tLong Long)");
		oneTwoToThree(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if ((oldVersion == 2 || oldVersion == 1) && newVersion == 3) {
			oneTwoToThree(db);
		}
	}

	private void oneTwoToThree(SQLiteDatabase db) {
		db.execSQL("create table "
				+ ChatGroupMessageBeanService.CHAT_GROUP
				+ "(_id integer primary key autoincrement,"
				+ " messageId integer, messageStatus integer, messageContent varchar(10000),"
				+ " senderId integer, senderName integer, senderHeadUrl varchar(100), senderHeadPath varchar(100),"
				+ " receiverId integer, receiverName integer, receiverHeadUrl varchar(100), receiverHeadPath varchar(100), "
				+ " createTime varchar(200),"
				+ " resourceId integer, rMessageContent varchar(10000), objectType varchar(100), objectName varchar(100),"
				+ " objectUrl varchar(100), objectPath varchar(100), objectSize integer, objectBak1 long, "
				+ " objectBak2 varchar(100), objectBak3 varchar(100), isBreak integer)");

		db.execSQL("create table "
				+ ChatMessageBeanService.CHAT_MSG
				+ "(_id integer primary key autoincrement,"
				+ " messageId integer, messageStatus integer, messageContent varchar(10000),"
				+ " senderId integer, senderName integer, senderHeadUrl varchar(100), senderHeadPath varchar(100),"
				+ " receiverId integer, receiverName integer, receiverHeadUrl varchar(100), receiverHeadPath varchar(100), "
				+ " createTime varchar(200),"
				+ " resourceId integer, rMessageContent varchar(10000), objectType varchar(100), objectName varchar(100),"
				+ " objectUrl varchar(100), objectPath varchar(100), objectSize integer, objectBak1 long, "
				+ " objectBak2 varchar(100), objectBak3 varchar(100), isBreak integer)");
	}

	private ChatMessageBeanService(Context context) {
		super(context, QChatSendService.name, null, QChatSendService.version);
	}

	public static ChatMessageBeanService getService(Context context) {
		if (service == null)
			synchronized (ChatMessageBeanService.class) {
				if (service == null)
					service = new ChatMessageBeanService(context);
			}
		return service;
	}

	public void save(ArrayList<Object> objectList, String messageId,
			boolean unRead) {
		synchronized (CHAT_MSG) {
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = null;
			try {
				int mId = 0;
				for (Object object : objectList) {
					MessageBean mb = (MessageBean) object;
					mId = mb.getMessageId();
					// messageId已存在对接上
					cursor = db
							.rawQuery("select messageId from " + CHAT_MSG
									+ " where messageId = ?",
									new String[] { mId + "" });
					if (cursor.getCount() > 0) {
						mId = 0;
						break;
					}

					int resourceId = 0;
					String rMessageContent = "", objectType = "", objectName = "", objectUrl = "", objectPath = "", objectBak2 = "", objectBak3 = "";
					long objectSize = 0, objectBak1 = 0;

					List<MessageResourceInfo> messageResourceInfoList = mb
							.getMessageResourceInfoList();
					if (messageResourceInfoList != null
							&& messageResourceInfoList.size() > 0) {
						MessageResourceInfo mri = messageResourceInfoList
								.get(0);
						resourceId = mri.getResourceId();
						rMessageContent = mri.getMessageContent();
						objectType = mri.getObjectType();
						objectName = mri.getObjectName();
						objectUrl = mri.getObjectUrl();
						objectPath = mri.getObjectPath();
						objectSize = mri.getObjectSize();
						objectBak1 = mri.getObjectBak1();
						objectBak2 = mri.getObjectBak2();
						objectBak3 = mri.getObjectBak3();
					}

					db.execSQL(
							"insert into "
									+ CHAT_MSG
									+ "(messageId, messageStatus, messageContent, senderId, senderName, "
									+ "senderHeadUrl, senderHeadPath, receiverId, receiverName, receiverHeadUrl, "
									+ "receiverHeadPath, createTime, resourceId, rMessageContent, objectType, "
									+ "objectName, objectUrl, objectPath, objectSize, objectBak1, "
									+ "objectBak2, objectBak3, isBreak) "
									+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
							new Object[] { mId, mb.getMessageStatus(),
									mb.getMessageContent(), mb.getSenderId(),
									mb.getSenderName(),
									mb.getSenderHead().getHeadUrl(),
									mb.getSenderHead().getHeadPath(),
									mb.getReceiverId(), mb.getReceiverName(),
									mb.getReceiverHead().getHeadUrl(),
									mb.getReceiverHead().getHeadPath(),
									mb.getCreateTime(), resourceId,
									rMessageContent, objectType, objectName,
									objectUrl, objectPath, objectSize,
									objectBak1, objectBak2, objectBak3, 0 });
				}

				if (messageId != null && !"".equals(messageId)
						&& !"0".equals(messageId)) {
					db.execSQL("update " + CHAT_MSG
							+ " set isBreak = ? where messageId = ?",
							new Object[] { 0, messageId });
				}
				if (mId != 0 && !unRead)
					db.execSQL("update " + CHAT_MSG
							+ " set isBreak = ? where messageId = ?",
							new Object[] { 1, mId });
			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
			}
		}
	}

	/**
	 * 保存自己发送的私信
	 * 
	 * @param mb
	 */
	public void saveSend(MessageBean mb) {
		synchronized (CHAT_MSG) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				int resourceId = 0;
				String rMessageContent = "", objectType = "", objectName = "", objectUrl = "", objectPath = "", objectBak2 = "", objectBak3 = "";
				long objectSize = 0, objectBak1 = 0;

				List<MessageResourceInfo> messageResourceInfoList = mb
						.getMessageResourceInfoList();
				if (messageResourceInfoList != null
						&& messageResourceInfoList.size() > 0) {
					MessageResourceInfo mri = messageResourceInfoList.get(0);
					resourceId = mri.getResourceId();
					rMessageContent = mri.getMessageContent();
					objectType = mri.getObjectType();
					objectName = mri.getObjectName();
					objectUrl = mri.getObjectUrl();
					objectPath = mri.getObjectPath();
					objectSize = mri.getObjectSize();
					objectBak1 = mri.getObjectBak1();
					objectBak2 = mri.getObjectBak2();
					objectBak3 = mri.getObjectBak3();
				}

				db.execSQL(
						"insert into "
								+ CHAT_MSG
								+ "(messageId, messageStatus, messageContent, senderId, senderName, "
								+ "senderHeadUrl, senderHeadPath, receiverId, receiverName, receiverHeadUrl, "
								+ "receiverHeadPath, createTime, resourceId, rMessageContent, objectType, "
								+ "objectName, objectUrl, objectPath, objectSize, objectBak1, "
								+ "objectBak2, objectBak3, isBreak) "
								+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						new Object[] { mb.getMessageId(),
								mb.getMessageStatus(), mb.getMessageContent(),
								mb.getSenderId(), mb.getSenderName(),
								mb.getSenderHead().getHeadUrl(),
								mb.getSenderHead().getHeadPath(),
								mb.getReceiverId(), mb.getReceiverName(),
								mb.getReceiverHead().getHeadUrl(),
								mb.getReceiverHead().getHeadPath(),
								mb.getCreateTime(), resourceId,
								rMessageContent, objectType, objectName,
								objectUrl, objectPath, objectSize, objectBak1,
								objectBak2, objectBak3, 0 });
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	public ArrayList<Object> queryChat(String messageId, String friendId) {
		synchronized (CHAT_MSG) {
			ArrayList<Object> objectList = null;
			Cursor cursor = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				if (messageId == null || "".equals(messageId)
						|| "0".equals(messageId)) {
					cursor = db
							.rawQuery(
									"select * from  "
											+ CHAT_MSG
											+ " where senderId = ? or receiverId = ? order by messageId desc limit 20",
									new String[] { friendId + "", friendId + "" });
				} else {
					cursor = db
							.rawQuery(
									"select * from  "
											+ CHAT_MSG
											+ " where messageId < ? and(senderId = ? or receiverId = ?) order by messageId desc limit 20",
									new String[] { messageId + "",
											friendId + "", friendId + "" });
				}
				if (cursor != null && cursor.moveToFirst()) {
					objectList = new ArrayList<Object>();
					do {
						MessageBean mb = new MessageBean();
						// messageId, messageStatus, messageContent, senderId,
						mb.setMessageId(cursor.getInt(1));
						L.d(TAG, "queryChat MessageId=" + cursor.getInt(1));
						mb.setMessageStatus(cursor.getInt(2));
						mb.setMessageContent(cursor.getString(3));
						mb.setSenderId(cursor.getInt(4));
						// senderName, senderHeadUrl, senderHeadPath,
						// receiverId,
						mb.setSenderName(cursor.getString(5));
						MemberHeadBean senderHead = new MemberHeadBean();
						senderHead.setHeadUrl(cursor.getString(6));
						senderHead.setHeadPath(cursor.getString(7));
						mb.setSenderHead(senderHead);
						mb.setReceiverId(cursor.getInt(8));
						// receiverName, receiverHeadUrl, receiverHeadPath,
						// createTime,
						mb.setReceiverName(cursor.getString(9));
						MemberHeadBean receiverHead = new MemberHeadBean();
						receiverHead.setHeadUrl(cursor.getString(10));
						receiverHead.setHeadPath(cursor.getString(11));
						mb.setReceiverHead(receiverHead);
						mb.setCreateTime(cursor.getString(12));

						// resourceId, rMessageContent, objectType, objectName,
						List<MessageResourceInfo> messageResourceInfoList = new ArrayList<MessageResourceInfo>();
						MessageResourceInfo mri = new MessageResourceInfo();
						mri.setResourceId(cursor.getInt(13));
						mri.setMessageContent(cursor.getString(14));
						mri.setObjectType(cursor.getString(15));
						mri.setObjectName(cursor.getString(16));
						// objectUrl, objectPath, objectSize, objectBak1,
						// objectBak2, objectBak3
						mri.setObjectUrl(cursor.getString(17));
						mri.setObjectPath(cursor.getString(18));
						mri.setObjectSize(cursor.getInt(19));
						mri.setObjectBak1(cursor.getLong(20));
						mri.setObjectBak2(cursor.getString(21));
						mri.setObjectBak3(cursor.getString(22));
						messageResourceInfoList.add(mri);
						mb.setMessageResourceInfoList(messageResourceInfoList);
						objectList.add(mb);

						if (cursor.getInt(23) == 1)
							break;
					} while (cursor.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
			}
			return objectList;
		}
	}

	/**
	 * 删除列表
	 */
	public void deleteChat(String messageId, String friendId) {
		L.i(TAG, "deleteChat messageId=" + messageId);
		synchronized (CHAT_MSG) {
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor0 = null;
			Cursor cursor = null;
			try {
				cursor0 = db.rawQuery("select isBreak from " + CHAT_MSG
						+ " where messageId = ?",
						new String[] { messageId + "" });
				if (cursor0 != null) {
					if (cursor0.moveToFirst() || cursor0.getInt(0) == 1) {
						cursor = db
								.rawQuery(
										"select messageId from  "
												+ CHAT_MSG
												+ " where messageId > ? and (senderId = ? or receiverId = ?) order by messageId limit 1",
										new String[] { messageId + "",
												friendId + "", friendId + "" });
						if (cursor != null && cursor.moveToFirst()) {
							L.d(TAG, "deleteChat messageId=" + cursor.getInt(0));
							db.execSQL("update " + CHAT_MSG
									+ " set isBreak = ? where messageId = ?",
									new Object[] { 1, cursor.getInt(0) });
						}
					}
				}

				db.execSQL("delete from " + CHAT_MSG + " where messageId = '"
						+ messageId + "'");
			} finally {
				if (cursor0 != null)
					cursor0.close();
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
			}
		}
	}

	/**
	 * 删除所有
	 */
	public void deleteAll() {
		synchronized (CHAT_MSG) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from  " + CHAT_MSG);
			} finally {
				if (db != null)
					db.close();
			}
		}
	}
}
