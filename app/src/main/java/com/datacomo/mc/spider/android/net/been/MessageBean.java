package com.datacomo.mc.spider.android.net.been;

import java.util.List;

public class MessageBean {

	/**
	 * 消息编号
	 */
	private int messageId;

	/**
	 * 消息状态 1：发送的 2：接收的
	 */
	private int messageStatus;

	/**
	 * 消息是否未读 true： 未读 false：已读
	 */
	// private boolean unRead;

	/**
	 * 消息内容
	 */
	private String messageContent;

	/**
	 * 消息所属人社区标识号
	 */
	// private int memberId;

	/**
	 * 相应联系人的唯一标识号
	 */
	// private int leaguerId;

	/**
	 * 消息发起者社区标识号
	 */
	private int senderId;

	/**
	 * 消息发起者名字
	 */
	private String senderName;

	/**
	 * 消息发起者头像
	 */
	private MemberHeadBean senderHead;

	/**
	 * 消息接收者社区标识号
	 */
	private int receiverId;

	/**
	 * 消息接收者名字
	 */
	private String receiverName;

	/**
	 * 消息接收者头像
	 */
	private MemberHeadBean receiverHead;

	/**
	 * 来源圈子编号
	 */
	// private int groupId;

	/**
	 * 来源圈子名称
	 */
	// private String groupName;

	/**
	 * 消息创建时间
	 */
	private String createTime;

	private List<MessageResourceInfo> messageResourceInfoList;

	public List<MessageResourceInfo> getMessageResourceInfoList() {
		return messageResourceInfoList;
	}

	public void setMessageResourceInfoList(
			List<MessageResourceInfo> messageResourceInfoList) {
		this.messageResourceInfoList = messageResourceInfoList;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getMessageStatus() {
		return messageStatus;
	}

	public boolean isReceived() {
		if (2 == messageStatus) {
			return true;
		}
		return false;
	}

	public void setMessageStatus(int messageStatus) {
		this.messageStatus = messageStatus;
	}

	// public boolean isUnRead() {
	// return unRead;
	// }
	//
	// public void setUnRead(boolean unRead) {
	// this.unRead = unRead;
	// }

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	// public int getMemberId() {
	// return memberId;
	// }
	//
	// public void setMemberId(int memberId) {
	// this.memberId = memberId;
	// }

	// public int getLeaguerId() {
	// return leaguerId;
	// }
	//
	// public void setLeaguerId(int leaguerId) {
	// this.leaguerId = leaguerId;
	// }

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		if (senderName == null) {
			senderName = "";
		}
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public MemberHeadBean getSenderHead() {
		return senderHead;
	}

	public void setSenderHead(MemberHeadBean senderHead) {
		this.senderHead = senderHead;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public MemberHeadBean getReceiverHead() {
		return receiverHead;
	}

	public void setReceiverHead(MemberHeadBean receiverHead) {
		this.receiverHead = receiverHead;
	}

	// public int getGroupId() {
	// return groupId;
	// }
	//
	// public void setGroupId(int groupId) {
	// this.groupId = groupId;
	// }

	// public String getGroupName() {
	// return groupName;
	// }
	//
	// public void setGroupName(String groupName) {
	// this.groupName = groupName;
	// }

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
