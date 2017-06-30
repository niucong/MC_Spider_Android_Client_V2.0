package com.datacomo.mc.spider.android.net.been;

public class MessageContacterBean {

	/**
	 * 社员标识号
	 */
	private int memberId;

	/**
	 * 联系人主键标识 查找与指定联系人之间的消息时必须使用本字段
	 */
	private int contacterLeaguerId;

	/**
	 * 联系人社区标识号
	 */
	private int contacterId;

	/**
	 * 联系人头像
	 */
	private MemberHeadBean contacterHead;

	/**
	 * 联系人名字 当联系人是已注册用户时，本字段存放该联系人的名字； 当联系人是未注册的手机号或邮箱时，本字段存放该手机号或邮箱。
	 * 应结合contacterType指定的联系人类型使用本字段
	 */
	private String contacterName;

	/**
	 * 联系人类型 0：圈聊 1：社员编号 2：手机号码 3：邮箱地址
	 */
	private int contacterType;

	/**
	 * 最后一条消息的编号
	 */
	private int lastMessageId;

	/**
	 * 最后一条消息的内容
	 */
	private String lastMessageContent;

	/**
	 * 最后一条消息的时间
	 */
	private String lastMessageTime;

	/**
	 * 双方往来的消息总数量
	 */
	private int allMessageNum;

	/**
	 * 未读的消息数量
	 */
	private int newMessageNum;

	/**
	 * 该联系人是否处于未读状态 true： 未读状态 false: 非未读状态
	 */
	private boolean unRead;

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getContacterLeaguerId() {
		return contacterLeaguerId;
	}

	public void setContacterLeaguerId(int contacterLeaguerId) {
		this.contacterLeaguerId = contacterLeaguerId;
	}

	public int getContacterId() {
		return contacterId;
	}

	public void setContacterId(int contacterId) {
		this.contacterId = contacterId;
	}

	public MemberHeadBean getContacterHead() {
		return contacterHead;
	}

	public void setContacterHead(MemberHeadBean contacterHead) {
		this.contacterHead = contacterHead;
	}

	public String getContacterName() {
		return contacterName;
	}

	public void setContacterName(String contacterName) {
		this.contacterName = contacterName;
	}

	public int getContacterType() {
		return contacterType;
	}

	public void setContacterType(int contacterType) {
		this.contacterType = contacterType;
	}

	public int getLastMessageId() {
		return lastMessageId;
	}

	public void setLastMessageId(int lastMessageId) {
		this.lastMessageId = lastMessageId;
	}

	public String getLastMessageContent() {
		return lastMessageContent;
	}

	public void setLastMessageContent(String lastMessageContent) {
		this.lastMessageContent = lastMessageContent;
	}

	public String getLastMessageTime() {
		return lastMessageTime;
	}

	public void setLastMessageTime(String lastMessageTime) {
		this.lastMessageTime = lastMessageTime;
	}

	public int getAllMessageNum() {
		return allMessageNum;
	}

	public void setAllMessageNum(int allMessageNum) {
		this.allMessageNum = allMessageNum;
	}

	public int getNewMessageNum() {
		return newMessageNum;
	}

	public void setNewMessageNum(int newMessageNum) {
		this.newMessageNum = newMessageNum;
	}

	public boolean isUnRead() {
		return unRead;
	}

	public void setUnRead(boolean unRead) {
		this.unRead = unRead;
	}

}
