package com.datacomo.mc.spider.android.net.been;

public class MembercacheGPInfo {

	/**
	 * 圈子id
	 */
	private int groupID;
	/**
	 * 圈子名称
	 */
	private String groupName;
	/**
	 * 
	 */
	private String groupProperty;
	/**
	 * 圈主id
	 */
	private int memberID;
	/**
	 * 圈主名字
	 */
	private String memberName;
	/**
	 * 圈主头像前缀
	 */
	private String memberHeadUrl;
	/**
	 * 圈主头像路径
	 */
	private String memberHeadPath;
	/**
	 * 圈子开发信息
	 */
	private int openStatus;
	/**
	 * 圈子海报前缀
	 */
	private String groupPosterUrl;
	/**
	 * 圈子海报路径
	 */
	private String groupPosterPath;

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupProperty() {
		return groupProperty;
	}

	public void setGroupProperty(String groupProperty) {
		this.groupProperty = groupProperty;
	}

	public int getMemberID() {
		return memberID;
	}

	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberHeadUrl() {
		return memberHeadUrl;
	}

	public void setMemberHeadUrl(String memberHeadUrl) {
		this.memberHeadUrl = memberHeadUrl;
	}

	public String getMemberHeadPath() {
		return memberHeadPath;
	}

	public void setMemberHeadPath(String memberHeadPath) {
		this.memberHeadPath = memberHeadPath;
	}

	public int getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(int openStatus) {
		this.openStatus = openStatus;
	}

	public String getGroupPosterUrl() {
		return groupPosterUrl;
	}

	public void setGroupPosterUrl(String groupPosterUrl) {
		this.groupPosterUrl = groupPosterUrl;
	}

	public String getGroupPosterPath() {
		return groupPosterPath;
	}

	public void setGroupPosterPath(String groupPosterPath) {
		this.groupPosterPath = groupPosterPath;
	}
	public String getWholeUrl(){
		return getGroupPosterUrl() + getGroupPosterPath();
	}
}
