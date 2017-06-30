package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class MemberStatusBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 社员编号
	 */
	private int memberId;

	/**
	 * 社员状态 1: 正常使用 2：被封杀
	 */
	private int memberStatus;

	/**
	 * 登录次数
	 */
	private int loginNum;

	/**
	 * 最后一次登录地址
	 */
	private String lastLoginAddress;

	/**
	 * 登录地点的隐私设置
	 */
	// private PrivacyLevel viewLoginAddressSetting;
	private String viewLoginAddressSetting;

	/**
	 * 最后一次登录时间
	 */
	private String lastLoginTime;

	/**
	 * 朋友数量
	 */
	private int friendNum;

	/**
	 * 访客数
	 */
	private int visitMemberNum;

	/**
	 * 访问总数量
	 */
	private int visitTotalNum;

	/**
	 * 家庭成员数量
	 */
	private int familyNum;

	/**
	 * 在线状态 0： 不在线 1： 在线
	 */
	private int onlineStatus;

	/**
	 * 朋友动态数量
	 */
	private int friendTrendNum;

	/**
	 * 个人动态数量
	 */
	private int memberTrendNum;

	/**
	 * 朋友状态 0: 非朋友；1：申请; 2：朋友
	 */
	private int friendStatus;

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(int memberStatus) {
		this.memberStatus = memberStatus;
	}

	public int getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(int loginNum) {
		this.loginNum = loginNum;
	}

	public String getLastLoginAddress() {
		return lastLoginAddress;
	}

	public void setLastLoginAddress(String lastLoginAddress) {
		this.lastLoginAddress = lastLoginAddress;
	}

	public String getViewLoginAddressSetting() {
		return viewLoginAddressSetting;
	}

	public void setViewLoginAddressSetting(String viewLoginAddressSetting) {
		this.viewLoginAddressSetting = viewLoginAddressSetting;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public int getFriendNum() {
		return friendNum;
	}

	public void setFriendNum(int friendNum) {
		this.friendNum = friendNum;
	}

	public int getVisitMemberNum() {
		return visitMemberNum;
	}

	public void setVisitMemberNum(int visitMemberNum) {
		this.visitMemberNum = visitMemberNum;
	}

	public int getVisitTotalNum() {
		return visitTotalNum;
	}

	public void setVisitTotalNum(int visitTotalNum) {
		this.visitTotalNum = visitTotalNum;
	}

	public int getFamilyNum() {
		return familyNum;
	}

	public void setFamilyNum(int familyNum) {
		this.familyNum = familyNum;
	}

	public int getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(int onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public int getFriendTrendNum() {
		return friendTrendNum;
	}

	public void setFriendTrendNum(int friendTrendNum) {
		this.friendTrendNum = friendTrendNum;
	}

	public int getMemberTrendNum() {
		return memberTrendNum;
	}

	public void setMemberTrendNum(int memberTrendNum) {
		this.memberTrendNum = memberTrendNum;
	}

	public int getFriendStatus() {
		return friendStatus;
	}

	public void setFriendStatus(int friendStatus) {
		this.friendStatus = friendStatus;
	}

}
