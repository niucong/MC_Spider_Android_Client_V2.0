package com.datacomo.mc.spider.android.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.datacomo.mc.spider.android.net.been.groupchat.GroupChatMessageBean;
import com.datacomo.mc.spider.android.net.been.groupchat.ObjectInfoBean;

public class ChatGroupMessageBeanService extends SQLiteOpenHelper {

	public static String CHAT_GROUP = "group_chat";

	private static ChatGroupMessageBeanService service = null;

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
				+ CHAT_GROUP
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

	private ChatGroupMessageBeanService(Context context) {
		super(context, QChatSendService.name, null, QChatSendService.version);
	}

	public static ChatGroupMessageBeanService getService(Context context) {
		if (service == null)
			synchronized (ChatGroupMessageBeanService.class) {
				if (service == null)
					service = new ChatGroupMessageBeanService(context);
			}
		return service;
	}

	public void save(List<GroupChatMessageBean> LIST, String messageId,
			String groupId) {
		synchronized (CHAT_GROUP) {
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = null;
			try {
				if (messageId != null && !"".equals(messageId)
						&& !"0".equals(messageId)) {
					db.execSQL("delete from " + CHAT_GROUP
							+ " where receiverId = '" + groupId + "'");
				}

				int mId = 0;
				for (GroupChatMessageBean mb : LIST) {
					mId = mb.getMessageId();
					// messageId已存在对接上
					cursor = db
							.rawQuery("select messageId from " + CHAT_GROUP
									+ " where messageId = ?",
									new String[] { mId + "" });
					if (cursor.getCount() > 0) {
						mId = 0;
						break;
					}

					int resourceId = 0;
					String rMessageContent = "", objectType = "", objectName = "", objectUrl = "", objectPath = "", objectBak2 = "", objectBak3 = "";
					long objectSize = 0, objectBak1 = 0;

					List<ObjectInfoBean> messageList = mb.getMessageList();
					if (messageList != null && messageList.size() > 0) {
						ObjectInfoBean mri = messageList.get(0);
						resourceId = mri.getMessageType();
						rMessageContent = mri.getMessageContent();
						objectType = mri.getObjectType();
						objectName = mri.getObjectName();
						objectUrl = mri.getObjectUrl();
						objectPath = mri.getObjectPath();
						objectSize = mri.getObjectSize();
						objectBak1 = mri.getObjectLength();
						objectBak2 = "";
						objectBak3 = "";
					}

					db.execSQL(
							"insert into "
									+ CHAT_GROUP
									+ "(messageId, messageStatus, messageContent, senderId, senderName, "
									+ "senderHeadUrl, senderHeadPath, receiverId, receiverName, receiverHeadUrl, "
									+ "receiverHeadPath, createTime, resourceId, rMessageContent, objectType, "
									+ "objectName, objectUrl, objectPath, objectSize, objectBak1, "
									+ "objectBak2, objectBak3, isBreak) "
									+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
							new Object[] { mId, 0, "", mb.getSendMemberId(),
									mb.getSendMemberName(),
									mb.getSendMemberUrl(),
									mb.getSendMemberPath(), groupId, "", "",
									"", mb.getCreateTime(), resourceId,
									rMessageContent, objectType, objectName,
									objectUrl, objectPath, objectSize,
									objectBak1, objectBak2, objectBak3, 0 });
				}

				if (messageId != null && !"".equals(messageId)
						&& !"0".equals(messageId)) {
					db.execSQL("update " + CHAT_GROUP
							+ " set isBreak = ? where messageId = ?",
							new Object[] { 0, messageId });
				}
				if (mId != 0)
					db.execSQL("update " + CHAT_GROUP
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
	public void saveSend(GroupChatMessageBean mb, String groupId) {
		synchronized (CHAT_GROUP) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				int resourceId = 0;
				String rMessageContent = "", objectType = "", objectName = "", objectUrl = "", objectPath = "", objectBak2 = "", objectBak3 = "";
				long objectSize = 0, objectBak1 = 0;

				List<ObjectInfoBean> messageList = mb.getMessageList();
				if (messageList != null && messageList.size() > 0) {
					ObjectInfoBean mri = messageList.get(0);
					resourceId = mri.getMessageType();
					rMessageContent = mri.getMessageContent();
					objectType = mri.getObjectType();
					objectName = mri.getObjectName();
					objectUrl = mri.getObjectUrl();
					objectPath = mri.getObjectPath();
					objectSize = mri.getObjectSize();
					objectBak1 = mri.getObjectLength();
					objectBak2 = "";
					objectBak3 = "";
				}

				db.execSQL(
						"insert into "
								+ CHAT_GROUP
								+ "(messageId, messageStatus, messageContent, senderId, senderName, "
								+ "senderHeadUrl, senderHeadPath, receiverId, receiverName, receiverHeadUrl, "
								+ "receiverHeadPath, createTime, resourceId, rMessageContent, objectType, "
								+ "objectName, objectUrl, objectPath, objectSize, objectBak1, "
								+ "objectBak2, objectBak3, isBreak) "
								+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						new Object[] { mb.getMessageId(), 0, "",
								mb.getSendMemberId(), mb.getSendMemberName(),
								mb.getSendMemberUrl(), mb.getSendMemberPath(),
								groupId, "", "", "", mb.getCreateTime(),
								resourceId, rMessageContent, objectType,
								objectName, objectUrl, objectPath, objectSize,
								objectBak1, objectBak2, objectBak3, 0 });
			} finally {
				if (db != null)
					db.close();
			}
		}
	}

	public ArrayList<GroupChatMessageBean> queryChat(int messageId,
			String groupId) {
		synchronized (CHAT_GROUP) {
			ArrayList<GroupChatMessageBean> objectList = null;
			Cursor cursor = null;
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				if (messageId == 0) {
					cursor = db
							.rawQuery(
									"select * from  "
											+ CHAT_GROUP
											+ " where receiverId = ? order by messageId desc limit 20",
									new String[] { groupId });
				} else {
					cursor = db
							.rawQuery(
									"select * from  "
											+ CHAT_GROUP
											+ " where messageId < ? and receiverId = ? order by messageId desc limit 20",
									new String[] { messageId + "", groupId });
				}
				if (cursor != null && cursor.moveToFirst()) {
					objectList = new ArrayList<GroupChatMessageBean>();
					do {
						GroupChatMessageBean mb = new GroupChatMessageBean();
						// messageId, messageStatus, messageContent, senderId,
						mb.setMessageId(cursor.getInt(1));
						mb.setSendMemberId(cursor.getInt(4));
						// senderName, senderHeadUrl, senderHeadPath,
						// receiverId,
						mb.setSendMemberName(cursor.getString(5));
						mb.setSendMemberUrl(cursor.getString(6));
						mb.setSendMemberPath(cursor.getString(7));
						// receiverName, receiverHeadUrl, receiverHeadPath,
						// createTime,
						mb.setCreateTime(cursor.getString(12));

						// resourceId, rMessageContent, objectType, objectName,
						List<ObjectInfoBean> messageResourceInfoList = new ArrayList<ObjectInfoBean>();
						ObjectInfoBean mri = new ObjectInfoBean();
						mri.setMessageType(cursor.getInt(13));
						mri.setMessageContent(cursor.getString(14));
						mri.setObjectType(cursor.getString(15));
						mri.setObjectName(cursor.getString(16));
						// objectUrl, objectPath, objectSize, objectBak1,
						// objectBak2, objectBak3
						mri.setObjectUrl(cursor.getString(17));
						mri.setObjectPath(cursor.getString(18));
						mri.setObjectSize(cursor.getInt(19));
						mri.setObjectLength(cursor.getLong(20));
						messageResourceInfoList.add(mri);
						mb.setMessageList(messageResourceInfoList);
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
	public void deleteChat(int messageId, String groupId) {
		synchronized (CHAT_GROUP) {
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor0 = null;
			Cursor cursor = null;
			try {
				cursor0 = db.rawQuery("select isBreak from " + CHAT_GROUP
						+ " where messageId = ?",
						new String[] { messageId + "" });
				if (cursor0 != null) {
					if (cursor0.moveToFirst() || cursor0.getInt(0) == 1) {
						cursor = db
								.rawQuery(
										"select messageId from  "
												+ CHAT_GROUP
												+ " where messageId > ? and receiverId = ? order by messageId limit 1",
										new String[] { messageId + "",
												groupId + "" });
						if (cursor != null && cursor.moveToFirst()) {
							db.execSQL("update " + CHAT_GROUP
									+ " set isBreak = ? where messageId = ?",
									new Object[] { 1, cursor.getInt(0) });
						}
					}
				}

				db.execSQL("delete from " + CHAT_GROUP + " where messageId = '"
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
		synchronized (CHAT_GROUP) {
			SQLiteDatabase db = this.getWritableDatabase();
			try {
				db.execSQL("delete from  " + CHAT_GROUP);
			} finally {
				if (db != null)
					db.close();
			}
		}
	}
}
