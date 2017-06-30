package com.datacomo.mc.spider.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 */
public class DataBaseHelper extends SQLiteOpenHelper {
	private static int version = 12;

	// 数据库名字
	private static final String name = "user.db";
	/** 用户资料 */
	protected static final String USER_MESSAGE = "user_message";
	/** 邀请联系人列表 */
	protected static final String CONTACTS_BOOK = "contacts_book";
	/** 注册信息 */
	protected static final String REGISTER_INFO = "register_info";
	/** 圈子列表 */
	protected static final String GROUP_LIST = "group_list";
	/** 圈子动态墙 - 0：圈子、1：朋友、2：随便看看 */
	protected static final String GROUP_TREND = "group_trend";
	/** 消息- 0：通知、1：私信、2：招呼 、3：邮件、4：我的笔记、5：朋友分享的笔记 */
	protected static final String MESSAGE = "message";
	/** 搜索记录 */
	protected static final String RECORD_SEARCH = "record_search";
	/** 同步联系人头像 */
	protected static final String UPDATE_CONTACT_HEAD = "update_contact_head";

	/** 私信聊天记录 */
	protected static final String MESSAGE_CHAT = "message_chat";

	/** 朋友列表 */
	protected static final String FRIEND_LIST = "friend_list";

	/** 朋友圈-朋友 */
	protected static final String FRIEND_TO_GROUP = "friend_to_group";

	private static DataBaseHelper instance;

	private DataBaseHelper(Context context) {
		super(context, name, null, version);
	}

	public static DataBaseHelper getHelper(Context context) {
		if (instance == null)
			synchronized (DataBaseHelper.class) {
				if (instance == null)
					instance = new DataBaseHelper(context);
			}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + USER_MESSAGE
				+ "(_id integer primary key autoincrement,"
				+ " user_name varchar(15), " + " account varchar(100),"
				+ " password varchar(20)," + " session_key varchar(30),"
				+ " memberId varchar(30)," + " mark varchar(50),"
				+ " uploadcontacts_time varchar(15),"// 上传通讯录时间
				+ " backupcontacts_time long,"// 备份通讯录时间
				+ " renewcontacts_time long,"// 恢复通讯录时间
				+ " nobackupcontacts_weeks integer,"// 已经几星期没备份了
				+ " firstlogin_time long,"// 首次登录时间
				+ " firstlogin_weeks integer,"// 几星期从未备份

				+ " accountType varchar(2),"// 帐号类型 0:优优工作圈帐号、1：新浪微博帐号、2：腾讯微博帐号
				+ " openId varchar(20),"// 第三方用户id
				+ " name varchar(20),"// 第三方用户头像
				+ " sex varchar(1),"// 第三方用户性别
				+ " headUrlPath varchar(100),"// 第三方用户头像
				+ " oauth_token varchar(100),"// 第三方用户oauth_token
				+ " access_Token varchar(100),"// 第三方用户access_Token
				+ " access_Token_secret varchar(100),"// 第三方用户access_Token_secret
				+ " startUpdateTime long,"// 备份通讯录时间
				+ " startDeleteTime long)");// 恢复通讯录时间

		db.execSQL("create table "
				+ CONTACTS_BOOK
				+ "(user_id integer primary key autoincrement,"
				+ " name varchar(15), "
				+ " number varchar(20), "
				+ " RegisterStatus integer ," // 注册状态：
												// 1已注册、2未注册
				+ " FriendStatus integer, "// 朋友状态： 0、1、2
				+ " contactMemberId integer, " + " memberHead varchar(100) "
				+ ")");

		db.execSQL("create table " + REGISTER_INFO
				+ "(_id integer primary key autoincrement,"
				+ " email varchar(100)," + " password varchar(20),"
				+ " phone varchar(15))");

		db.execSQL("create table "
				+ GROUP_LIST
				+ "(_id integer primary key autoincrement,"
				+ " id varchar(10),"
				+ " name varchar(20) ,type varchar(1),"
				+ " head varchar(100), openStatus varchar(1), groupNamePy varchar(100), groupNameJp varchar(20))");

		db.execSQL("create table " + GROUP_TREND
				+ "(_id integer primary key autoincrement,"
				+ " session_key varchar(30)," + " type varchar(1),"// 0：圈子、1：朋友、2：随便看看、3：收藏的圈博、4：收藏的文件、5：收藏的照片
				+ " group_trend varchar(10000000))");

		db.execSQL("create table if not exists " + RECORD_SEARCH
				+ "(_id integer primary key autoincrement,"
				+ " session_key varchar(30)," + " record varchar(100))");

		db.execSQL("create table " + MESSAGE
				+ "(_id integer primary key autoincrement,"
				+ " session_key varchar(30)," + " type varchar(1),"// 0：通知、1：私信、2：招呼
				+ " message varchar(10000000))");

		db.execSQL("create table " + UPDATE_CONTACT_HEAD
				+ "(_id integer primary key autoincrement,"
				+ " number varchar(30)," + " memberHead varchar(100))");

		// 私信聊天记录
		db.execSQL("create table " + MESSAGE_CHAT
				+ "(_id integer primary key autoincrement,"
				+ " session_key varchar(30)," + " friendId varchar(15),"
				+ " contactLeaguerId varchar(15),"// 0：通知、1：私信、2：招呼
				+ " message varchar(10000000))");

		// 朋友列表
		db.execSQL("create table " + FRIEND_LIST
				+ "(_id integer primary key autoincrement,"
				+ " memberId integer," + " memberName varchar(100),"
				+ " memberNamePY varchar(500)," + " memberNameJP varchar(100),"
				+ " memberHeadUrl varchar(100),"
				+ " memberHeadPath varchar(500)," + " memberSex integer,"
				+ " memberMood varchar(500)," + " friendName varchar(100),"
				+ " friendNamePY varchar(500)," + " friendNameJP varchar(100))");

		// 朋友圈-朋友
		db.execSQL("create table " + FRIEND_TO_GROUP
				+ "(_id integer primary key autoincrement,"
				+ " memberId integer," + " friendGroupId integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 11 && newVersion == 12) {
			dbElevenToTwelve(db);
		} else if (oldVersion == 10 && newVersion == 12) {
			dbTenToEleven(db);
			dbElevenToTwelve(db);
		} else if (oldVersion == 9 && newVersion == 12) {
			dbNineToTen(db);
			dbTenToEleven(db);
			dbElevenToTwelve(db);
		} else if (oldVersion == 8 && newVersion == 12) {
			dbEightToNine(db);
			dbNineToTen(db);
			dbTenToEleven(db);
			dbElevenToTwelve(db);
		} else if (oldVersion == 7 && newVersion == 12) {
			dbSevenToEight(db);
			dbEightToNine(db);
			dbNineToTen(db);
			dbTenToEleven(db);
			dbElevenToTwelve(db);
		} else if (oldVersion == 6 && newVersion == 12) {
			dbSixToSeven(db);
			dbSevenToEight(db);
			dbEightToNine(db);
			dbNineToTen(db);
			dbTenToEleven(db);
			dbElevenToTwelve(db);
		} else if (oldVersion == 5 && newVersion == 12) {
			dbFiveToSix(db);
			dbSixToSeven(db);
			dbSevenToEight(db);
			dbEightToNine(db);
			dbNineToTen(db);
			dbTenToEleven(db);
			dbElevenToTwelve(db);
		} else if (oldVersion == 4 && newVersion == 12) {
			dbFourToFive(db);
			dbFiveToSix(db);
			dbSixToSeven(db);
			dbSevenToEight(db);
			dbEightToNine(db);
			dbNineToTen(db);
			dbTenToEleven(db);
			dbElevenToTwelve(db);
		} else if (oldVersion == 3 && newVersion == 12) {
			dbThreeToFour(db);
			dbFourToFive(db);
			dbFiveToSix(db);
			dbSixToSeven(db);
			dbSevenToEight(db);
			dbEightToNine(db);
			dbNineToTen(db);
			dbTenToEleven(db);
			dbElevenToTwelve(db);
		} else if (oldVersion == 2 && newVersion == 12) {
			dbTwoToThree(db);
			dbThreeToFour(db);
			dbFourToFive(db);
			dbFiveToSix(db);
			dbSixToSeven(db);
			dbSevenToEight(db);
			dbEightToNine(db);
			dbNineToTen(db);
			dbTenToEleven(db);
			dbElevenToTwelve(db);
		} else if (oldVersion == 1 && newVersion == 12) {
			dbOneToTwo(db);
			dbTwoToThree(db);
			dbThreeToFour(db);
			dbFourToFive(db);
			dbFiveToSix(db);
			dbSixToSeven(db);
			dbSevenToEight(db);
			dbEightToNine(db);
			dbNineToTen(db);
			dbTenToEleven(db);
			dbElevenToTwelve(db);
		}
	}

	/**
	 * 数据库Version从11升级到12
	 * 
	 * @param db
	 */
	private void dbElevenToTwelve(SQLiteDatabase db) {
		// 圈子名字拼音
		db.execSQL("alter table " + GROUP_LIST
				+ " add groupNamePy varchar(100)");
		// 圈子名字简拼
		db.execSQL("alter table " + GROUP_LIST + " add groupNameJp varchar(20)");

		// 私信聊天记录
		db.execSQL("create table " + MESSAGE_CHAT
				+ "(_id integer primary key autoincrement,"
				+ " session_key varchar(30)," + " friendId varchar(15),"
				+ " contactLeaguerId varchar(15),"
				+ " message varchar(10000000))");

		// 朋友列表更新时间
		db.execSQL("alter table " + USER_MESSAGE + " add startUpdateTime long");
		// 朋友列表删除时间
		db.execSQL("alter table " + USER_MESSAGE + " add startDeleteTime long");

		// 朋友列表
		db.execSQL("create table " + FRIEND_LIST
				+ "(_id integer primary key autoincrement,"
				+ " memberId integer," + " memberName varchar(100),"
				+ " memberNamePY varchar(500)," + " memberNameJP varchar(100),"
				+ " memberHeadUrl varchar(100),"
				+ " memberHeadPath varchar(500)," + " memberSex integer,"
				+ " memberMood varchar(500)," + " friendName varchar(100),"
				+ " friendNamePY varchar(500)," + " friendNameJP varchar(100))");

		// 朋友圈-朋友
		db.execSQL("create table " + FRIEND_TO_GROUP
				+ "(_id integer primary key autoincrement,"
				+ " memberId integer," + " friendGroupId integer)");
	}

	/**
	 * 数据库Version从10升级到11
	 * 
	 * @param db
	 */
	private void dbTenToEleven(SQLiteDatabase db) {
		db.execSQL("create table " + UPDATE_CONTACT_HEAD
				+ "(_id integer primary key autoincrement,"
				+ " number varchar(30)," + " memberHead varchar(100))");
	}

	/**
	 * 数据库Version从9升级到10
	 * 
	 * @param db
	 */
	private void dbNineToTen(SQLiteDatabase db) {
		db.execSQL("create table " + MESSAGE
				+ "(_id integer primary key autoincrement,"
				+ " session_key varchar(30)," + " type varchar(1),"// 0：通知、1：私信、2：招呼
				+ " message varchar(10000000))");
		db.execSQL("alter table " + GROUP_TREND + " add type varchar(1)");// 0：圈子、1：朋友、2：随便看看、3：收藏的圈博、4：收藏的文件、5：收藏的照片
	}

	/**
	 * 数据库Version从8升级到9
	 * 
	 * @param db
	 */
	private void dbEightToNine(SQLiteDatabase db) {
		// 圈子私密类型 1 - 公开 2 - 私密 3 - 自定义
		db.execSQL("alter table " + GROUP_LIST + " add openStatus varchar(1)");
	}

	/**
	 * 数据库Version从7升级到8
	 * 
	 * @param db
	 */
	private void dbSevenToEight(SQLiteDatabase db) {
		db.execSQL("alter table " + CONTACTS_BOOK + " add FriendStatus integer");
		db.execSQL("alter table " + CONTACTS_BOOK
				+ " add memberHead varchar(100)");
		db.execSQL("alter table " + CONTACTS_BOOK
				+ " add contactMemberId integer");
	}

	/**
	 * 数据库Version从6升级到7
	 * 
	 * @param db
	 */
	private void dbSixToSeven(SQLiteDatabase db) {
		db.execSQL("create table " + GROUP_TREND
				+ "(_id integer primary key autoincrement,"
				+ " session_key varchar(30),"
				+ " group_trend varchar(10000000))");
		// 用户id
		db.execSQL("alter table " + USER_MESSAGE + " add memberId varchar(20)");
	}

	/**
	 * 数据库Version从5升级到6
	 * 
	 * @param db
	 */
	private void dbFiveToSix(SQLiteDatabase db) {
		db.execSQL("create table " + GROUP_LIST
				+ "(_id integer primary key autoincrement,"
				+ " id varchar(10)," + " name varchar(20),type varchar(1),"
				+ " head varchar(100))");
	}

	/**
	 * 数据库Version从4升级到5
	 * 
	 * @param db
	 */
	private void dbFourToFive(SQLiteDatabase db) {
		// 帐号类型 0:优优工作圈帐号、1：新浪微博帐号、2：腾讯微博帐号
		db.execSQL("alter table " + USER_MESSAGE
				+ " add accountType varchar(2)");
		// 第三方用户id
		db.execSQL("alter table " + USER_MESSAGE + " add openId varchar(20)");
		// 第三方用户名字
		db.execSQL("alter table " + USER_MESSAGE + " add name varchar(20)");
		// 第三方用户性别
		db.execSQL("alter table " + USER_MESSAGE + " add sex varchar(1)");
		// 第三方用户头像url
		db.execSQL("alter table " + USER_MESSAGE
				+ " add headUrlPath varchar(100)");
		// 第三方用户oauth_token
		db.execSQL("alter table " + USER_MESSAGE
				+ " add oauth_token varchar(100)");
		// 第三方用户access_Token
		db.execSQL("alter table " + USER_MESSAGE
				+ " add access_Token varchar(100)");
		// 第三方用户access_Token_secret
		db.execSQL("alter table " + USER_MESSAGE
				+ " add access_Token_secret varchar(100)");
	}

	/**
	 * 数据库Version从3升级到4
	 * 
	 * @param db
	 */
	private void dbThreeToFour(SQLiteDatabase db) {
		// 首次登录时间
		db.execSQL("alter table " + USER_MESSAGE + " add firstlogin_time long");
		// 几星期从未备份
		db.execSQL("alter table " + USER_MESSAGE
				+ " add firstlogin_weeks integer");
	}

	/**
	 * 数据库Version从2升级到3
	 * 
	 * @param db
	 */
	private void dbTwoToThree(SQLiteDatabase db) {
		// 备份通讯录时间
		db.execSQL("alter table " + USER_MESSAGE
				+ " add backupcontacts_time long");
		// 恢复通讯录时间
		db.execSQL("alter table " + USER_MESSAGE
				+ " add renewcontacts_time long");
		// 几星期没备份
		db.execSQL("alter table " + USER_MESSAGE
				+ " add nobackupcontacts_weeks integer");
	}

	/**
	 * 数据库Version从1升级到2
	 * 
	 * @param db
	 */
	private void dbOneToTwo(SQLiteDatabase db) {
		// 添加account列
		db.execSQL("alter table " + USER_MESSAGE + " add account varchar(100)");
		// 复制数据
		db.execSQL("update " + USER_MESSAGE + " set account = user_name");
		// 添加uploadcontacts_time列
		db.execSQL("alter table " + USER_MESSAGE
				+ " add uploadcontacts_time varchar(15) default '0'");

		// 添加CONTACTS_BOOK表
		db.execSQL("create table " + CONTACTS_BOOK
				+ "(user_id integer primary key autoincrement,"
				+ " name varchar(15), " + "number varchar(20), "
				+ "RegisterStatus integer" + ")");

		// 添加REGISTER_INFO表
		db.execSQL("create table " + REGISTER_INFO
				+ "(_id integer primary key autoincrement,"
				+ " email varchar(100)," + " password varchar(20),"
				+ " phone varchar(15))");
	}
}
