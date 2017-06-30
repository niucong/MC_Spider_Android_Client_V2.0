package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

/**
 * 社员类
 */
public class MemberBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8991085675293565021L;

	/**
	 * 社员编号
	 */
	private int memberId;

	/**
	 * 朋友状态 :0：自己 1：朋友 2：朋友的朋友 3：申请状态 4：陌生人
	 */
	private int friendStatus;

	/**
	 * 基本信息
	 */
	private MemberBasicBean basicInfo;
	// private Object basicInfo;

	/**
	 * 联系信息
	 */
	private MemberContactBean contactInfo;
	// private Object contactInfo;

	/**
	 * 账户信息
	 */
	private MemberAccountBean accountInfo;
	// private Object accountInfo;

	/**
	 * 现居住地址信息
	 */
	private MemberAddressBean residenceAddress;
	// private Object residenceAddress;

	/**
	 * 出生地地址信息
	 */
	private MemberAddressBean hometownAddress;
	// private Object hometownAddress;

	/**
	 * 学校信息
	 */
	private MemberSchoolBean schoolInfo;
	// private Object schoolInfo;

	/**
	 * 公司信息
	 */
	private MemberCompanyBean companyInfo;
	// private Object companyInfo;

	/**
	 * 情感对象信息
	 */
	private MemberEmotionBean emotionInfo;
	// private Object emotionInfo;

	/**
	 * 状态及统计信息 包括登录信息、各种关系以及资源的数量等统计信息
	 */
	private MemberStatusBean statusInfo;
	// private Object statusInfo;

	/**
	 * 是否在线 0: 不在线 1: 在线
	 */
	private int online;

	/**
	 * 权限信息
	 */
	private MemberAuthoritySettingBean authoritySettingInfo;
	// private Object authoritySettingInfo;

	/**
	 * 登录次数
	 */
	private int loadNum;

	/**
	 * 朋友动态数量
	 */
	private int friendTrendNum;

	/**
	 * 朋友数量
	 */
	private int friendNum;
	/**
	 * 朋友备注
	 */
	private String friendRemarkName;

	public MemberContactBean getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(MemberContactBean contactInfo) {
		this.contactInfo = contactInfo;
	}

	public MemberAccountBean getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(MemberAccountBean accountInfo) {
		this.accountInfo = accountInfo;
	}

	public MemberAddressBean getResidenceAddress() {
		return residenceAddress;
	}

	public void setResidenceAddress(MemberAddressBean residenceAddress) {
		this.residenceAddress = residenceAddress;
	}

	public MemberAddressBean getHometownAddress() {
		return hometownAddress;
	}

	public void setHometownAddress(MemberAddressBean hometownAddress) {
		this.hometownAddress = hometownAddress;
	}

	public MemberSchoolBean getSchoolInfo() {
		return schoolInfo;
	}

	public void setSchoolInfo(MemberSchoolBean schoolInfo) {
		this.schoolInfo = schoolInfo;
	}

	public MemberCompanyBean getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(MemberCompanyBean companyInfo) {
		this.companyInfo = companyInfo;
	}

	public MemberEmotionBean getEmotionInfo() {
		return emotionInfo;
	}

	public void setEmotionInfo(MemberEmotionBean emotionInfo) {
		this.emotionInfo = emotionInfo;
	}

	public MemberStatusBean getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(MemberStatusBean statusInfo) {
		this.statusInfo = statusInfo;
	}

	public MemberAuthoritySettingBean getAuthoritySettingInfo() {
		return authoritySettingInfo;
	}

	public void setAuthoritySettingInfo(
			MemberAuthoritySettingBean authoritySettingInfo) {
		this.authoritySettingInfo = authoritySettingInfo;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getFriendStatus() {
		return friendStatus;
	}

	public void setFriendStatus(int friendStatus) {
		this.friendStatus = friendStatus;
	}

	public MemberBasicBean getBasicInfo() {
		return basicInfo;
	}

	public void setBasicInfo(MemberBasicBean basicInfo) {
		this.basicInfo = basicInfo;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public int getLoadNum() {
		return loadNum;
	}

	public void setLoadNum(int loadNum) {
		this.loadNum = loadNum;
	}

	public int getFriendTrendNum() {
		return friendTrendNum;
	}

	public void setFriendTrendNum(int friendTrendNum) {
		this.friendTrendNum = friendTrendNum;
	}

	public int getFriendNum() {
		return friendNum;
	}

	public void setFriendNum(int friendNum) {
		this.friendNum = friendNum;
	}

	public String getFriendRemarkName() {
		return friendRemarkName;
	}

	public void setFriendRemarkName(String friendRemarkName) {
		this.friendRemarkName = friendRemarkName;
	}
}
