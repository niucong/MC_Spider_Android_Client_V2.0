package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class MessageResourceInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7579311834761629713L;
	// 资源编号
	private int resourceId;
	// 消息编号
	// private int messageId;
	// 消息内容
	private String messageContent;
	// 对象类型：OBJ_TEXT-文本,OBJ_FILE-文件,OBJ_PHOTO-照片,OBJ_VOICE-语音;
	private String objectType;
	// 对象名字
	private String objectName;
	// 对象url
	private String objectUrl;
	// 对象路径
	private String objectPath;
	// 文件大小：以B为单位
	private int objectSize;
	// 对象备注1，如果为音频或者视频此字段存放时长
	private long objectBak1;
	// 对象备注2，视频时为存放视频的连接地址前缀
	private String objectBak2;
	// 对象备注3，视频时为存放视频的连接地址前缀
	private String objectBak3;

	// // 基础表中文件格式编号
	// private int formatId;
	// // 圈子文件类型名字,例如：txt、doc等等
	// private String formatName;
	// // 文件格式路径前缀
	// private String formatUrl;
	// // 文件格式路径
	// private String formatPath;
	// // 创建时间
	// private String createTime;

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	// public int getMessageId() {
	// return messageId;
	// }
	//
	// public void setMessageId(int messageId) {
	// this.messageId = messageId;
	// }

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
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

	public int getObjectSize() {
		return objectSize;
	}

	public void setObjectSize(int objectSize) {
		this.objectSize = objectSize;
	}

	public long getObjectBak1() {
		return objectBak1;
	}

	public void setObjectBak1(long objectBak1) {
		this.objectBak1 = objectBak1;
	}

	public String getObjectBak2() {
		return objectBak2;
	}

	public void setObjectBak2(String objectBak2) {
		this.objectBak2 = objectBak2;
	}

	public String getObjectBak3() {
		return objectBak3;
	}

	public void setObjectBak3(String objectBak3) {
		this.objectBak3 = objectBak3;
	}

	// public int getFormatId() {
	// return formatId;
	// }
	//
	// public void setFormatId(int formatId) {
	// this.formatId = formatId;
	// }

	// public String getFormatName() {
	// return formatName;
	// }
	//
	// public void setFormatName(String formatName) {
	// this.formatName = formatName;
	// }

	// public String getFormatUrl() {
	// return formatUrl;
	// }
	//
	// public void setFormatUrl(String formatUrl) {
	// this.formatUrl = formatUrl;
	// }
	//
	// public String getFormatPath() {
	// return formatPath;
	// }
	//
	// public void setFormatPath(String formatPath) {
	// this.formatPath = formatPath;
	// }
	//
	// public String getCreateTime() {
	// return createTime;
	// }
	//
	// public void setCreateTime(String createTime) {
	// this.createTime = createTime;
	// }
}
