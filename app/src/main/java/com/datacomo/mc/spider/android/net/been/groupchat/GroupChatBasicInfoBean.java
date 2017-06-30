package com.datacomo.mc.spider.android.net.been.groupchat;

public class GroupChatBasicInfoBean {

	/**
	 * 群聊ID
	 */
	private int chatId;

	/**
	 * 群聊标题
	 */
	private String chatTitle;

	/**
	 * 群聊类型：1.群聊 2.圈聊
	 */
	private int chatType;

	/**
	 * 圈子编号
	 */
	private int groupId;

	/**
	 * 群聊头像路径前缀
	 */
	private String chatHeadUrl;

	/**
	 * 群聊头像路径
	 */
	private String chatHeadPath;

	/**
	 * 群聊成员数量
	 */
	private int leaguerNumber;

	/**
	 * 圈子类型：1-兴趣圈 2-企业圈 3-开放主页 4-外部社区
	 */
	private int groupType;

	/**
	 * 圈子属性:1-没有认证圈子 2-校园认证圈子 3-企业认证圈子 4-其它认证圈子
	 */
	private int groupProperty;

	/**
	 * 创建时间
	 */
	private String createTime;

	public int getChatId() {
		return chatId;
	}

	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	public String getChatTitle() {
		return chatTitle;
	}

	public void setChatTitle(String chatTitle) {
		this.chatTitle = chatTitle;
	}

	public int getChatType() {
		return chatType;
	}

	public void setChatType(int chatType) {
		this.chatType = chatType;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getChatHeadUrl() {
		return chatHeadUrl;
	}

	public void setChatHeadUrl(String chatHeadUrl) {
		this.chatHeadUrl = chatHeadUrl;
	}

	public String getChatHeadPath() {
		return chatHeadPath;
	}

	public void setChatHeadPath(String chatHeadPath) {
		this.chatHeadPath = chatHeadPath;
	}

	public int getLeaguerNumber() {
		return leaguerNumber;
	}

	public void setLeaguerNumber(int leaguerNumber) {
		this.leaguerNumber = leaguerNumber;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

	public int getGroupProperty() {
		return groupProperty;
	}

	public void setGroupProperty(int groupProperty) {
		this.groupProperty = groupProperty;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
