package com.datacomo.mc.spider.android.net.been.groupchat;

import java.io.Serializable;

public class ObjectInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8885659513763746164L;
	/**
	 * 消息内容
	 */
	private String messageContent;
	/**
	 * 消息类型 1-正常消息，2-邀请成员加入，3-主动加入，4-退出，5-踢出成员
	 */
	private int messageType;
	/**
	 * 对象类型 文本 OBJ_TEXT, 文件 OBJ_FILE, 照片 OBJ_PHOTO, 语音 OBJ_VOICE;
	 */
	private String objectType;
	/**
	 * 对象名字
	 */
	private String objectName;
	/**
	 * 对象URL
	 */
	private String objectUrl;
	/**
	 * 对象Path
	 */
	private String objectPath;
	/**
	 * 对象大小 （以B为单位）
	 */
	private long objectSize;
	/**
	 * 对象时长（对象类型为音频或视频时使用）
	 */
	private long objectLength;

	// /**
	// * 对象后缀编号
	// */
	// private int objectFormatId;
	// /**
	// * 对象后缀名字
	// */
	// private String objectFormatName;
	// /**
	// * 对象后缀URL
	// */
	// private String objectFormatUrl;
	// /**
	// * 对象后缀PATH
	// */
	// private String objectFormatPath;

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectUrl() {
		if (objectUrl == null)
			objectUrl = "";
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

	public long getObjectSize() {
		return objectSize;
	}

	public void setObjectSize(long objectSize) {
		this.objectSize = objectSize;
	}

	public long getObjectLength() {
		return objectLength;
	}

	public void setObjectLength(long objectLength) {
		this.objectLength = objectLength;
	}

	// public int getObjectFormatId() {
	// return objectFormatId;
	// }
	//
	// public void setObjectFormatId(int objectFormatId) {
	// this.objectFormatId = objectFormatId;
	// }
	//
	// public String getObjectFormatName() {
	// return objectFormatName;
	// }
	//
	// public void setObjectFormatName(String objectFormatName) {
	// this.objectFormatName = objectFormatName;
	// }
	//
	// public String getObjectFormatUrl() {
	// return objectFormatUrl;
	// }
	//
	// public void setObjectFormatUrl(String objectFormatUrl) {
	// this.objectFormatUrl = objectFormatUrl;
	// }
	//
	// public String getObjectFormatPath() {
	// return objectFormatPath;
	// }
	//
	// public void setObjectFormatPath(String objectFormatPath) {
	// this.objectFormatPath = objectFormatPath;
	// }

}
