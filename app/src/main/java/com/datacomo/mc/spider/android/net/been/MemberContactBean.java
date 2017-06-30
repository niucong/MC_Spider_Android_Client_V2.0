package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class MemberContactBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 社员编号
	 */
	private int memberId;

	/**
	 * 手机号码
	 */
	private String memberPhone;

	/**
	 * 社员手机号码可见状态设置
	 */
	// private PrivacyLevel viewPhoneSetting;
	private String viewPhoneSetting;

	/**
	 * 邮箱
	 */
	private String memberMail;

	/**
	 * 社员邮箱绑定状态 0：未设置 1：绑定 2：未绑定
	 */
	private int memberMailBindStatus;

	/**
	 * 社员邮箱隐私设置
	 */
	// private PrivacyLevel viewMailSetting;
	private String viewMailSetting;

	/**
	 * QQ
	 */
	private String memberQQ;

	/**
	 * 飞信
	 */
	private String memberFetion;

	/**
	 * MSN
	 */
	private String memberMSN;

	/**
	 * 社员即时通讯软件账号隐私设置
	 */
	// private PrivacyLevel viewIMSetting;
	private String viewIMSetting;

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public String getViewPhoneSetting() {
		return viewPhoneSetting;
	}

	public void setViewPhoneSetting(String viewPhoneSetting) {
		this.viewPhoneSetting = viewPhoneSetting;
	}

	public String getMemberMail() {
		return memberMail;
	}

	public void setMemberMail(String memberMail) {
		this.memberMail = memberMail;
	}

	public int getMemberMailBindStatus() {
		return memberMailBindStatus;
	}

	public void setMemberMailBindStatus(int memberMailBindStatus) {
		this.memberMailBindStatus = memberMailBindStatus;
	}

	public String getViewMailSetting() {
		return viewMailSetting;
	}

	public void setViewMailSetting(String viewMailSetting) {
		this.viewMailSetting = viewMailSetting;
	}

	public String getMemberQQ() {
		return memberQQ;
	}

	public void setMemberQQ(String memberQQ) {
		this.memberQQ = memberQQ;
	}

	public String getMemberFetion() {
		return memberFetion;
	}

	public void setMemberFetion(String memberFetion) {
		this.memberFetion = memberFetion;
	}

	public String getMemberMSN() {
		return memberMSN;
	}

	public void setMemberMSN(String memberMSN) {
		this.memberMSN = memberMSN;
	}

	public String getViewIMSetting() {
		return viewIMSetting;
	}

	public void setViewIMSetting(String viewIMSetting) {
		this.viewIMSetting = viewIMSetting;
	}

}
