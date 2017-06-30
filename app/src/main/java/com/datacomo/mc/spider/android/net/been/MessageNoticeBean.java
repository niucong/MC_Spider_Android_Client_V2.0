package com.datacomo.mc.spider.android.net.been;

public class MessageNoticeBean {
	/**
	 * 通知表
	 */
	private int noticeId;

	/**
	 * 发送者编号
	 */
	private int sendMemberId;

	/**
	 * 发送者名字
	 */
	private String sendMemberName;

	// /**
	// * 发送者头像
	// */
	// private MemberHeadBean senderHead;

	/**
	 * 动作发起方对象标识号 例： 申请圈子合作中，sendObjectId为发起合作申请的圈子标识号。
	 */
	private int sendObjectId;

	/**
	 * 动作发起方对象名字
	 */
	private String sendObjectName;

	// /**
	// * 动作发起方对象图片url
	// */
	// private String sendObjectImageUrl;
	//
	// /**
	// * 动作发起方对象图片路径
	// */
	// private String sendObjectImagePath;

	/**
	 * 对象编号
	 */
	private int objectId;

	/**
	 * 对象名字
	 */
	private String objectName;

	// /**
	// * 对象头像
	// */
	// private MemberHeadBean objectHead;

	/**
	 * 对象类型 1：社员\n2：圈子',
	 */
	private String objectType;

	/**
	 * 动作类型: 1. 申请成为朋友 2. 推荐成为朋友 3. 申请成为家人 4. 申请成为情感对象 5. 申请加入圈子 6. 申请成为管理员 7.
	 * 邀请加入圈子 8. 申请成为下级圈子 9. 申请圈子合作
	 */
	private String actionType;

	// /**
	// * 所有者编号
	// */
	// private int ownerMemberId;

	/**
	 * 通知内容
	 */
	private String noticeContent;

	/**
	 * 通知时间
	 */
	private String noticeTime;

	// /**
	// * 通知类型（1.申请，2回复， 3分享）
	// */
	// private int noticeType;
	/**
	 * 是否已读
	 */
	private boolean isRead;
	/**
	 * 发送者头像url
	 */
	private String sendMemberHeadUrl;
	/**
	 * 发送者头像path
	 */
	private String sendMemberHeadPath;
	// /**
	// * 发送对象图片前缀
	// */
	// private String sendObjectUrl;
	// /**
	// * 发送对象图片路径
	// */
	// private String sendObjectPath;
	/**
	 * 对象图片前缀
	 */
	private String objectUrl;
	/**
	 * 对象图片路径
	 */
	private String objectPath;

	// private String noticeUuid;
	// /**
	// * 备注编号
	// */
	// private int remarkId;

	/**
	 * 备注内容
	 */
	private String remarkContent;

	// public int getRemarkId() {
	// return remarkId;
	// }
	//
	// public void setRemarkId(int remarkId) {
	// this.remarkId = remarkId;
	// }

	public String getRemarkContent() {
		return remarkContent;
	}

	public void setRemarkContent(String remarkContent) {
		this.remarkContent = remarkContent;
	}

	public int getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
	}

	public int getSendMemberId() {
		return sendMemberId;
	}

	public void setSendMemberId(int sendMemberId) {
		this.sendMemberId = sendMemberId;
	}

	public String getSendMemberName() {
		return sendMemberName;
	}

	public void setSendMemberName(String sendMemberName) {
		this.sendMemberName = sendMemberName;
	}

	// public MemberHeadBean getSenderHead() {
	// return senderHead;
	// }
	//
	// public void setSenderHead(MemberHeadBean senderHead) {
	// this.senderHead = senderHead;
	// }

	public int getSendObjectId() {
		return sendObjectId;
	}

	public void setSendObjectId(int sendObjectId) {
		this.sendObjectId = sendObjectId;
	}

	public String getSendObjectName() {
		return sendObjectName;
	}

	public void setSendObjectName(String sendObjectName) {
		this.sendObjectName = sendObjectName;
	}

	// public String getSendObjectImageUrl() {
	// return sendObjectImageUrl;
	// }
	//
	// public void setSendObjectImageUrl(String sendObjectImageUrl) {
	// this.sendObjectImageUrl = sendObjectImageUrl;
	// }
	//
	// public String getSendObjectImagePath() {
	// return sendObjectImagePath;
	// }
	//
	// public void setSendObjectImagePath(String sendObjectImagePath) {
	// this.sendObjectImagePath = sendObjectImagePath;
	// }

	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	// public MemberHeadBean getObjectHead() {
	// return objectHead;
	// }
	//
	// public void setObjectHead(MemberHeadBean objectHead) {
	// this.objectHead = objectHead;
	// }

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	// public int getOwnerMemberId() {
	// return ownerMemberId;
	// }
	//
	// public void setOwnerMemberId(int ownerMemberId) {
	// this.ownerMemberId = ownerMemberId;
	// }

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public String getNoticeTime() {
		return noticeTime;
	}

	public void setNoticeTime(String noticeTime) {
		this.noticeTime = noticeTime;
	}

	// public int getNoticeType() {
	// return noticeType;
	// }
	//
	// public void setNoticeType(int noticeType) {
	// this.noticeType = noticeType;
	// }

	public boolean isRead() {
		return isRead;
	}

	public void setIsRead(boolean isRead) {
		this.isRead = isRead;
	}

	public String getSendMemberHeadUrl() {
		return sendMemberHeadUrl;
	}

	public void setSendMemberHeadUrl(String sendMemberHeadUrl) {
		this.sendMemberHeadUrl = sendMemberHeadUrl;
	}

	public String getSendMemberHeadPath() {
		return sendMemberHeadPath;
	}

	public void setSendMemberHeadPath(String sendMemberHeadPath) {
		this.sendMemberHeadPath = sendMemberHeadPath;
	}

	// public String getSendObjectUrl() {
	// return sendObjectUrl;
	// }
	//
	// public void setSendObjectUrl(String sendObjectUrl) {
	// this.sendObjectUrl = sendObjectUrl;
	// }
	//
	// public String getSendObjectPath() {
	// return sendObjectPath;
	// }
	//
	// public void setSendObjectPath(String sendObjectPath) {
	// this.sendObjectPath = sendObjectPath;
	// }

	public String getObjectUrl() {
		return objectUrl;
	}

	public void setObjectUrl(String objectUrl) {
		this.objectUrl = objectUrl;
	}

	public String getObjectPath() {
		return objectPath;
	}

	public void setObjectPath(String objectPath) {
		this.objectPath = objectPath;
	}

	// public String getNoticeUuid() {
	// return noticeUuid;
	// }
	//
	// public void setNoticeUuid(String noticeUuid) {
	// this.noticeUuid = noticeUuid;
	// }

}
