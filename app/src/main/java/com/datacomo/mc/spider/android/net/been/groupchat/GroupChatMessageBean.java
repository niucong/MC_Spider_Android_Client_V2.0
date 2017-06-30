package com.datacomo.mc.spider.android.net.been.groupchat;

import java.io.Serializable;
import java.util.List;

public class GroupChatMessageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1349371635945960940L;

	/*
	 * 圈聊ID
	 */
	// private int chatId;

	/*
	 * 圈聊消息ID
	 */
	private int messageId;

	/*
	 * 发送消息的社员ID
	 */
	private int sendMemberId;

	/*
	 * 发送消息的社员名字
	 */
	private String sendMemberName;

	/*
	 * 发送消息的社员头像路径前缀
	 */
	private String sendMemberUrl;

	/*
	 * 发送消息的社员头像路径
	 */
	private String sendMemberPath;

	/*
	 * 圈聊消息
	 */
	private List<ObjectInfoBean> messageList;

	/*
	 * 创建时间
	 */
	private String createTime;

	// public int getChatId() {
	// return chatId;
	// }
	//
	// public void setChatId(int chatId) {
	// this.chatId = chatId;
	// }

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
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

	public String getSendMemberUrl() {
		return sendMemberUrl;
	}

	public void setSendMemberUrl(String sendMemberUrl) {
		this.sendMemberUrl = sendMemberUrl;
	}

	public String getSendMemberPath() {
		return sendMemberPath;
	}

	public void setSendMemberPath(String sendMemberPath) {
		this.sendMemberPath = sendMemberPath;
	}

	public List<ObjectInfoBean> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<ObjectInfoBean> messageList) {
		this.messageList = messageList;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getShowText() {
		if (null == messageList || messageList.size() == 0) {
			return null;
		}
		ObjectInfoBean b = messageList.get(0);
		switch (b.getMessageType()) {
		case 1:
			return b.getMessageContent();
		case 2:
			return sendMemberName + "邀请了成员" + b.getMessageContent() + "加入圈聊";
		case 3:
			return sendMemberName + "加入了圈聊";
		case 4:
			return sendMemberName + "退出了圈聊，当前不方便接受信息";
		case 5:
			return sendMemberName + "被管理员请出了圈聊";
		}
		return null;
	}
}
