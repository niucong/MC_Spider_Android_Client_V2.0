package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class MemberAccountBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 社员编号
	 */
	private int memberId;

	/**
	 * 社员姓名
	 */
	private String memberName;

	/**
	 * 社员金币数量
	 */
	private int memberGold;

	/**
	 * 社员工分数量
	 */
	private int memberScore;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 登录名
	 */
	private String loadName;

	/**
	 * 已绑定的邮箱
	 */
	private String email;

	/**
	 * 社员邮箱绑定状态 0：未设置 1：绑定 2：未绑定
	 */
	private int emailCheckStatus;

	/**
	 * 社员短号
	 */
	private int memberShort;

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public int getMemberGold() {
		return memberGold;
	}

	public void setMemberGold(int memberGold) {
		this.memberGold = memberGold;
	}

	public int getMemberScore() {
		return memberScore;
	}

	public void setMemberScore(int memberScore) {
		this.memberScore = memberScore;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLoadName() {
		return loadName;
	}

	public void setLoadName(String loadName) {
		this.loadName = loadName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getEmailCheckStatus() {
		return emailCheckStatus;
	}

	public void setEmailCheckStatus(int emailCheckStatus) {
		this.emailCheckStatus = emailCheckStatus;
	}

	public int getMemberShort() {
		return memberShort;
	}

	public void setMemberShort(int memberShort) {
		this.memberShort = memberShort;
	}

}
